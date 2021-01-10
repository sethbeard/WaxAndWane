<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>

<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<%@ include file="common/moods2.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Records</title>
</head>
<body class="text-light bg-dark">
<div style="padding:20px">
	<table class="table table-dark">
		<tr>
			<th width="30%">Artist</th>
			<th width="30%">Title</th>
		</tr>
		<c:forEach items="${records}" var="record">
			<tr>
				<td>${record.artist}</td>
				<td>${record.title }</td>
				<td><a
					href="ShowEditRecord?recordId=${record.id}&moodId=${moodId}"><button
							class="btn btn-primary">Edit</button></a></td>
				<td><a
					href="DeleteRecord?recordId=${record.id}&moodId=${moodId}"><button
							class="btn btn-danger">Delete</button></a></td>
			</tr>
		</c:forEach>
	</table>
	</div>
</body>
</html>