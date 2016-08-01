package com.xb.aop;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.framework.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.CUBaseTO;

@Aspect
@Component
public class MyAop {

	private Logger logger = Logger.getLogger(getClass());

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
	
	@Pointcut("baseInsert() || baseMapInsert()")//|| insertService()
	private void embeddedPt(){
		
	}

	@Before("embeddedPt()")
	public void doBeforeModify(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String userId = (String) request.getSession().getAttribute("USERINFO");
		if(userId==null){
			userId = "system";
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
		CUBaseTO baseTO = new CUBaseTO();
		baseTO.setCreatedBy(userId);
		baseTO.setUpdatedBy(userId);
		ServiceImpl.threadLocal.set(baseTO);
		/*Object[] args = joinPoint.getArgs();
		if(args!=null){
			for(int i=0;i<args.length;++i){
				if(args[i] instanceof BaseTO){
					BaseTO baseTO = (BaseTO)args[i];
					if(isInsert){
						baseTO.setCreatedBy(userId);
						baseTO.setCreatedDt(new Date());
					}
					if(isUpdate){
						baseTO.setUpdatedBy(userId);
						baseTO.setUpdatedDt(new Date());
					}
				}
				if(args[i] instanceof List){
					List list = (List) args[i];
					if(list!=null){
						for(Object obj : list){
							if(obj instanceof BaseTO){
								BaseTO baseTO = (BaseTO)obj;
								if(isInsert){
									baseTO.setCreatedBy(userId);
									baseTO.setCreatedDt(new Date());
								}
								if(isUpdate){
									baseTO.setUpdatedBy(userId);
									baseTO.setUpdatedDt(new Date());
								}
							}
						}
					}
				}
			}
		}*/
//		System.out.println("userId = " + userId);
//		System.out.println("target = " + target);
//		System.out.println("args = " + args);

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
