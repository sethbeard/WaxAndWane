<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Record</title>
</head>
<body class="text-light bg-dark" style="margin:20px">
	<br>
	<br>
	<div class="form-group">
		<form action="addRecord" method="post">

			Artist: <br> <input type="text" name="artist" /> <br> <br>
			Title: <br> <input type="text" name="title" /> <br> <br>
			<hr>
			<table class="table table-dark">
				<tr>
				<td>
					<c:forEach items="${moods}" var="mood" varStatus="loop">
						<c:if test="${(loop.index +1) % 5 == 0}"> 
                        </td>
                            </tr>
                        <tr>
                            <td>
						</c:if>
						${mood.name}&nbsp<input type="checkbox" name="moods"
							value="${mood.id}">		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp

					</c:forEach>
					</td>
				</tr>
			</table>
			<br>
			<input type="submit" class="btn btn-primary" name="addRecord"
				value="Add Record" />

		</form>
	</div>
	<br> ${msg}
	
</body>
</html>