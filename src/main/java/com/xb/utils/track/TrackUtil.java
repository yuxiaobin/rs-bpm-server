package com.xb.utils.track;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.framework.service.IService;

public class TrackUtil {
	
	private static Map<String,IService<Object,Object>> serviceInstMap = new HashMap<String,IService<Object,Object>>();
	private static Map<String,Method> getPKMethodMap = new HashMap<String,Method>();
	
	public static void addService(String entityClassName, IService<Object,Object> iservice){
		serviceInstMap.put(entityClassName, iservice);
	}

	public static void addPkMethod(String entityClassName, Method getPkMethod){
		getPKMethodMap.put(entityClassName, getPkMethod);
	}
	
	public static IService<Object, Object> getServiceBeanByEntity(String entityName){
		return serviceInstMap.get(entityName);
	}
	
	public static IService<Object, Object> getServiceBeanByEntity(Class<?> entityClazz){
		return getServiceBeanByEntity(entityClazz.getName());
	}
	
	public static Object getPKValue(Object targetObj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method getPkMethod = getPKMethodMap.get(targetObj.getClass().getName());
		if(getPkMethod!=null){
			return getPkMethod.invoke(targetObj);
		}
		return null;
	}
	
	public static Object getObjectFromDB(Object targetObject) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Object pkVal = getPKValue(targetObject);
		return getServiceBeanByEntity(targetObject.getClass()).selectById(pkVal);
	}
	
	public static List<Object> getObjectListByIds(Class<?> objType, List<Object> idList){
		return getServiceBeanByEntity(objType).selectBatchIds(idList);
	}
	
	public static void deleteEntity(String entityClassName, Object id){
		getServiceBeanByEntity(entityClassName).deleteById(id);
	}
	
	public static void updateEntity(String entityClassName, Object entity){
		getServiceBeanByEntity(entityClassName).updateById(entity);
	}
	
	public static void insertEntity(String entityClassName, Object entity){
		getServiceBeanByEntity(entityClassName).insert(entity);
	}
}
