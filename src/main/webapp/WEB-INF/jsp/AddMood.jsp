<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@page isELIgnored="false" %>
<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add A Mood</title>
</head>
<body class="text-light bg-dark">
<h2>Add A Mood</h2>
<hr>
<form action="addMood" method="post">

<pre>
<h4 style="color:white;">Mood Name:</h4>
<input type="text" name="name"/>
<input type="hidden" name="userId" value="${user.id}">
<input type="submit" class="btn btn-primary" name="addMood" value="Add Mood"/>
</pre>
</form>
${msg}
<br>
<hr>
<br>
<table class="table table-striped">
<tr scope=>
<!--  Shows all currently created moods -->
<th>Moods Created for ${user.userName}</th>
<th></th>
<th></th>
</tr>
<c:forEach items="${moods}" var="mood">
<tr>
<td scope="row">${mood.name}</td>
<td> <a href="ShowEditMood?moodId=${mood.id}&userId=${user.id}"><button class="btn btn-primary" >Edit</button></a></td>
<td> <a href="DeleteMood?moodId=${mood.id}&userId=${user.id}"><button  class="btn btn-danger" >Delete</button></a></td>
</tr>
</c:forEach>
</table>
<a href="mainpage?userId=${user.id}"><button class="btn btn-primary" >Return Home</button></a>
</body>
</html>