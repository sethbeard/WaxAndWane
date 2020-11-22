<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Mood</title>
</head>
<body class="text-light bg-dark">
<div class="form-group">
<form action="editmood" method="post">
<input type="text" name="name" value="${mood.name}"/>
<input type="hidden" name="userId" value="${user.id}">
<input type="hidden" name="moodId" value="${mood.id}">
<br>
<input type="submit" class="btn btn-primary" name="editmood"/>
</form>
</div>
</body>
</html>