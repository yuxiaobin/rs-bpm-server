package com.xb.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.IService;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xb.persistent.WfVersion;
import com.xb.service.IWfInstTrackService;
import com.xb.service.IWfInstTracklogService;
import com.xb.utils.track.TrackUtil;
import com.xb.utils.track.WfTrackJob;
import com.xb.utils.track.WfTrackLogJob;

@Component
public class WfTrackUtilFactory implements BeanFactoryAware{
	
	private BeanFactory beanFactory;
	
	private LoadingCache<String, String> trackIdCache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
			.maximumSize(20).initialCapacity(5).concurrencyLevel(8)
			.build(new CacheLoader<String, String>() {
			@Override
			public String load(String trackId) throws Exception {
				trackIdCache.put(trackId, trackId);
				return trackId;
			}
	});
	
	private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
	
	@Autowired
	private IWfInstTrackService trackService;
	@Autowired
	private IWfInstTracklogService tracklogService;
	
	@PostConstruct
	public void init() throws NoSuchMethodException, SecurityException{
		initServiceInstances();
		initEntityPk();
	}
	
	public void initEntityPk() throws NoSuchMethodException, SecurityException{
		String entityPackage = WfVersion.class.getPackage().getName();
		Reflections reflections = new Reflections(entityPackage);
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(TableName.class);
		for(Class<?> entityClass: classSet){
			Field[] fields = entityClass.getDeclaredFields();
			for(Field field:fields){
				if(field.getAnnotation(TableId.class)!=null){
					String fieldName = field.getName();
					TrackUtil.addPkMethod(entityClass.getName(), entityClass.getDeclaredMethod("get"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1), new Class[] {}));
				}
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initServiceInstances(){
		String servicePackage = this.getClass().getPackage().getName();
		Reflections reflections = new Reflections(servicePackage);
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Service.class);
		for(Class<?> clazz:classSet){
			if(IService.class.isAssignableFrom(clazz)){
				Class[] interfaces = clazz.getInterfaces();
				Object proxy = beanFactory.getBean(interfaces[0]);
				IService iser = (IService)proxy;
				Type[] params = ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments();
				if(params!=null && params.length>1){
					String[] typeArg = params[1].toString().split(" ");
					String entityClassName = null;
					if(typeArg.length>1){
						entityClassName = typeArg[1];
					}else{
						entityClassName = typeArg[0];
					}
					TrackUtil.addService(entityClassName, iser);
				}
			}else{
				System.out.println("clazz is not a ServiceImpl, ignored["+clazz.getName()+"]");
			}
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	
	/**
	 * 异步创建Track记录
	 * @param trackId
	 * @param userId
	 * @param entityClassName
	 * @param beforeList
	 * @param afterList
	 * @param operation
	 */
	public void createTrack(String trackId, String userId, String entityClassName, List<Object> beforeList, List<Object> afterList, String operation){
		if(trackId==null){
			return;
		}
		if(trackIdCache.getIfPresent(trackId)==null){
			executor.execute(new WfTrackJob(trackId, trackService, userId));
			trackIdCache.put(trackId, trackId);
		}
		executor.execute(new WfTrackLogJob(trackId, tracklogService, entityClassName, beforeList, afterList, operation));
	}
	
	
	
}
