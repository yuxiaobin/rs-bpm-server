package com.xb.utils.track;

import java.util.Date;

import com.baomidou.framework.service.IService;
import com.xb.persistent.WfInstTrack;

public class WfTrackJob implements IWFTrackJob<Object> {
	
	private final IService<WfInstTrack, String> iservice;
	private final String userId;
	private final String trackId;

	public WfTrackJob(String trackId, IService<WfInstTrack, String> iservice, String userId) {
		super();
		this.iservice = iservice;
		this.userId = userId;
		this.trackId = trackId;
	}

	@Override
	public void run() {
		WfInstTrack trackTO = new WfInstTrack();
		trackTO.setTrackId(trackId);
		trackTO.setRollbackFlag("F");
		trackTO.setCreatedBy(userId);
		trackTO.setCreatedDt(new Date());
		iservice.insert(trackTO);
	}

}
