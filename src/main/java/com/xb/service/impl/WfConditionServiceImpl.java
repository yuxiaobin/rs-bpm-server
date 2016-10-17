package com.xb.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	private static final String INFO_MSG_NO_BUZ_DATA_DEFINED = "No func vars defined for [refMkid=%s]";

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
		boolean condResut = evaluateExpression(condTask.getCondExp(), refMkid, wfInstNum, condTask.getWfId());
		
		WfTaskConn connParm = new WfTaskConn();
		connParm.setSourceTaskId(condTask.getTaskId());
		connParm.setConnVal(String.valueOf(condResut));
		WfTaskConn conn = taskConnService.selectOne(connParm);
		if(conn!=null){
			return conn.getTargetTaskId();
		}
		return null;
	}
	
	/**
	 * 条件节点表达式：
	 * custVar1==1 and test_amount==1 and test_age==1
	 * 
	 * custVar1:自定义变量，表达式为：select count(1) from aaa where aaa.stes=:test_amount
	 */
	public boolean evaluateExpression(String expression, String refMkid, int wfInstNum, String wfId){
		String expressionBefore = expression;
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		if(StringUtils.isEmpty(expression)){
			return true;
		}else{
			JSONArray funcVars = entityFactory.getFuncVariables(refMkid);
			JSONObject entityData = getFuncVarValues(refMkid, wfInstNum);
			if(entityData!=null){
				if(!funcVars.isEmpty()){
					for(int i=0;i<funcVars.size();++i){
						JSONObject js = funcVars.getJSONObject(i);
						String varCode = js.getString(WFConstants.FuncVarsParm.PARM_VAR_CODE);//is column name
						Pattern p = Pattern.compile(varCode+"[=><! ]");
						Matcher m = p.matcher(expression);
						if(m.find()){
							expression = expression.replace(varCode, "#"+varCode);//replace total_count with #total_count, which can be evaluated by Spring EL
							Object val = entityData.get(varCode.toUpperCase());
							context.setVariable(varCode, val);
						}
					}
				}
			}
			
			WfCustVars custVarParm = new WfCustVars();
			custVarParm.setWfId(wfId);
			custVarParm.setVarType(WFConstants.CustVarTypes.VAR_TYPE_VAR);
			List<WfCustVars> custVars = custVarsService.selectList(custVarParm);//自定义变量
			/*
			 * 如果自定义变量中使用了预定义变量(:test_amount, 以:开头的变量)
			 * 需要将预定义变量（也就是实体中定义的功能变量）替换成值。
			 */
			if(custVars!=null && !custVars.isEmpty()){
				for(WfCustVars var: custVars){
					String varCode = var.getVarCode();
					Pattern p = Pattern.compile(varCode+"[=><! ]");
					Matcher m = p.matcher(expression);
					if(m.find()){
						String custVarExp = var.getVarExpression();
						if(custVarExp==null){
							//TODO: need test if the expression can always be true @1017
							continue;
						}
						custVarExp = custVarExp.toUpperCase();
						if(entityData!=null && !entityData.isEmpty()){
							for(Map.Entry<String, Object> ety : entityData.entrySet()){
								String key = ":"+ety.getKey();
								if(custVarExp.contains(key)){
									Object val = ety.getValue();
									if(val instanceof String){
										custVarExp = custVarExp.replace(key, "'"+ety.getValue()+"'");
									}else{
										custVarExp = custVarExp.replace(key, ""+ety.getValue());
									}
								}
							}
						}
						List<JSONObject> custVarDataList = getEntityDataListBySql(custVarExp,false);
						if(custVarDataList==null || custVarDataList.isEmpty()){
							log.warn("no data found for custVar.expression="+var.getVarExpression());
							continue;
						}
						JSONObject custVarData = custVarDataList.get(0);
						if(custVarData.isEmpty()){
							log.warn("query data is empty for custVar.expression="+var.getVarExpression()+", after transfered custVarExp="+custVarExp);
						}else{
							Object expResult = custVarData.values().iterator().next();
							expression = expression.replace(varCode, "#"+varCode);
							context.setVariable(varCode, expResult);
						}
					}
				}
			}
			log.debug("condExp before is "+expressionBefore+", after is "+ expression);
			return parser.parseExpression(expression).getValue(context, Boolean.class);
		}
	}

	public JSONObject getFuncVarValues(String refMkid, Integer wfInstNum){
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
			
			List<JSONObject> entityDataList = getEntityDataListBySql(sb.toString(), false);
			if(entityDataList==null || entityDataList.isEmpty()){
				log.error(String.format(ERROR_MSG_NO_BUZ_DATA_FOUND, refMkid,wfInstNum));
				return null;
			}else{
				return entityDataList.get(0);
			}
		}
		log.info(String.format(INFO_MSG_NO_BUZ_DATA_DEFINED, refMkid));
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<JSONObject> getEntityDataListBySql(String sql, boolean withOrder){
		Map<String,Object> parm = new HashMap<String,Object>();
		parm.put("getBuzDataSql", sql);
		if(withOrder){
			List<LinkedHashMap> result =  baseMapper.getBuzDataByEntityWithOrder(parm);
			if(result!=null){
				List<JSONObject> returnList = new ArrayList<JSONObject>(result.size());
				for(LinkedHashMap record:result){
					JSONObject json = new JSONObject(true);
					json.putAll(record);
					returnList.add(json);
				}
				return returnList;
			}
			return null;
		}else{
			return baseMapper.getBuzDataByEntity(parm);
		}
	}
	
}
