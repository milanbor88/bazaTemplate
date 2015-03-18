<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h"  uri="http://java.sun.com/jsf/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>
<f:subview id="headerMain">
<p  style="font-size:26px; text-align:center;font-weight:bold; color:#FF0000;"><%= request.getParameter("pageName") %></p>
<br>
<br>

	<%-- <h:outputText id="pageHeading" value="#{param.pageName }" /> --%>
		<h:outputLink id="adminUser" value="adminUser.jsp" styleClass="okvir">
			<f:verbatim escape="true">Korisnik</f:verbatim>
		</h:outputLink>
		<h:outputLink id="adminTable" value="adminTable.jsp" styleClass="okvir">
			<f:verbatim escape="true">Tabele</f:verbatim>
		</h:outputLink>

</f:subview>
</body>
</html>