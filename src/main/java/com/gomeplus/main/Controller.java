package com.gomeplus.main;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.gomeplus.GlobalEnum;
import com.gomeplus.cache.GlobalCache;
import com.gomeplus.parser.SqlParser;
import com.gomeplus.query.SqlExecutor;
import com.gomeplus.utils.CompareMapUtil;
import com.gomeplus.utils.OutPutJsonUtil;

@RestController
public class Controller {

    @ResponseBody
    @RequestMapping("/**")
    public void doRend(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Content-type", "text/json;charset=UTF-8");
        GlobalEnum cur_enum = null;
        try {
    		/***
        	 * sql解析
        	 */
			SqlParser.parserSql(GlobalCache.ReqUrl+".xml");

        	if(!CompareMapUtil.compare(GlobalCache.ReqSqlContainer,GlobalCache.SqlContainer)
        			||
        	   !CompareMapUtil.compare(GlobalCache.ReqParams,GlobalCache.Params)){
        		/***
        		 * 只要本次请求参数、sql、查询接口url与上一次请求产生差异，立即触发impala查询，否则直接取缓存
        		 */
        		GlobalCache.QueryResult = new JSONObject();
        		GlobalCache.QueryResult = SqlExecutor.executesql(GlobalCache.SqlContainer, GlobalCache.Params);
        		GlobalCache.ReqSqlContainer = GlobalCache.SqlContainer;
        		GlobalCache.ReqParams = GlobalCache.Params;

        	}

		}catch(NoSuchMethodException me){
			cur_enum = GlobalEnum.unsupportinterface;
			me.printStackTrace();
		}
        catch (Exception e) {
        	cur_enum = GlobalEnum.systemerror;
        	e.printStackTrace();
		}
        if(cur_enum!=null){
        	try {
				response.getWriter().write(OutPutJsonUtil.out(cur_enum).toJSONString());
				GlobalCache.ControllerStatus = true;//出现异常，拦截器postHandler停止处理
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
    }
}

