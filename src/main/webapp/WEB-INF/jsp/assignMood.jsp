<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<%@ include file="common/moods.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Mood Assignement</title>
</head>
<body class="text-light bg-dark">

	<table class="table table-dark">
		<thead class="thead-dark">
			<tr>
				<th scope="col">Artists</th>
				<th scope="col">Title</th>
				<th scope="col">Moods</th>
			</tr>
		</thead>

		<c:forEach items="${records}" var="record">
			<tr>
				<form action="assigningValues" method="post">
					<th scope="row" width="30%">${record.artist}</th>
					<td width="30%">${record.title }</td>

					<c:forEach items="${moods}" var="mood">
						<c:if test="${mood.id != moodId }">
						<td>${mood.name} &nbsp<input type="checkbox" name="moods"
							value="${mood.id}" /></td>
						</c:if>

					</c:forEach>
					<td><input type="hidden" name="records" value="${record.id }" />
						<input type="hidden" name="userId" value="${user.id}" />
						<input type="hidden" name="moodId" value="${moodId}" />  <input
						type="submit" class="btn btn-primary" name="assigningValues"
						value="Save" />
						</td>
							</form>
						<c:if test="${moodId != 0}">
						<td><a
					href="UntagRecord?recordId=${record.id}&userId=${user.id}&moodId=${moodId}"><button
							class="btn btn-danger">Remove Tag</button></a></td>
							</c:if>
			
		
			</tr>
		</c:forEach>
	</table>

</body>
</html>