<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page isELIgnored="false"%>
<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<body class="text-light bg-dark">
<div style="padding:20px">
<table>
<tr style="padding-right:5px;padding-top: 10px">

	<c:forEach items="${moods}" var="mood" varStatus="loop">
	
		<c:if test="${(loop.index -1) % 5 == 0 && loop.index != 1}"> 
                        
                            </tr>
                           
                           
                        <tr style="padding-right:5px;padding-top: 10px">
                           
						</c:if>
				 <td style="padding-right:5px;padding-top: 10px;" align="center">		
		<form action="moodPicked" method="post">
			  <input
				type="hidden" name="moodId" value="${mood.id}"> <input
				type="submit" class="btn btn-primary" name="mood"
				value="${mood.name}">&nbsp&nbsp
		</form>
		</td>

	</c:forEach>
	
	</tr>
	</table>
	</div>
</html>