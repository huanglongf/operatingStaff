package com.gomeplus.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gomeplus.cache.GlobalCache;
import com.gomeplus.utils.OutPutJsonUtil;

public class BaseModule {

	/***
	 * 默认的处理方法，可以处理结果集json中包含明细数据和分页总数两部分内容
	 * @return
	 */
	protected JSONObject default_module(){
		
		JSONObject tempObject = GlobalCache.QueryResult;
		JSONObject totalNumJsonObject = null;//分页总数json
		JSONArray  detailJsonObject = null;//明细数据json
		for(String key : tempObject.keySet()){
			JSONArray array = (JSONArray) tempObject.get(key);
			if(array!=null && array.size() == 0){
				detailJsonObject = array;
			}else if(String.valueOf(array.get(0)).contains("total_num")){
				totalNumJsonObject = (JSONObject) array.get(0);
			}else{
				detailJsonObject = (JSONArray) tempObject.get(key); 
			}
		}
		JSONObject dataObject = new JSONObject();
		dataObject.put("total_num", totalNumJsonObject.get("total_num"));
		dataObject.put("list", detailJsonObject);
		return OutPutJsonUtil.out(dataObject);
	}
}
