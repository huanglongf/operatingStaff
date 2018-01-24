package com.gomeplus.utils;

/***
 * 计时器
 */
public class SystemTimerUtil {
	
	private static long startTime = 0L;

    private static long endTime = 0L;
    
    public static void StartTimer(){
    	startTime = System.currentTimeMillis();
    }
    
    public static void EndTimer(){
    	endTime = System.currentTimeMillis();
    }
    
    public static String PrintTimer(){    	
    	String request_time =(endTime - startTime) + "ms";
    	return request_time;
    }
    
	
	
}
