<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
	pageEncoding="ISO-8859-1" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/template.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>IBM - WebEstimation - Error</title>
</head>
<body>
<div id="errorwrapper">
<div id="errorcontainer">
<div id="content">
<h1>An internal server error occurred!</h1>
<div><%= request.getAttribute("javax.servlet.error.message") %></div>
<div>See log file for further details.</div>
</div>
<div id="errorlink"><a href="<%= request.getContextPath()%>/home.wep">Go back to main page</a></div>
</div>
</div>
</body>
</html>