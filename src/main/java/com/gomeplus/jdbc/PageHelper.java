package com.gomeplus.jdbc;

public class PageHelper{
	
    /**
     * 修改原SQL为分页SQL
     * @param sql
     * @param page
     * @return
     */
    public static String buildPageSql(String sql) {
        StringBuilder pageSql = new StringBuilder();
        pageSql.append(sql);
        pageSql.append(" limit ${limit} ");
        pageSql.append(" offset ${offset} ");
        return pageSql.toString();
    }
    
    /**
     * 构建同比、环比sql
     * @param sql
     * @param page
     * @param isPaging 是否加分页
     * @return
     */
    public static String buildTbOrHbSql(String sql,String tbOrHb,boolean isPaging) {
        StringBuilder pageSql = new StringBuilder();
        if(tbOrHb!= null && "tb".equals(tbOrHb)){
        	 sql = sql.replaceAll("\\$\\{st\\}", "\\$\\{st_tb\\}");
        	 sql = sql.replaceAll("\\$\\{et\\}", "\\$\\{et_tb\\}");
        }else if(tbOrHb!= null && "hb".equals(tbOrHb)){
        	 sql = sql.replaceAll("\\$\\{st\\}", "\\$\\{st_hb\\}");
        	 sql = sql.replaceAll("\\$\\{et\\}", "\\$\\{et_hb\\}");
        }
        pageSql.append(sql);
        if(isPaging){
        	pageSql.append(" limit ${limit} ");
        	pageSql.append(" offset ${offset} ");
        }
        return pageSql.toString();
    }

    /**
     * 获取总记录数
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    public static String buildTotalNumSql(String sql){
        // 记录总记录数
        String countSql = "select count(0) as total_num from (" + sql + ") a";
        return countSql;
    }
}
