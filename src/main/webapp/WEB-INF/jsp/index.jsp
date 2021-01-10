<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="common/header.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Homepage</title>
</head>
<body class="text-light bg-dark" >
	<div style="background-color: #e7e7e7;">
		<br><div style=" margin-left: 20px">
		<h1 style="color: black; font-size: 60px; font-weight: bolder;">
			<img
				src="https://live.staticflickr.com/65535/50631417172_897509865d_b.jpg"
				width="60" height="60" alt="waxandwane1">Welcome to Wax and
			Wane
		</h1>
		</div>
		<br>
	</div>
	<br>
	<div style="margin: 20px">
	<div style="display: inline-block">
		<form action="showReg">
			<h3>
				New Users,<br> Create An Account Here
			</h3>
			<input class="btn btn-primary" type="submit" value="Register Now" />
		</form>
		<br>
		<br>
		<form action="showLogin">
			<h3>
				Existing Users, <br>Log In Here
			</h3>
			<input type="submit" class="btn btn-primary"
				value="Login to your Account" />
		</form>
		<br>
		<br>
		<h3>Try out the demo account.</h3>
		<form action="login" method="post">
			<input type="hidden" name="email" value="${demoEmail}"> <input
				type="hidden" name="password" value="${demoPassword}"> <input
				class="btn btn-primary" type="submit" value="Demo Account" />
		</form>
	</div>

	<div style="display: inline-block; float: right; padding-right: 20px">
		<h3 align="center">
			Wax And Wane is a web application<br>to help you find the
			perfect record for your mood.<br>Upload your collection and tag
			your records.<br>Then find the correct record to listen to based
			on how you feel.
		</h3>
		<br>
		<br>
		<br>
		<br>
	</div>
</div>


</body>
<%@ include file="common/footer.jspf"%>
</html>