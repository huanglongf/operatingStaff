package com.gomeplus.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;

import com.gomeplus.cache.GlobalCache;

/***
 * 类mybatis格式sql解析器
 *
 */
public class SqlParser {
	
    @SuppressWarnings("unchecked")
	public static void parserSql(String sqlXmlFileName) throws Exception{
    	GlobalCache.SqlContainer = new HashMap<String, String>();//复位sql容器
		Document document = GlobalCache.SqlDocumentContainer.get(sqlXmlFileName);
		if(document== null){
			throw new NoSuchMethodException();
		}
    	Map<String,String> conf = new HashMap<String, String>();//select节点属性
    	Element element = document.getRootElement();
    	List<Element> elements = element.elements("select");//取得所有select标签
    	
    	StringBuilder sb = null;
    	for (Element e : elements) {
    		//递归解析
    		sb = new StringBuilder();
    		parserElement(e, conf, GlobalCache.Params, sb);
		}
    }

    /**
     * 使用递归解析动态sql
     * @param ele1 待解析的xml标签
     * @param globalParams
     * @param sb
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	private static void parserElement(Element ele1, Map<String, String> conf,Map<String, String> globalParams, StringBuilder sb) throws Exception {
        // 解析一个节点，比如解析到了一个if节点，假如test判断为true这里就返回true
        TempVal val = parserOneElement(conf, globalParams, ele1, sb);
        //得到解析的这个节点的抽象节点对象
        BaseNode node = val.getNode();
        //判断是否还需要解析节点里的内容，例如if节点test结果为true
        boolean flag = val.isContinue();
        // 得到该节点下的所有子节点的集合，包含普通文本
        List<Node> nodes = ele1.content();
        if (flag && !nodes.isEmpty()) {
            //循环所有子节点
            for (int i = 0; i < nodes.size();) {
                Node n = nodes.get(i);
                //如果节点是普通文本
                if (n instanceof Text) {
                    String text = ((Text) n).getStringValue();
                    if (StringUtils.isNotEmpty(text.trim())) {
//                        sb.append(handlerText(text,conf,globalParams));
                        sb.append(text);
                    }
                    i++;
                } else if (n instanceof Element) {
                    Element e1 = (Element) n;
                    // 递归解析xml子元素
                    parserElement(e1, conf, globalParams, sb);
                    i++;
                }
            }
            //递归后回调
            if(conf.size()>0){
            	node.after(conf, globalParams, ele1, sb);
            }
            sb = new StringBuilder();
        }
    }

    /**
     * 处理文本替换掉#{item}这种参数
     * @param str
     * @param params
     * @return
     * @throws Exception
     */
//    private static String handlerText(String str, Map<String, String> conf,Map<String, String> globalParams) throws Exception {
//        //匹配${a}这种参数
//        String reg2 = "(\\$\\{)(\\w+)(\\})";
//        Pattern p2 = Pattern.compile(reg2);
//        Matcher m2 = p2.matcher(str);
//        while(m2.find()) {
//            String tmpKey = m2.group(2);
//            Object value = globalParams.get(tmpKey);
//            if(value != null) {
//                str = str.replace(m2.group(0), getValue(value));
//            }
//        }
//        return str;
//    }

//    private static String getValue(Object value) {
//        String result = "";
//        if(value instanceof Date) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            result = sdf.format((Date)value);
//        } else {
//            result = String.valueOf(value);
//        }
//        return result;
//    }
    
    /**
     *  解析一个xml元素
     * @param conf
     * @param globalParams
     * @param ele
     * @param sb
     * @return
     * @throws Exception
     */
    private static TempVal parserOneElement(Map<String, String> conf,Map<String, String> globalParams, Element ele, StringBuilder sb) throws Exception {
        //获取xml标签名
        String eleName = ele.getName();
        //解析一个节点后是否继续，如遇到if这种节点，就需要判断test里是否为空
        boolean isContinue = false;
        //声明一个抽象节点
        BaseNode node = null;
        if (StringUtils.isNotEmpty(eleName)) {
            //使用节点工厂根据节点名得到一个节点对象比如是if节点还是foreach节点
            node = NodeFactory.create(eleName);
            //解析一下这个节点，返回是否还需要解析节点里的内容
            isContinue = node.parse(conf, globalParams, ele, sb);
        }
        return new TempVal(isContinue, ele, node);
    }

    /**
     * xml解析结果
     */
    final static class TempVal {

        private boolean isContinue;

        private Element ele;

        private BaseNode node;

        public TempVal(boolean isContinue, Element ele, BaseNode node) {
            this.isContinue = isContinue;
            this.ele = ele;
            this.node = node;
        }

        public boolean isContinue() {
            return isContinue;
        }

        public void setContinue(boolean isContinue) {
            this.isContinue = isContinue;
        }

        public Element getEle() {
            return ele;
        }

        public void setEle(Element ele) {
            this.ele = ele;
        }

        public BaseNode getNode() {
            return node;
        }

        public void setNode(BaseNode node) {
            this.node = node;
        }
    }
    
    public static void main(String[] args) throws Exception {
//      GlobalCache.params.put("st","20170801");
//      GlobalCache.params.put("et","20170803");
//      GlobalCache.params.put("orderby", "store_name");
//      GlobalCache.params.put("ordertype", "desc");
//      GlobalCache.params.put("limit", "40");
//      GlobalCache.params.put("offset", "1");
//      SqlParser parser = new SqlParser();
//      SAXReader reader = new SAXReader(false);
//      File f = new File("D:\\workspace\\operatingStaff\\target\\classes\\com\\gomeplus\\mapping\\self_category_sale.xml");
//      Document document = reader.read(f);
//      parser.parserSql();
  }
}
