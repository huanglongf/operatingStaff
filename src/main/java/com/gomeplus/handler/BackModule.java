package com.gomeplus.handler;

import com.alibaba.fastjson.JSONObject;
import com.gomeplus.cache.GlobalCache;
import com.gomeplus.utils.OutPutJsonUtil;

public class BackModule extends BaseModule{
	
	public JSONObject self_category_flow(){
		JSONObject tempObject = GlobalCache.QueryResult;
//		JSONObject o = new JSONObject();
//		for (String key : tempObject.keySet()) {
//			JSONArray array = (JSONArray) tempObject.get(key);
//			o.put("list", array);
//		}
		return OutPutJsonUtil.out(tempObject);
	}
	
	public JSONObject self_category_sale(){
		return OutPutJsonUtil.out(GlobalCache.QueryResult);
	}
}
