package com.lims.core.utils.web;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AjaxUtil {
    private static final Log log = LogFactory.getLog(AjaxUtil.class);
    public static boolean sendAjaxData(HttpServletResponse response,String data){
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out = response.getWriter();
            out.print(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }

        return true;

    }

    /**
     * json格式内容响应
      * @param response
     * @param content
     */
    public static void responesJsonContent (HttpServletResponse response,String jsonObject) {
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(jsonObject);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }
    }
}