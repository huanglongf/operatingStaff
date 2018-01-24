package com.gomeplus.parser;

import org.dom4j.Element;

import java.util.Map;

public class MapperNode extends BaseNode{

    @Override
    public boolean parse(Map<String, String> conf, Map<String, String> globalParams, Element ele,StringBuilder sb) throws Exception {
        return true;
    }
}