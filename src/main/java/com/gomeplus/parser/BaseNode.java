package com.gomeplus.parser;

import org.dom4j.Element;

import com.gomeplus.cache.GlobalCache;
import com.gomeplus.jdbc.PageHelper;

import java.util.Map;

/**
 * 抽象sql标签
 */
public abstract class BaseNode {
	
	public abstract boolean parse(Map<String, String> conf,Map<String, String> globalParams, Element ele, StringBuilder sb) throws Exception;

	/***
	 * 递归结束后回调
	 * @param conf
	 * @param globalParams
	 * @param ele
	 * @param sb  sql上下文
	 * @throws Exception
	 */
	public void after(Map<String, String> conf,Map<String, String> globalParams, Element ele, StringBuilder sb) throws Exception {
		/**
		 * 根据conf配置，进行sql装配
		 */
		String sql_id= conf.get("sql_id");
		String isPaging = conf.get("page");
		String total = conf.get("total");
		String tb = conf.get("tb");
		String hb = conf.get("hb");
		String export = conf.get("export");
		String front_tb_flag = globalParams.get("tb");//前端是否传了包含同比查询的标记
		String front_hb_flag = globalParams.get("hb");//前端是否传了包含环比查询的标记
		String front_export_flag = globalParams.get("export");//前端是否传了导出标记
		
		String finalSql = null;
		/***
		 * 如果是导出操作
		 */
		if(export != null && "true".equals(export) && front_export_flag != null && "true".equals(front_export_flag)){
			/***
			 * 构建导出的明细数据sql
			 */
			finalSql = sb.toString();
			GlobalCache.SqlContainer.put(sql_id,finalSql);
			/***
			 * 构建导出的同比数据sql
			 */
			if(tb != null && "true".equals(tb)){
				finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"tb",false);
				GlobalCache.SqlContainer.put(sql_id+"_tb",finalSql);//存放同比sql	
			}	
			/***
			 * 构建导出的环比数据sql
			 */
			if(hb != null && "true".equals(hb)){
				finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"hb",false);
				GlobalCache.SqlContainer.put(sql_id+"_hb",finalSql);//存放环比sql
			}			
		}else if(export == null && front_export_flag != null && "true".equals(front_export_flag)){
			//donothing 
			//这个分支的意义在于，将某xml文件中，没有标记export的sql都过滤掉，不需要在执行了
//			System.out.println("过滤一个sql");
		}else{
			/***
			 * 进入这个分支，是常规的非导出sql
			 */
			if("true".equals(isPaging)){ //构建分页sql
				finalSql = PageHelper.buildPageSql(sb.toString());
			}else if("true".equals(total)){
				finalSql = PageHelper.buildTotalNumSql(sb.toString());
			}else{
				finalSql = sb.toString();
			}
			GlobalCache.SqlContainer.put(sql_id,finalSql);//存放解析sql结果到全局sql上下文容器中
			
			/***
			 * 只有在<select>中配置了tb标记且前端也传入了tb标记，才构建同比sql，环比同理
			 */
			if(tb != null && "true".equals(tb) && front_tb_flag != null && "true".equals(front_tb_flag)){
				if("true".equals(isPaging)){
					finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"tb",true);
				}else{
					finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"tb",false);
				}
				GlobalCache.SqlContainer.put(sql_id+"_tb",finalSql);//存放同比sql	
			}
			
			if(hb != null && "true".equals(hb) && front_hb_flag != null && "true".equals(front_hb_flag)){
				if("true".equals(isPaging)){
					finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"hb",true);
				}else{
					finalSql = PageHelper.buildTbOrHbSql(sb.toString(),"hb",false);
				}
				GlobalCache.SqlContainer.put(sql_id+"_hb",finalSql);//存放环比sql
			}		
		}
	}
}