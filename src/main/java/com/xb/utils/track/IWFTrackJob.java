package com.xb.utils.track;

public interface IWFTrackJob<T> extends Runnable {

	public static final String OPT_TYPE_INSERT = "I";
	public static final String OPT_TYPE_UPDATE = "U";
	public static final String OPT_TYPE_DELETE = "D";
	
}
