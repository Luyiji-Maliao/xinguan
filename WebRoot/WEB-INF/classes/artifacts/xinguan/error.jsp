<%@ page language="java" import="java.util.*" %>
<%@page isErrorPage="true" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" isErrorPage="true"%>

<%
    response.setStatus(HttpServletResponse.SC_OK);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
    <head>
        <title>错误页面</title>
        <script type="text/javascript" src="./js/jquery.min.js"></script>
   
    </head>
    <body>
        <table width="100%">
           
            <tr>
               
                <td>尊敬的用户：<br />系统开小差啦，请稍后重试或<a href="javascript:returnlogin()">返回登陆</a>
                    <br />如问题重复出现，请向系统管理员反馈。<br /><br />
                </td>
            </tr>
        </table>
      
    </body>
</html>