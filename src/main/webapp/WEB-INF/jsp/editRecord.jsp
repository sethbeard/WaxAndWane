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
<body class="text-light bg-dark">

<div  style="display:inline-flex;padding:40px" align="left" >
<form action="editRecord" method="post">
<label for="artist">Artist:</label><br>
<input type="text" name="artist" id="artist" value="${record.artist}"/><br><br>
Title:<br>
<input type="text" name="title" value="${record.title}" /><br>
<input type="hidden" name="moodId" value="${moodId}">
<input type="hidden" name="recordId" value="${record.id}"/>

<input type="submit" name="editRecord" class="btn btn-primary" value="Save" style="margin-top:15px"/>
</form>
</div>
<c:if test="${emptyMoods ne true }">
<div style="display:inline-block; align-content: center;padding-left: 40px">
Click A Mood To Untag:

<table align="center">
<tr style="padding-right:5px;padding-top: 10px">

	<c:forEach items="${moods}" var="mood" varStatus="loop">
	
		<c:if test="${(loop.index -1) % 4 == 0 && loop.index != 1}"> 
                        
                            </tr>
                           
                           
                        <tr style="padding-right:5px;padding-top: 10px">
                           
						</c:if>
				 <td style="padding-right:5px;padding-top: 10px;" align="center">		
		<form action="untagRecord" method="post">
			 <input
				type="hidden" name="moodId" value="${mood.id}">
				<input type="hidden" name="recordId" value="${record.id}"> <input
				type="submit" class="btn btn-primary" name="mood"
				value="${mood.name}" style="margin-top:10px">
		</form>
		</td>

	</c:forEach>
	
	</tr>
	</table>

</div>
</c:if>
</body>
</html>