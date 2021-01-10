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
<div style="background-color:#e7e7e7; padding:20px">

<div style="display:inline-block; vertical-align:middle;">
<h1 style="color: black; font-size:60px; font-weight: bolder;"><img src="https://live.staticflickr.com/65535/50631417172_897509865d_b.jpg" width="60" height="60" alt="waxandwane1"> Login:</h1>
 </div>
  <div style="display:inline-block;float:right; vertical-align:middle;">
			<a href="/showReg">Register</a>
		</div> 
		
</div>

<div class="form-group" style="color:white; padding-left:20px">
<br>
<form action="login" method="post">
<pre>
<h6 style="color:white;">Email:
<input type="text" name="email"/>
<br>
Password:
<input type="password" name="password"/>
<br></h6>
<input class="btn btn-primary" type="submit" value="Login" />
</pre>
</form>
<br>
</div>
${msg}
</body>
</html>