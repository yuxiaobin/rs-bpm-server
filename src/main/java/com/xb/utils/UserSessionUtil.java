package com.xb.utils;

import com.xb.base.CUBaseTO;

public class UserSessionUtil {
	
	private static ThreadLocal<CUBaseTO> userSessionHolder = new ThreadLocal<CUBaseTO>();
	
	public static CUBaseTO getUserSession(){
		return userSessionHolder.get();
	}
	
	public static void setUserSession(CUBaseTO baseTO){
		userSessionHolder.set(baseTO);
	}

}
