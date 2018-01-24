package com.gomeplus.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.gomeplus.GlobalEnum;
import com.gomeplus.cache.GlobalCache;
import com.gomeplus.handler.BackModule;
import com.gomeplus.log.LogRecorder;
import com.gomeplus.utils.DateUtil;
import com.gomeplus.utils.IpUtil;
import com.gomeplus.utils.OutPutJsonUtil;
import com.gomeplus.utils.ParamUtil;
import com.gomeplus.utils.SystemTimerUtil;

public class GlobalInterceptor implements HandlerInterceptor {
	
	private static Logger logger=LoggerFactory.getLogger(GlobalInterceptor.class);
	
	/***
	 * controller请求前处理
	 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      GlobalCache.LogEntity.clear();
      GlobalCache.SqlEntity.clear();
      GlobalCache.LogEntity.put("ip",IpUtil.getIpAddr(request));
      GlobalCache.LogEntity.put("visit_time",DateUtil.dateToMMddHHmmss());
      SystemTimerUtil.StartTimer();	
      String o_uri = request.getRequestURI();
      GlobalCache.LogEntity.put("req_url",o_uri);
      try{
    	  o_uri = o_uri.substring(1);
    	  o_uri = o_uri.replace("/","_");    	  
      }catch(Exception e){  
    	  OutPutJsonUtil.outForResponse(response,OutPutJsonUtil.out(GlobalEnum.interfaceformaterror).toJSONString());
    	  e.printStackTrace();
    	  return false;
      }     
      GlobalCache.ReqUrl= o_uri;
      GlobalCache.Params = ParamUtil.prepareParams(request);
      GlobalCache.LogEntity.put("params",GlobalCache.Params);
      return true;
    }

    /***
     * controller请求后处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {  
    	 try {
    		if(!GlobalCache.ControllerStatus){	    		
	    		BackModule backModule = new BackModule();
	 			Class<? extends BackModule> clazz = backModule.getClass();
	 			Method method = clazz.getDeclaredMethod(GlobalCache.ReqUrl);
	 			JSONObject finally_result = (JSONObject) method.invoke(backModule);
	 			OutPutJsonUtil.outForResponse(response, finally_result.toJSONString());
    		}
    		GlobalCache.ControllerStatus = false;
 		} catch (IllegalAccessException e) {
 			e.printStackTrace();
 		} catch (NoSuchMethodException e) {
			logger.info("BackModule类不包含"+GlobalCache.ReqUrl+"方法,请在BackModule类实现最终json处理逻辑");
			response.getWriter().write("BackModule类不包含"+GlobalCache.ReqUrl+"方法,请在BackModule类实现最终json处理逻辑");
 		} catch (SecurityException e) {
 			e.printStackTrace();
 		} catch (IllegalArgumentException e) {
 			e.printStackTrace();
 		} catch (InvocationTargetException e) {
 			e.printStackTrace();
 		} 
    	SystemTimerUtil.EndTimer();
    	GlobalCache.LogEntity.put("req_time",SystemTimerUtil.PrintTimer());    	
    	LogRecorder.log();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}