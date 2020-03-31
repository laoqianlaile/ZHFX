package com.strongit.iss.common;

import java.util.Map;

/**
 * Created by tannc on 2016/11/4 13:21.
 */
public interface ICommonUtils {
    /***
     *  名字转成CODE
     * @orderBy
     * @param
     * @return
     * @author tannc
     * @Date 2016/11/4 13:24
     **/
    public  Map<String, String> codeMapFullName (Map<String, String> filters);
    
    
    /***
     *  拼接过滤条件
     * @orderBy
     * @param
     * @return
     * @author zhoupeng
     * @Date 2016/11/11 15:00
     **/
    public String searchSql(String tableStr, Map<String, String> filters);
    
    /***
     *  校验排序条件
     * @orderBy
     * @param
     * @return
     * @author zhoupeng
     * @Date 2016/11/15 11:00
     **/
    public String orderbySql(String orderbyStr, Map<String, String> filters);
    
    /**
     * 动态替换具体的层级维度
     * @orderBy 
     * @param Level
     * @param SQL
     * @return
     * @author wuwei
     * @Date 2017年3月16日下午5:32:25
     */
    public String formatItemKey(String Level,String SQL);
    
    /**
     * 根据建设地点获取查询所需条件
     * @orderBy 
     * @param buildPlace
     * @return
     * @author wuwei
     * @Date 2017年3月16日下午6:01:26
     */
    public String getSql(String buildPlace);
}
