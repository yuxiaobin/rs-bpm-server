package com.xb.aop;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xb.base.CUBaseTO;
import com.xb.utils.UserSessionUtil;

@Aspect
@Component
public class MyAop {

	private Logger logger = LogManager.getLogger(getClass());

//	@Pointcut("execution(* *Service*.insert(..))")
	/*@Pointcut("execution(public * com.xb.service..*.*(..))")
	public void insertPoint() {

	}
	@Pointcut("execution(public * com.xb.controller..*.create*(..))")
	public void insertInCtrl() {
		
	}*/
	@Pointcut("execution(public * com.xb.service..*.*(..))")
	public void insertService() {
		
	}
	@Pointcut("execution(public * com.baomidou.framework.service..*.*(..))")
	public void modifyMethod(){
		
	}
	@Pointcut("execution(public * com.xb.persistent.mapper..*.*(..))")
	public void baseInsert(){
		
	}
	@Pointcut("execution(public * com.baomidou.mybatisplus.mapper.BaseMapper.insert(..))")
	public void baseMapInsert(){
		
	}
	@Pointcut("execution(public * com.baomidou.mybatisplus.mapper.BaseMapper.update*(..))")
	public void baseMapUpdate(){
		
	}
	
	@Pointcut("baseMapInsert() ||baseMapUpdate()")//|| insertService()
	private void embeddedPt(){
		
	}

	@Before("embeddedPt()")
	public void doBeforeModify(JoinPoint joinPoint) throws Throwable {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		CUBaseTO baseTO = null;
		if(attrs==null){
			baseTO = UserSessionUtil.getUserSession();
			if(baseTO==null){
				baseTO = new CUBaseTO();
			}
			baseTO.setCreatedBy("mockup");
			baseTO.setUpdatedBy("mockup");
			UserSessionUtil.setUserSession(baseTO);
		}else{
			HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
			@SuppressWarnings("unchecked")
			Map<String,Object> userinfo = (Map<String, Object>) request.getSession().getAttribute("USERINFO");
			String userId = null;
			if(userinfo==null || userinfo.get("userId") ==null){
				userId = "system";
			}else{
				userId = (String)userinfo.get("userId");
			}
			baseTO = UserSessionUtil.getUserSession();
			if(baseTO==null){
				baseTO = new CUBaseTO();
			}
			baseTO.setCreatedBy(userId);
			baseTO.setUpdatedBy(userId);
			UserSessionUtil.setUserSession(baseTO);
		}
		String methodName = joinPoint.getSignature().getName();
		boolean isInsert = false;
		boolean isUpdate = false;
		if(methodName.startsWith("insert") || methodName.startsWith("create")){
			isInsert = true;
		}
		if(methodName.startsWith("update")){
			isUpdate = true;
		}
		logger.debug("doBeforeModify=========isInsert="+isInsert+",isUpdate="+isUpdate);
		if(isInsert||isUpdate){
			Object[] args = joinPoint.getArgs();
			if(args!=null){
				for(int i=0;i<args.length;++i){
					if(args[i] instanceof CUBaseTO){
						CUBaseTO baseArg = (CUBaseTO)args[i];
						if(isInsert){
							baseArg.setCreatedBy(baseTO.getCreatedBy());
							if(baseArg.getCreatedDt()==null){
								baseArg.setCreatedDt(new Date());
							}
						}
						if(isUpdate){
							baseArg.setUpdatedBy(baseTO.getUpdatedBy());
							baseArg.setUpdatedDt(new Date());
						}
					}
				}
			}
			
		}
		// // 记录下请求内容
		// logger.info("URL : " + request.getRequestURL().toString());
		// logger.info("HTTP_METHOD : " + request.getMethod());
		// logger.info("IP : " + request.getRemoteAddr());
		// logger.info("CLASS_METHOD : " +
		// joinPoint.getSignature().getDeclaringTypeName() + "." +
		// joinPoint.getSignature().getName());
		// logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

	}

}
