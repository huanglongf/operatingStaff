package com.gomeplus.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gomeplus.cache.GlobalCache;

public class LogRecorder{
	
	private static Logger logger= LoggerFactory.getLogger(LogRecorder.class);

	public static void log(){
		
//		StringBuilder logMain = new StringBuilder();
		JSONObject logEntity = GlobalCache.LogEntity;
//		logMain.append("visit_time:");
//		logMain.append(logEntity.get("visit_time"));
//		logMain.append("\t");
//		logMain.append("req_time:");
//		logMain.append(logEntity.get("req_time"));
//		logMain.append("\t");
//		logMain.append("ip:");		
//		logMain.append(logEntity.get("ip"));
//		logMain.append("\t");
//		logMain.append("req_url:");		
//		logMain.append(logEntity.get("req_url"));
//		logMain.append("\t");
//		logMain.append("req_params:");
//		logMain.append(logEntity.get("params"));
//		logMain.append("\t");
//		JSONObject sqlObject = (JSONObject) logEntity.get("sql");
//		for (String key : sqlObject.keySet()) {
//			logMain.append(key);		
//		    logMain.append(":");
//		    logMain.append(sqlObject.get(key));
//		    logMain.append("\t");
//		}
		logger.trace(logEntity.toJSONString());
	}
}
