<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body class="text-light bg-dark" >
<div style="display:inline-block; width:28%; padding:30px;  vertical-align: top">

	<h2>
		You're feeling: ${mood.name}? <br>
		
		<br> You Should Toss This on:
	</h2>
	<h1>
		<b>${record.artist }-<br> ${record.title }
		</b>
	</h1>
	<br>
	<br>
	<br>
Try Again? <%@ include file="common/moodspickanother.jspf"%>
	
	
	</div>
	<div style="display:inline-block; padding-top:15px; padding-left:60px; vertical-align: top">
	
	<img src="${imageUrl}" width="625px">
	</div>

	
</body>
</html>