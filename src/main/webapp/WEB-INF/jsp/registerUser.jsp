<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	  <%@ include file="common/header.jspf" %>
	 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Registration</title>
<script>
	// Function to check Whether both passwords 
	// are same or not. 
	function checkPassword(form) {
		password = form.password.value;
		confirmPassword = form.confirmPassword.value;
		username = form.userName.value;
		email = form.email.value;

		// If password not entered 
		if (password == '') {
			alert("Please enter Password");
			return false;
		}
		// If confirm password not entered 
		else if (confirmPassword == '') {
			alert("Please enter confirm password");
			return false;
		}

		// If Not same return False.     
		else if (password != confirmPassword) {
			alert("\nPassword did not match: Please try again...")
			return false;

		// if email or username is empty return false.
		} else if (email == "" || username == "") {
			alert("\nPlease enter a valid email and username")
			return false;
		}
		//Check for valid email
		else if (!"/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/".test(myForm.email.value)){
			alert("\n Please Enter a Valid Email")
			return false;
			}

		// If same return True. 
		else {
			return true;
		}
	}
</script>
</head>
<body class="text-light bg-dark">
<div style="background-color:#e7e7e7;">
<br>
	<h2 style="color:black;"><img src="https://live.staticflickr.com/65535/50631417172_897509865d_b.jpg" width="50" height="50" alt="waxandwane1"> &nbsp Sign Up For A New Account:</h2>
	  <ul style="float:right;">
			<li><a href="/showLogin">Login</a></li>
		</ul> 
	<br>
	</div>
	<pre>
	<div class="form-group" style="color:white;">
<form action="registerUser" method="post"
			onSubmit="return checkPassword(this)">
User Name: 
<input type="text" name="userName" /><br>
Email: 
<input type="text" name="email" /><br>
Password: 
<input type="password" name="password" /><br>
Confirm Password:
<input type="password" name="confirmPassword" /><br><br>
<input type="submit" class="btn btn-primary" value="Register" />
</form>
</div>
${msg}
</pre>
</body>
</html>