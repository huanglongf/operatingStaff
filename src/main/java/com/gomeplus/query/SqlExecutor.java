package com.gomeplus.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gomeplus.jdbc.ImpalaJdbc;
import com.gomeplus.utils.ExecutorServiceUtil;

/***
 * 多线程sql执行器
 * @author mijia
 *
 */
public class SqlExecutor {

	/***
	 * sql执行
	 * @param sqlcontainer sql_id和sql的map集合
	 * @param params sql的参数
	 */
	public static JSONObject executesql(Map<String,String> sqlcontainer,Map<String,String> params) throws InterruptedException, ExecutionException{
		ExecutorService executorServicePool = ExecutorServiceUtil.getExecutorServicePool();		
		CountDownLatch latch = new CountDownLatch(sqlcontainer.size());
		List<Future<JSONObject>> future_list = new ArrayList<Future<JSONObject>>();
		
		for (String sql_id : sqlcontainer.keySet()) {				
			Future<JSONObject> threadResult = executorServicePool.submit(new QueryImpalaThread(sql_id,sqlcontainer.get(sql_id),params,latch));				
			future_list.add(threadResult);
		}
		
		/** 等待所有子线程完成 **/
		latch.await();
		
		JSONObject combine_data_object = new JSONObject();

		if(combine_data_object!= null && combine_data_object.size() > 0){
			//System.out.println("先清空上一次的combine_data_object数据");
			combine_data_object.clear();
		}
		
		for (Future<JSONObject> future : future_list) {
			JSONObject singleSqlResultObject = future.get();//每一条sql的执行结果json，包含sql_id
			for(String sql_id : singleSqlResultObject.keySet()){
				combine_data_object.put(sql_id, singleSqlResultObject.get(sql_id));
			}
		}
		return combine_data_object;
	}
}

/***
 * 
 * 带回调结果的线程，需要实现Callable接口
 *
 */
class QueryImpalaThread implements Callable<JSONObject>{
	
	private String sql_id;//当前执行的sql_id
	private String sqlcontext;//当前还未进行变量替换的原始sql，包含${xxx}占位符
	private Map<String, String> params;//入参	
	private CountDownLatch latch;
	
	public QueryImpalaThread(String sql_id, String sqlcontext,Map<String, String> params, CountDownLatch latch) {
		this.sql_id = sql_id;
		this.sqlcontext = sqlcontext;
		this.params = params;
		this.latch = latch;
	}

	@Override
	public JSONObject call() throws Exception {
		JSONArray array = ImpalaJdbc.query(this.sql_id,this.sqlcontext, this.params);
		JSONObject result = new JSONObject();
        result.put(this.sql_id, array);
		/** 计数器-1 **/
		latch.countDown();
		return result;
	}
}
