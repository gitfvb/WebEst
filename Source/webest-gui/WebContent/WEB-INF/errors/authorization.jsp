<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"
	pageEncoding="ISO-8859-1" import="com.ibm.webest.gui.utils.ExceptionUtils" %>
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
<h1>Access denied!</h1>
<div>URL ${pageContext.errorData.requestURI} is protected.<br />You do not have sufficient permission to view this page.</div>
</div>
<div><a href="<%= request.getContextPath()%>/home.wep" id="errorlink">Go back to main page</a></div>
</div>
</div>
</body>
</html>