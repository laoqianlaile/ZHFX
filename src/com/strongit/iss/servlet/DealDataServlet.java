package com.strongit.iss.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strongit.iss.common.Constant;
import com.strongit.iss.common.ServletUtils;
import com.strongit.iss.common.SpringContextUtil;
import com.strongit.iss.service.impl.ReportCacheServiceImpl;

/**
 * Created by tannc on 2017/3/19 10:44.
 *  处理数据
 */
public class DealDataServlet extends HttpServlet{
    //-- header 常量定义 --//
    private static final String HEADER_ENCODING = "encoding";
    private static final String HEADER_NOCACHE = "no-cache";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final boolean DEFAULT_NOCACHE = true;

    private ReportCacheServiceImpl reportCacheService;
    private static ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
    /**
     * 禁止通过WEB方式访问本Servlet
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method=request.getParameter("m");
        // 帆软报表的缓存方法
        if(Constant.REPORT_DATAFROM_CACHE.equals(method)) {
            //访问数据UUID
            String reportCacheUUID = request.getParameter("reportCacheUUID");
            reportCacheService = (ReportCacheServiceImpl) SpringContextUtil.getBean("reportCacheService");
            // 初始化repsponse
            response=initResponseHeader(response,ServletUtils.TEXT_TYPE);
            String content = reportCacheService.getReport(reportCacheUUID);
            try {
                response.getWriter().write(content);
                response.getWriter().flush();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        // 访问
    }

    /**
     * 禁止通过WEB方式访问本Servlet
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
             String method=request.getParameter("m");
        // 帆软报表的缓存方法
         if(Constant.REPORT_DATAFROM_CACHE.equals(method)) {
                 //访问数据UUID
                 String reportCacheUUID = request.getParameter("reportCacheUUID");
                 reportCacheService = (ReportCacheServiceImpl) SpringContextUtil.getBean("reportCacheService");
                 // 初始化repsponse
                   response=initResponseHeader(response,ServletUtils.TEXT_TYPE);
                 String content = reportCacheService.getReport(reportCacheUUID);
                 try {
                     response.getWriter().write(content);
                     response.getWriter().flush();
                 } catch (IOException e) {
                     throw new RuntimeException(e.getMessage(), e);
                 }
         }
         // 访问


        }


    /**
     * 分析并设置contentType与headers.
     */
    private static HttpServletResponse initResponseHeader(HttpServletResponse response,final String contentType, final String... headers) {
        //分析headers参数
        String encoding = DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        for (String header : headers) {
            String headerName = org.apache.commons.lang3.StringUtils.substringBefore(header, ":");
            String headerValue = org.apache.commons.lang3.StringUtils.substringAfter(header, ":");

            if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                encoding = headerValue;
            } else if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                noCache = Boolean.parseBoolean(headerValue);
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }

        //设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            ServletUtils.setDisableCacheHeader(response);
        }

        return response;
    }


}
