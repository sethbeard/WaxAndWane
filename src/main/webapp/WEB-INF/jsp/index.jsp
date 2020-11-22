<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ include file="common/header.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Homepage</title>
</head>
<body class="text-light bg-dark">

<div style="background-color:#e7e7e7;">
<br>
<h2 style="color:black;"><img src="https://live.staticflickr.com/65535/50631417172_897509865d_b.jpg" width="50" height="50" alt="waxandwane1">Welcome to Wax and Wane</h2>
<br>
</div>
<br>
<form action="showReg">
<h3>New Users,<br> Create An Account Here  </h3><input class="btn btn-primary" type="submit" value="Register Now" />
</form>
<br><br>
<form action="showLogin">
  <h3>Existing Users, <br>Log In Here  </h3>  <input type="submit" class="btn btn-primary" value="Login to your Account" />
</form>


</body>
</html>