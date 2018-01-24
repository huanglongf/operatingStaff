package com.gomeplus.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.gomeplus.GlobalEnum;

/*
 * 输出json对象工具类
 */
public class OutPutJsonUtil {

	public static JSONObject out(GlobalEnum code){
		JSONObject result = new JSONObject();
		switch (code) {
		case success:	//成功
			result.put("code", "200");
			result.put("message", "成功");
			break;
		case unsupportinterface://不支持的接口
			result.put("code", "501");
			result.put("message", "不支持的接口");
			break;
		case systemerror://系统异常
			result.put("code", "500");
			result.put("message", "系统异常");
			break;
		case interfaceformaterror://接口格式错误，包含非法字符等
			result.put("code", "502");
			result.put("message", "接口格式错误,包含非法字符");
			break;
		}
		return result;
	}	
	
	public static JSONObject out(JSONObject data){
		JSONObject result = new JSONObject();
		result.put("code", "200");
		result.put("message", "Success");
		result.put("data", data);
		return result;
	}
	
	public static void outForResponse(HttpServletResponse response,String msg) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(msg);
	}	
}
