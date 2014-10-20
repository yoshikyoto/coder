<html>
<head>
<title>FizzBuzz - YLabCoder</title>
<link rel="stylesheet" href="/YLabCoder/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="/YLabCoder/bootstrap/css/bootstrap.css">
</head>
<body>
<h2 class="page-header">FizzBuzz</h2><br />


<form method="POST" action="submit">
<h4>Question:

<%@ page import="java.io.*" %>
<% String root = "/Users/admin/junit/"; %>
<% File file = new File(root); %>

<select name="name">
<%
for(File qfile : file.listFiles()){ 
	if(!qfile.isDirectory()) continue;
	String name = qfile.getName();
	out.print("<option value=\"" + name + "\">" + name + "</option>");
}
%>
</select>
</h4>

<h4>Input Code:</h4>
<p><textarea name="submited_code" rows="20" cols="100"></textarea></p>

<p><input type="submit" value="Submit" class="btn btn-primary"></p>
</form>
</body>
</html>