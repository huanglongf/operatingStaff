package com.gomeplus.parser;

import org.dom4j.Element;

import java.util.Map;

public class SelectNode extends BaseNode{

    @Override
    public boolean parse(Map<String, String> conf, Map<String, String> globalParams, Element ele,StringBuilder sb) throws Exception {
    	//取得sql_id
        String sql_id = ele.attributeValue("id");
        //取得是否是分页sql标记
        String paging = ele.attributeValue("paging");
        //取得是否是计算分页总数的sql标记
        String total = ele.attributeValue("total");
        //取得是否需要添加同比sql的标记
        String tb = ele.attributeValue("tb");
        //取得是否需要添加环比sql的标记
        String hb = ele.attributeValue("hb");
        //导出标记        
        String export = ele.attributeValue("export");
        conf.put("sql_id",sql_id);
        conf.put("paging", paging);
        conf.put("total",total);
        conf.put("tb",tb);       
        conf.put("hb",hb);
        conf.put("export",export);        
    	return true;
    }

}