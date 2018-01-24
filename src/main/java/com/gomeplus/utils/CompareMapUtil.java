package com.gomeplus.utils;

import java.util.HashMap;  
import java.util.Map;  
  
public class CompareMapUtil {
      
	/***
	 * 比较两个map集合是否完全一致，包括key和value都一致才返回true
	 * @param map1
	 * @param map2
	 * @return
	 */
    public static boolean compare(Map<String,String> map1,Map<String,String> map2){  
  
        if(map1.size()!=map2.size()){    
            return false;  
        }  
          
        String tmp1;  
        String tmp2;  
        boolean b=false;  
          
        for(Map.Entry<String, String> entry:map1.entrySet()){  
            if(map2.containsKey(entry.getKey())){  
                tmp1=entry.getValue();  
                tmp2=map2.get(entry.getKey());  
                  
                if(null!=tmp1 && null!=tmp1){   //都不为null  
                    if(tmp1.equals(tmp2)){  
                        b=true;  
                        continue;  
                    }else{  
                        b=false;  
                        break;  
                    }  
                }else if(null==tmp1 && null==tmp2){  //都为null  
                    b=true;  
                    continue;  
                }else{  
                    b=false;  
                    break;  
                }  
            }else{  
                b=false;  
                break;  
            }  
        }  
        return b;  
    }  
      
      
    public static void main(String[] args) {  
          
        Map<String,String> hs1=new HashMap<String,String>();  
        Map<String,String> hs2=new HashMap<String,String>();  
          
        hs1.put("001key","001value");  
        hs1.put("002key","002value");  
        hs1.put("003key","003value");  
        hs1.put("004key","004value");  
        hs1.put("005key","005value");  
        hs1.put("006key","006value");  
        hs1.put("007key","007value");  
          
        hs2.put("001key","001value");  
        hs2.put("002key","002value");  
        hs2.put("006key","006value");  
        hs2.put("005key","005value");  
        hs2.put("007key","007value");  
        hs2.put("004key","004value");  
        hs2.put("003key","003value");  
        
        long st2=System.currentTimeMillis();  
        boolean b2=compare(hs1, hs2);  
        long et2=System.currentTimeMillis();  
          
        System.out.println("结果是: "+b2+", 耗时是: "+(et2-st2));  
    }  
}  