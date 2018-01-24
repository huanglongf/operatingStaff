package com.gomeplus.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {

	private static ExecutorService exec;
	
	static{
		 exec = Executors.newCachedThreadPool();		 
	}
	
	public static ExecutorService getExecutorServicePool(){
		if(exec!= null){
//			System.out.println("使用已存在的线程池进行操作");
			return exec;
		}else{
//			System.out.println("新创建一个线程池进行操作");
			exec = Executors.newCachedThreadPool();
			return exec;
		}
	}
}
