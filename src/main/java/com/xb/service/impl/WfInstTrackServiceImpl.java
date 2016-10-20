package com.xb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.framework.service.impl.CommonServiceImpl;
import com.xb.persistent.WfInstTrack;
import com.xb.persistent.WfInstTracklog;
import com.xb.persistent.mapper.WfInstTrackMapper;
import com.xb.service.IWfInstTrackService;
import com.xb.service.IWfInstTracklogService;
import com.xb.utils.track.IWFTrackJob;
import com.xb.utils.track.TrackUtil;

/**
 *
 * WfInstTrack 表数据服务层接口实现类
 *
 */
@Service
public class WfInstTrackServiceImpl extends CommonServiceImpl<WfInstTrackMapper, WfInstTrack> implements IWfInstTrackService {

	@Autowired
	IWfInstTracklogService tracklogService;
	
	@Override
	public boolean doRollback(String trackId) throws Exception {
		WfInstTrack trackTO = this.selectById(trackId);
		if("T".equals(trackTO.getRollbackFlag())){
			System.err.println("failed to rollback for trackId="+trackId+" due to already rollbacked");
			return false;
		}
		WfInstTracklog logParm = new WfInstTracklog();
		logParm.setTrackId(trackId);
		List<WfInstTracklog> logList = tracklogService.selectList(logParm);
		for(WfInstTracklog logTO:logList){
			String optType = logTO.getDataOption();
			String entityClassName = logTO.getEntityClassType();
			switch (optType) {
			case IWFTrackJob.OPT_TYPE_INSERT:
				TrackUtil.deleteEntity(entityClassName, TrackUtil.getPKValue(JSON.parseObject(logTO.getDataAfter(), Class.forName(entityClassName))));
				break;
			case IWFTrackJob.OPT_TYPE_UPDATE:
				TrackUtil.updateEntity(entityClassName, JSON.parseObject(logTO.getDataBefore(), Class.forName(entityClassName)));
				break;
			case IWFTrackJob.OPT_TYPE_DELETE:
				TrackUtil.insertEntity(entityClassName, JSON.parseObject(logTO.getDataBefore(), Class.forName(entityClassName)));
				break;
			default:
				break;
			}
		}
		trackTO.setRollbackFlag("T");
		this.updateById(trackTO);
		return true;
	}


}