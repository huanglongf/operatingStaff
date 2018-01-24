package com.gomeplus.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gomeplus.cache.GlobalCache;

@Component
public class ImpalaJdbc {
	
	private static Logger logger = LoggerFactory.getLogger(ImpalaJdbc.class); 
	
    public static String driver;
    
//  @Value("${jdbc.url}")
    public static String impala_url;
    
//    @Value("${impala_user}")
//    private static String impala_user;
//    
//    @Value("${impala_pwd}")
//    private static String impala_pwd;
    
    static{
    	try {
			Class.forName("com.cloudera.impala.jdbc4.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	public static JSONArray query(String sql_id,String sql,Map<String, String> params){
		JSONArray array = new JSONArray();
		
		try (Connection con = DriverManager.getConnection(impala_url);
				Statement st = con.createStatement();){
			for (String key : params.keySet()){
				sql = sql.replaceAll("\\$\\{" + key + "\\}", params.get(key));
			}
			logger.trace("url:"+GlobalCache.ReqUrl+"\t"+sql_id+":"+sql);
			try(ResultSet rs = st.executeQuery(sql);){
				ResultSetMetaData meta = rs.getMetaData();
				while(rs.next()){
					JSONObject row = new JSONObject(false);
					for(int i = 1; i <= meta.getColumnCount(); i++){
						row.put(meta.getColumnLabel(i).toLowerCase(), rs.getString(i));
					}
					array.add(row);
				}
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		} 
		return array;
	}
}
