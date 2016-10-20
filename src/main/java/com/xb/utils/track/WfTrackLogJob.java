package com.xb.utils.track;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.framework.service.IService;
import com.xb.persistent.WfInstTracklog;

public class WfTrackLogJob implements IWFTrackJob<Object> {
	
	private final IService<WfInstTracklog, String> iservice;
	private final String entityClassName;
	private final List<Object> beforeList;
	private List<Object> afterList;
	private final String operation;
	private final String trackId;

	public WfTrackLogJob(String trackId, IService<WfInstTracklog, String> iservice, String entityClassName, List<Object> beforeList, List<Object> afterList, String operation) {
		super();
		this.iservice = iservice;
		this.beforeList = beforeList;
		this.afterList = afterList;
		this.operation = operation;
		this.trackId = trackId;
		this.entityClassName = entityClassName;
	}

	@Override
	public void run() {
		switch (operation) {
		case OPT_TYPE_INSERT:
			for(Object object:afterList){
				WfInstTracklog tracklog = new WfInstTracklog();
				tracklog.setTrackId(trackId);
				tracklog.setDataBefore(null);
				tracklog.setDataAfter(JSON.toJSONString(object));
				tracklog.setDataOption(operation);
				tracklog.setEntityClassType(entityClassName);
				iservice.insert(tracklog);
			}
			break;
		case OPT_TYPE_UPDATE:
			int size = beforeList.size();
			Map<Object,Object> map = new HashMap<Object,Object>(size);
			List<Object> idList = new ArrayList<Object>(size);
			if(afterList==null){
				try {
					for(int i=0;i<size;++i){
						idList.add(TrackUtil.getPKValue(beforeList.get(i)));
					}
					List<Object> resultList = TrackUtil.getObjectListByIds(beforeList.get(0).getClass(), idList);
					for(int i=0;i<size;++i){
						for(Object result:resultList){
							map.put(TrackUtil.getPKValue(result), result);
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				/*for(int i=0;i<size;++i){
					Object obj = beforeList.get(i);
					try {
						afterList.add(trackUtil.getObjectFromDB(obj));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}*/
			}
			for(int i=0;i<beforeList.size();++i){
				WfInstTracklog tracklog = new WfInstTracklog();
				tracklog.setTrackId(trackId);
				tracklog.setDataBefore(JSON.toJSONString(beforeList.get(i)));
				tracklog.setDataAfter(JSON.toJSONString(map.get(idList.get(i))));
				tracklog.setDataOption(operation);
				tracklog.setEntityClassType(entityClassName);
				iservice.insert(tracklog);
			}
			break;
		case OPT_TYPE_DELETE:
			for(int i=0;i<beforeList.size();++i){
				WfInstTracklog tracklog = new WfInstTracklog();
				tracklog.setTrackId(trackId);
				tracklog.setDataBefore(JSON.toJSONString(beforeList.get(i)));
				tracklog.setDataAfter(null);
				tracklog.setDataOption(operation);
				tracklog.setEntityClassType(entityClassName);
				iservice.insert(tracklog);
			}
			break;
		default:
			System.err.println("invalid operation type:"+operation+", process track ignored");
			break;
		}
		
	}

}
