<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<%@ include file="common/moodsPick.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body class="text-light bg-dark">
	<h2>
		You're feeling: ${mood.name}? <br>
		<br>
		<hr>
		<br> You Should Toss This on:
	</h2>
	<h1>
		<b>${record.artist }<br> ${record.title }
		</b>
	</h1>
	<br>
	<br>

	<hr>
	<br>
	<h5>
		Don't like that suggestion?<br>Pick Another Record:
	</h5>
	<form>
		<input type="hidden" name="userId" value="${user.id}"> <input
			type="hidden" name="moodId" value="${mood.id}"> <input
			type="submit" class="btn btn-primary" name="mood"
			value="${mood.name}">
	</form>
	<br>
</body>
</html>