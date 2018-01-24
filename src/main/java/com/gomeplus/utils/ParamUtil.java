package com.gomeplus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamUtil {

	private static Logger logger=LoggerFactory.getLogger(ParamUtil.class);
	
	/***
	 * 解析get请求过来的参数，如果带有同比、环比标志，则根据传入日期推算出同比日期和环比日期，放入Map集合
	 * @param request
	 * @return
	 */
	public static Map<String, String> prepareParams(HttpServletRequest request){
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String[]> params = request.getParameterMap();
        boolean isContainTbFlag = false;//传入参数是否包含同比标记
        boolean isContainHbFlag = false;//传入参数是否包含环比标记
        for (String key : params.keySet()){
        	/***
        	 * 对入参进行处理，添加单引号
        	 */
        	String value = params.get(key)[0];
    		/***
    		 * 过滤不需要加''符号的常规入参
    		 */
    		if(key.equals("pnum") || key.equals("psize") || key.equals("st") || key.equals("et") || key.equals("export") ||
    				key.equals("hb") || key.equals("tb") || key.equals("orderby") || key.equals("ordertype")){
    			//donothing!
    		}else{
    			if(value!= null && value.contains(",")){
    	        	String[] split_array = value.split(",");
    	        	StringBuilder builder = new StringBuilder();
    	      		for(int i = 0 ; i < split_array.length ; i++){
    	      			builder.append("'").append(split_array[i]).append("'");
    	      			if(i < split_array.length - 1){
    	      				builder.append(",");
    	      			}
    	      		}
    	      		value = builder.toString();
    	        }else if(value!= null && !value.contains(",")){
    	        	/***
    	        	 * 判定该参数是否是纯数字，如果是纯数字则需要添加''，字母不需要
    	        	 */
    	        	if(PatternUtil.isNumeric(value)){
    	        		value = "'"+value+ "'";
    	        	}
    	        }
    		}
            
            if(key.contains("_tb")){
            	isContainTbFlag = true;
            }
            if(key.contains("_hb")){
            	isContainHbFlag = true;
            }
            result.put(key, value);
        }
        String export_flag = result.get("export");
        if(export_flag!= null && "true".equals(export_flag)){
        	isContainHbFlag = true;
        	isContainTbFlag = true;
        }
        
        if(isContainHbFlag){
        	calcHb(result);
        }
        if(isContainTbFlag){
        	calcTb(result);
        }
        //取得分页参数
        if(result.get("pnum")!= null && result.get("psize")!=null && !"".equals(result.get("pnum")) && !"".equals(result.get("psize"))){
        	int pnum = Integer.parseInt(result.get("pnum"));
        	int psize = Integer.parseInt(result.get("psize"));//每页显示几条记录
        	int offset = pnum > 0 ? (pnum - 1) * psize : 0; //第几页
        	result.put("limit",String.valueOf(psize));
        	result.put("offset",String.valueOf(offset));
        }
        logger.debug("处理后的入参:"+result);
        return result;
    }
	
	/***
	 * 处理用户的输入参数，对于如下形式的入参，type=1,2,3,4，要为每一个参数拼接''，防止sql报错
	 * 过滤一些基础参数，这些基础参数不需要加''
	 * @param value
	 * @return
	 */
	
	/***
	 * 推算环比日期
	 */
	private static void calcHb(Map<String,String> params){
		String start_time = (String) params.get("st");//起始日期
		String end_time = (String) params.get("et");//结束日期
		SimpleDateFormat sdf_line=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_nonline=new SimpleDateFormat("yyyyMMdd");
		boolean isDateLine = false;//日期是否带横线
        Date smdate = null;
        Date bdate = null;
        try {
			smdate = sdf_nonline.parse(start_time);
			bdate  = sdf_nonline.parse(end_time);
			isDateLine = false;
		} catch (ParseException e) {
			try {
				smdate = sdf_line.parse(start_time);
				bdate  = sdf_line.parse(end_time);
				isDateLine = true;
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}   
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
        int between_day = Integer.parseInt(String.valueOf(between_days)); //相差几天
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(smdate);  
        calendar.add(Calendar.DAY_OF_MONTH, -(between_day+1));  
        Date last_start_time = calendar.getTime();
        Calendar calendar1 = Calendar.getInstance();  
        calendar1.setTime(bdate);  
        calendar1.add(Calendar.DAY_OF_MONTH, -(between_day+1));  
        Date last_end_time = calendar1.getTime();
        if(isDateLine){
        	params.put("st_hb", sdf_line.format(last_start_time));
    	    params.put("et_hb", sdf_line.format(last_end_time));
        }else{
        	params.put("st_hb", sdf_nonline.format(last_start_time));
    	    params.put("et_hb", sdf_nonline.format(last_end_time));
        }
	}
	
	/***
	 * 推算同比日期
	 */
	private static void calcTb(Map<String,String> params){
		String start_time = (String) params.get("st");//起始日期
		String end_time = (String) params.get("et");//结束日期
		SimpleDateFormat sdf_line=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf_nonline=new SimpleDateFormat("yyyyMMdd");
		boolean isDateLine = false;//日期是否带横线
        Date smdate = null;
        Date bdate = null;
        try {
			smdate = sdf_nonline.parse(start_time);
			bdate  = sdf_nonline.parse(end_time);
			isDateLine = false;
		} catch (ParseException e) {
			try {
				smdate = sdf_line.parse(start_time);
				bdate  = sdf_line.parse(end_time);
				isDateLine = true;
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}   
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(smdate);
        calendar.add(Calendar.YEAR, -1);
        Date last_start_time = calendar.getTime();
        Calendar calendar1 = Calendar.getInstance(); 
        calendar1.setTime(bdate);
        calendar1.add(Calendar.YEAR, -1);  
        Date last_end_time = calendar1.getTime();
        if(isDateLine){
        	params.put("st_tb", sdf_line.format(last_start_time));
    	    params.put("et_tb", sdf_line.format(last_end_time));
        }else{
        	params.put("st_tb", sdf_nonline.format(last_start_time));
    	    params.put("et_tb", sdf_nonline.format(last_end_time));
        }
	}
}
