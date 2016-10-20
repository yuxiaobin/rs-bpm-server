package com.xb.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xb.base.CUBaseTO;
import com.xb.common.WFConstants;
import com.xb.service.impl.WfTrackUtilFactory;
import com.xb.utils.UserSessionUtil;
import com.xb.utils.track.TrackUtil;
import com.xb.utils.track.WfTrackLogJob;

@Aspect
@Component
public class MyAop {
	
	@Autowired
	WfTrackUtilFactory trackUtil;
	
	private ThreadLocal<List<Object>> beforeObjectHolder = new ThreadLocal<>();

	private Logger logger = LogManager.getLogger(getClass());

	@Pointcut("execution(public * com.baomidou.mybatisplus.mapper.BaseMapper.insert*(..))")
	private void baseMapInsert(){
		
	}
	@Pointcut("execution(public * com.baomidou.mybatisplus.mapper.BaseMapper.update*(..))")
	private void baseMapUpdate(){
		
	}
	@Pointcut("execution(public * com.baomidou.mybatisplus.mapper.BaseMapper.delete*(..))")
	private void baseMapDelete(){
		
	}
	
	@Pointcut("baseMapInsert() ||baseMapUpdate() || baseMapDelete()")//|| insertService()
	public void embeddedPt(){
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before("embeddedPt()")
	public void doBeforeModify(JoinPoint joinPoint) throws Throwable {
		RequestAttributes attr = RequestContextHolder.getRequestAttributes();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		if(attr==null){
			logger.warn("RequestAttributes is null for methodName="+methodName+",args[0]="+args[0]);
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) attr).getRequest();
		CUBaseTO baseTO = UserSessionUtil.getUserSession();
		if(baseTO==null){
			Map<String,Object> userinfo = (Map<String, Object>) request.getSession().getAttribute("USERINFO");
			String userId = null;
			if(userinfo==null || userinfo.get("userId") ==null){
				userId = "system";
			}else{
				userId = (String)userinfo.get("userId");
			}
			baseTO = new CUBaseTO();
			baseTO.setCreatedBy(userId);
			baseTO.setUpdatedBy(userId);
			UserSessionUtil.setUserSession(baseTO);
		}
		
		boolean isInsert = false;
		boolean isUpdate = false;
		if(methodName.startsWith("insert") || methodName.startsWith("create") ){
			isInsert = true;
		}
		if(methodName.startsWith("update")){
			isUpdate = true;
		}
		boolean trackFlag = !StringUtils.isEmpty(request.getHeader(WFConstants.API_PARM_TRACK_ID));
		if(isInsert||isUpdate){
			for(int i=0;i<args.length;++i){
				if(args[i] instanceof CUBaseTO){
					CUBaseTO baseArg = (CUBaseTO)args[i];
					if(isInsert){
						baseArg.setCreatedBy(baseTO.getCreatedBy());
						if(baseArg.getCreatedDt()==null){
							baseArg.setCreatedDt(new Date());
						}
						if(trackFlag){
							List<Object> beforeList = new ArrayList<>(1);
							beforeList.add(baseArg);
							beforeObjectHolder.set(beforeList);
						}
					}
					if(isUpdate){
						if(trackFlag){
							List<Object> beforeList = new ArrayList<>(1);
							beforeList.add(((BaseMapper) joinPoint.getTarget()).selectById(TrackUtil.getPKValue(baseArg)));
							beforeObjectHolder.set(beforeList);
						}
						baseArg.setUpdatedBy(baseTO.getUpdatedBy());
						baseArg.setUpdatedDt(new Date());
					}
				}
				else if(args[i] instanceof List){//insert batch. it's better not to use as oracle not support
					String errMsg = "batch insert or update, should be avoid";
					System.err.println(errMsg);
					logger.error(errMsg);
				}
			}
			return;
		}
		if(!trackFlag){
			return;
		}
		BaseMapper<Object, Object> iser = (BaseMapper) joinPoint.getTarget();
		if(methodName.equals("deleteSelective")){
			List<Object> beforeList = iser.selectList(new EntityWrapper(args[0]));
			beforeObjectHolder.set(beforeList);
			return;
		}
		if(methodName.equals("deleteBatchIds")){
			List<Object> beforeList = iser.selectBatchIds(Arrays.asList(args));
			beforeObjectHolder.set(beforeList);
			return;
		}
		if(methodName.equals("deleteById")){
			Object beforeDeleteTO = iser.selectById(args[0]);
			List<Object> beforeList = new ArrayList<>(1);
			beforeList.add(beforeDeleteTO);
			beforeObjectHolder.set(beforeList);
			return;
		}
	}
	
	@SuppressWarnings({ "unchecked" })
	@After("embeddedPt()")
	public void afterPt(JoinPoint joinPoint) throws Throwable {
		List<Object> beforeList = beforeObjectHolder.get();
		if(beforeList==null){
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String trackId = request.getHeader(WFConstants.API_PARM_TRACK_ID);
		String userId = null;
		CUBaseTO baseTO = UserSessionUtil.getUserSession();
		if(baseTO==null){
			Map<String,Object> userinfo = (Map<String, Object>) request.getSession().getAttribute("USERINFO");
			if(userinfo==null || userinfo.get("userId") ==null){
				userId = "trackSystem";
			}else{
				userId = (String)userinfo.get("userId");
			}
		}
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		String entityClassName = args[0].getClass().getName();
		if(methodName.startsWith("insert")){
			List<Object> afterList = new ArrayList<Object>(1);
			if(args[0] instanceof List){
				afterList.addAll((List<Object>)args[0]);
			}else{
				afterList.add(args[0]);
			}
			trackUtil.createTrack(trackId, userId, entityClassName, beforeList, afterList, WfTrackLogJob.OPT_TYPE_INSERT);
			return;
		}
		if(methodName.startsWith("update")){
			trackUtil.createTrack(trackId, userId, entityClassName, beforeList, null, WfTrackLogJob.OPT_TYPE_UPDATE);
			return;
		}
		if(methodName.startsWith("delete")){
			trackUtil.createTrack(trackId, userId, entityClassName, beforeList, null, WfTrackLogJob.OPT_TYPE_DELETE);
		}
	}

}
