<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ include file="common/header.jspf" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login To Your Account</title>
</head>
<body class="text-light bg-dark">
<div style="background-color:#e7e7e7;">
<br>
<h2 style="color:black"><img src="https://live.staticflickr.com/65535/50631417172_897509865d_b.jpg" width="50" height="50" alt="waxandwane1"> Login:</h2>
  <ul style="float:right;">
			<li><a href="/showReg">Register</a></li>
		</ul> 
<br>
</div>
<hr>
<div class="form-group" style="color:white;">
<br>
<form action="login" method="post">
<pre>
<h6 style="color:white;">Email:
<input type="text" name="email"/>
<br>
Password:
<input type="password" name="password"/>
<br></h6>
<input class="btn btn-primary" type="submit" name="login"/>
</pre>
</form>
<br>
</div>
${msg}
</body>
</html>