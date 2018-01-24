package com.gomeplus.cache;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

import com.alibaba.fastjson.JSONObject;

public class GlobalCache {
	
	public static String ReqUrl;//本次请求url
	
	public static Map<String, String> Params = new HashMap<String, String>();//本次请求参数map
	
	public static JSONObject QueryResult = new JSONObject();//本次查询返回的多条sql的合并jsonObject，key为sql_id,value为JsonArray数据
	
	public static boolean ControllerStatus = false;//本次请求controller层是否出现异常
	
	public static JSONObject LogEntity = new JSONObject();//全局日志对象
	
	public static JSONObject SqlEntity = new JSONObject();//全局日志对象中保存sql的部分
	
	public static Map<String,String> SqlContainer = new HashMap<String, String>();//预加载入缓存的所有sql,key为sql_id，value为sql语句
	
	public static Map<String,Document> SqlDocumentContainer = new HashMap<String, Document>();//启动加载的所有xml文件的doc对象
	
	public static Map<String, String> ReqSqlContainer = new HashMap<String, String>();//本次请求的接口对应的sql上下文，用于缓存校验
	
	public static Map<String, String> ReqParams = new HashMap<String, String>();//本次请求的接口的参数，用于缓存校验
}
