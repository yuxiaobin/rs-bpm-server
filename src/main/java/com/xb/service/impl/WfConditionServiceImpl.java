package com.xb.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.rshare.service.wf.annotations.WfEntityBeanFactory;
import com.xb.common.WFConstants;
import com.xb.persistent.WfCustVars;
import com.xb.persistent.WfTask;
import com.xb.persistent.WfTaskConn;
import com.xb.persistent.mapper.WfCustVarsMapper;
import com.xb.service.IWfConditionService;
import com.xb.service.IWfCustVarsService;
import com.xb.service.IWfTaskConnService;

@Service
public class WfConditionServiceImpl extends CommonServiceImpl<WfCustVarsMapper, WfCustVars> implements IWfConditionService {
	
	private static Logger log = LogManager.getLogger(WfConditionServiceImpl.class);
	
	private static final String ERROR_MSG_NO_BUZ_DATA_FOUND = "FATAL ERROR====no buz data found for [refMkid=%s],[wfInstNum=%s]";

	@Autowired
	WfEntityBeanFactory entityFactory;
	@Autowired
	IWfCustVarsService custVarsService;
	@Autowired
	IWfTaskConnService taskConnService;
	
	@Override
	public String getNextTaskIdByCondResult(WfTask condTask, String refMkid, int wfInstNum) {
		if(condTask==null){
			return null;
		}
		String condExp = condTask.getCondExp();
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		boolean condResut = false;
		if(StringUtils.isEmpty(condExp)){
			condResut = true;
		}else{
			String tableName = entityFactory.getFuncEntityTable(refMkid);
			JSONArray funcVars = entityFactory.getFuncVariables(refMkid);
			if(funcVars!=null && !funcVars.isEmpty()){
				StringBuilder sb = new StringBuilder();
				sb.append("select ");
				for(int i=0;i<funcVars.size();++i){
					JSONObject funcVar = funcVars.getJSONObject(i);
					String columnName = funcVar.getString(WFConstants.FuncVarsParm.PARM_VAR_CODE);
					sb.append(columnName.toUpperCase()).append(",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(" from ").append(tableName);
				sb.append(" where ").append(tableName).append("_WF_INST_NUM=").append(wfInstNum);
				
				Map<String,Object> parm = new HashMap<String,Object>();
				parm.put("getBuzDataSql", sb.toString());
				List<JSONObject> entityDataList = baseMapper.getBuzDataByEntity(parm);
				JSONObject entityData = null;
				if(entityDataList==null || entityDataList.isEmpty()){
					log.error(String.format(ERROR_MSG_NO_BUZ_DATA_FOUND, refMkid,wfInstNum));
				}else{
					entityData = entityDataList.get(0);
					if(!funcVars.isEmpty()){
						for(int i=0;i<funcVars.size();++i){
							JSONObject js = funcVars.getJSONObject(i);
							String varCode = js.getString(WFConstants.FuncVarsParm.PARM_VAR_CODE);//is column name
							if(condExp.contains(varCode)){
								condExp = condExp.replace(varCode, "#"+varCode);//replace total_count with #total_count, which can be evaluated by Spring EL
								Object val = entityData.get(varCode.toUpperCase());
								context.setVariable(varCode, val);
							}
						}
					}
				}
			}
			
			WfCustVars custVarParm = new WfCustVars();
			custVarParm.setWfId(condTask.getWfId());
			custVarParm.setVarType("V");
			List<WfCustVars> custVars = custVarsService.selectList(custVarParm);//自定义变量
			
			if(custVars!=null && !custVars.isEmpty()){
				for(WfCustVars var: custVars){
					String varCode = var.getVarCode();
					Pattern p = Pattern.compile(varCode+"[= ]");
					Matcher m = p.matcher(condExp);
//					System.out.println(m.find());
					if(m.find()){
//						if(condExp.contains(varCode)){
						Map<String,Object> parm = new HashMap<String,Object>();
						parm.put("getBuzDataSql", var.getVarExpression());
						List<JSONObject> entityDataList = baseMapper.getBuzDataByEntity(parm);
						if(entityDataList==null || entityDataList.isEmpty()){
							log.warn("no data found for custVar.expression="+var.getVarExpression());
							continue;
						}
						JSONObject record = entityDataList.get(0);
						if(record.isEmpty()){
							log.warn("query data is empty for custVar.expression="+var.getVarExpression());
						}else{
							Object expResult = record.values().iterator().next();
							condExp = condExp.replace(varCode, "#"+varCode);
							context.setVariable(varCode, expResult);
						}
					}
				}
			}
		}
		log.debug("condExp before is "+condTask.getCondExp()+", after is "+ condExp);
		condResut = parser.parseExpression(condExp).getValue(context, Boolean.class);
		
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(condTask.getTaskId());
		connParm.setConnVal(String.valueOf(condResut));
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getTargetTaskId();
		}
		return null;
	}

}
