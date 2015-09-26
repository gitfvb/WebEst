<%@page import="javax.faces.FacesException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
	pageEncoding="ISO-8859-1" import="com.ibm.webest.gui.utils.ExceptionUtils" %>
<%
if (ExceptionUtils.exceptionMatches(exception, ".*((Target Unreachable)|(No saved view state could be found)).*")) {
	response.sendRedirect("home.wep");
}
if (exception instanceof FacesException) {
	if (exception.getCause() != null)
		exception = exception.getCause();
}
%>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="css/template.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>IBM - WebEstimation - Error</title>
</head>
<body>
<div id="errorwrapper">
<div id="errorcontainer">
<div id="content">
<h1>An error occurred!</h1>
<div><%= exception != null ? (exception.getMessage().length() > 200 ? exception.getMessage().subSequence(0, 200)+"..." : exception.getMessage()) : "" %></div>
<div>See log file for further details.</div>
</div>
<div id="errorlink"><a href="home.wep">Go back to main page</a></div>
</div>
</div>
</body>
</html>