<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Main page</title>
</head>
<body class="text-light bg-dark">
<br><br>
<div width="100%" style="padding:20px">
<h4>
Hello ${user.userName},
<br><br>
I suggest you start out by adding a few moods. Click Add Moods Above To Start Adding Moods.
<br><br>
Once you have a few Moods, you'll probably want to add some records.  <br>Weirdly enough clicking add records lets you do exactly that. 
<br><br>
Well, you have records and moods now.  Time to put the two together.<br>  Click Assign Records to start assigning records to your moods. 
<br><br>
Once you've assigned some records you can click Find Record to find a random record based off of the mood you select. 
<br>
There's other stuff up there, that I hope is self explanatory. 
</h4>
<br><br>
</div>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
<%@ include file="common/footer.jspf" %>
</html>