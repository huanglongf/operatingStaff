package com.gomeplus.parser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  节点工厂
 */
public class NodeFactory {
    
    private static Map<String,BaseNode> nodeMap = new ConcurrentHashMap<String,BaseNode>();

    static {
        nodeMap.put("if", new IfNode());
        nodeMap.put("select", new SelectNode());
        nodeMap.put("mapper", new MapperNode());
    }
    
    public static BaseNode create(String nodeName) {        
        return nodeMap.get(nodeName);        
    }    
}