package com.gomeplus.parser;

import java.util.Map;
import ognl.Ognl;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * if节点
 */
public class IfNode extends BaseNode{

    @Override
    public boolean parse(Map<String, String> conf, Map<String, String> globalParams, Element ele,StringBuilder sb) throws Exception {
        //得到if节点的test属性
        String testStr = ele.attributeValue("test");
        boolean test = false;
        try {
            if(StringUtils.isNotEmpty(testStr)) {
                //使用ognl判断true或者false
                test = (Boolean) Ognl.getValue(testStr,globalParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("判断操作参数"+testStr+"不合法");
        }

        if(ele.content() != null && ele.content().size()==0) {
            test = true;
        }

        return test;
    }

}
