<%
	String path = request.getContextPath();
	response.sendRedirect(path + "/welcome.jsp");
	return;
%>