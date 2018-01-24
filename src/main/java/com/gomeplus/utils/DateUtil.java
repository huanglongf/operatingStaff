package com.gomeplus.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String dateToMMdd(){
		Date date = new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	public static String dateToMMddHHmmss(){
		Date date = new Date();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static void main(String[] args) {
		String dateToMMdd = DateUtil.dateToMMdd();
		String dateToMMddHHmmss = DateUtil.dateToMMddHHmmss();
		System.out.println(dateToMMddHHmmss);
		System.out.println(dateToMMdd);
	}
	
}
