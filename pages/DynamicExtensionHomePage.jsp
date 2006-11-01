
<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
    import="java.util.Collection"
	import="edu.common.dynamicextensions.domaininterface.EntityInterface"
	import="java.util.Iterator"
	import="java.text.SimpleDateFormat"
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<html:form styleId='formsIndexForm' action='/ApplyFormsIndexAction'>
	<body>
		<c:set var="entityCollection" value="${formsIndexForm.entityCollection}"/>
 		<jsp:useBean id="entityCollection" type="java.util.Collection"/>
		<table width='70%' align='center' cellspacing="5" cellspacing="0" border='0'>
			<tr class="formMessage">
				<td>
					<h3><bean:message key="table.heading" /></h3>
				</td>
			</tr>
			<tr >
				<td align="left"><bean:message key="app.formpage.heading" /></td>
			</tr>
			<tr >
				<td class="formTitle" align="center">
				<logic:messagesPresent message="true">
							<html:messages message="true" id="msg">
								<bean:write name="msg" ignore="true"/>
							</html:messages>
				</logic:messagesPresent>
				</td>
			</tr>
			<tr align='left'>
				<td>
					<html:submit property="buildForm" styleClass="buttonStyle" onclick='addFormAction()'>
						<bean:message  key="buttons.build.form" />
					</html:submit>
				</td>
			</tr>

			<tr>
				<td>
					<div style="border : solid 1px ; padding : 1px; width : 800px; height : 400px; overflow : auto; ">
						<table class="dataTable" width='100%' cellpadding="4" cellspacing="0" border='1' >
							<tr class="formTitle">
								<th width='5%' align='center'>
								<!--<input type='checkbox' disabled />-->
									<bean:message key="table.serialNumber" />
								</th>
								<th width="30%" align='left'>
									<bean:message key="table.title" />
								</th>

								<th width="20%" align='left'>
									<bean:message key="table.date" />
								</th>

								<!--  <th width="15%" align='left'>
									<bean:message key="table.description" />
								</th>
								-->
								 <th width="15%" align='left'>
									<bean:message key="table.createdBy" />
								</th>

								<th width="10%" align='left'>
									<bean:message key="table.status" />
								</th>
							</tr>

							<c:forEach items="${entityCollection}" var="entityInterface" varStatus="elements">
								<jsp:useBean id="entityInterface" type="edu.common.dynamicextensions.domaininterface.EntityInterface" />
								<tr>
									<td> <c:out value='${elements.count}' />&nbsp</td>
									<td><c:out value='${entityInterface.name}' />&nbsp</td>
									<td><c:out value='${entityInterface.createdDate}'/>&nbsp</td>
									<td><c:out value='admin'/>&nbsp</td>
									<td><c:out value='In Progress' />&nbsp</td>
							</tr>
							</c:forEach>
						</table>
					</div>
				</td>
			</tr>

			<tr><td></td></tr>
			<tr><td></td></tr>

			<!--  <tr>
				<td>
					<html:button styleClass="actionButton" property="delete" disabled='true' >
						<bean:message  key="buttons.delete"/>
					</html:button>
				</td>
			</tr>
			-->
		</table>
			<html:hidden property="mode" value=""/>
	</body>
	</html:form>
</html>
