<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<%@ include file="common/moodsm.jspf"%>
<%@ include file="common/moods.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Mood Assignment</title>
</head>
<body class="text-light bg-dark" >
<script>
function toggle(source) {
  checkboxes = document.getElementsByName('checkedRecord');
  for(var i=0, n=checkboxes.length;i<n;i++) {
	    checkboxes[i].checked = source.checked;
	  }
}
</script>

	<table class="table table-dark" style="margin:20px">
		<thead class="thead-dark">
			<tr>
				<th scope="col">Artists</th>
				<th scope="col">Title</th>
				<th scope="col" >Select All <input type="checkbox" onClick="toggle(this)" /></th>
			</tr>
		</thead>
<form action="assigningValues" method="post">
		<c:forEach items="${records}" var="record">
			<tr>
				
					<th scope="row" width="30%">${record.artist}</th>
					<td width="30%">${record.title}</td>

					
					<td style="padding-left:70px"><input type="checkbox" name="checkedRecord" 
							value="${record.id}"  /></td>
							
					
						
						
							
						

			</tr>
		</c:forEach>
		<input type="hidden" name="moodId" value="${moodId}" />  
		<input
					style="float:right;position:fixed; right:20px; top:350px"	type="submit" class="btn btn-primary" name="assigningValues"
						value="Save" />
						</form>
	</table>

</body>
</html>