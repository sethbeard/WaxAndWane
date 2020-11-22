<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Main page</title>
</head>
<body class="text-light bg-dark">
<br><br>
<div style="display:inline-block; align:left">
<h3 align="center">Find a Record <br>
Based on 
<br>Your Mood
<form action="pickAMood" method="Post">
<input type="hidden" name="userId" value="${user.id}">
<input type="submit" class="btn btn-primary" value="Find a Record" >
</form>
</h3>
</div>
&nbsp&nbsp&nbsp
<div style="display:inline-block; align:center">
<h3 align="center">Add/Edit<br> Mood
<form action="moodCreation" method="Post">
<input type="hidden" name="user" value="${user.id}">
<input type="submit" class="btn btn-primary" value="Add Moods">
</form>
</h3>
</div>
&nbsp&nbsp&nbsp
<div style="display:inline-block">
<h3 align="center">Add<br>Record
<form action="recordCreation" method="Post">
<input type="hidden" name="userId" value="${user.id}">
<input type="submit" class="btn btn-primary" value="Add Records">
</form>
</h3>
</div>
&nbsp&nbsp&nbsp
<div style="display:inline-block">
<h3 align="center">Assign Moods<br>to Records 
<form action="showUnassigned" method="Post">
<input type="hidden" name="userId" value="${user.id}">
<input type="hidden" name="mood" value="0">
<input type="submit" class="btn btn-primary" value="Show Unassigned">
</form>
</h3>
</div>
<br>&nbsp&nbsp&nbsp
<div>
<h3>Upload a CSV from Discogs</h3>
<form method="POST" action="uploadFile" enctype="multipart/form-data">
		<input type="file" class="btn btn-primary" name="file" accept=".csv"><br /> 
		<input type="hidden" name="userId" value="${user.id}">
		<br>
		<input type="submit" class="btn btn-primary" value="Upload">
	</form>	
	<br>
	${msg}
</div>
</body>
</html>