<%-- Jsp Summary                                                                                 	--%>
<%-- This shows forms' list and also allows user to add a new form or Deletion of a form.           --%>
<%-- @author : chetan_patil--%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@	
	page language="java" contentType="text/html" 
    import="java.util.List"
    import="java.util.Collection"
	import="edu.common.dynamicextensions.domain.Entity"
	import="java.util.Iterator"
	import="java.text.SimpleDateFormat"
  
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>		
	</head>
	

	<html:form styleId='formsIndexForm' action='/ApplyFormsIndexAction'>
	<body>
		
		<c:set var="entityList" value="${formsIndexForm.entityList}"/>
 		<jsp:useBean id="entityList" type="java.util.Collection"/>
							
		<table width='70%' align='center' cellspacing="5" cellspacing="0" border='0'>
			<tr class="formMessage">
				<h3><bean:message key="table.heading" /><h3>
			</tr>

			<tr align='left'>
				<td>
					<html:submit property="buildForm" onclick='addFormAction()'>
						<bean:message  key="buttons.build.form" />
					</html:submit>
				</td>
			</tr>

			<tr><td></td></tr>
			
			<tr>
				<td>
					<table class="dataTable" width='100%' cellpadding="5" cellspacing="0" border='1' >				
						<tr class="formTitle">
							<th width='5%' align='center'>
								<input type='checkbox' disabled />
							</th>
							<th width="30%" align='left'>
								<bean:message key="table.title" />
							</th>
							
							<th width="20%" align='left'>
								<bean:message key="table.date" />
							</th>
							
							<th width="15%" align='left'>
								<bean:message key="table.createdBy" />
							</th>
							
							<th width="10%" align='left'>
								<bean:message key="table.status" />
							</th>		
						</tr>
					
						<%
							int i = 0;
							Iterator entityIterator = entityList.iterator();
							Entity entity = null;
							String name = null;
							String createdDate = null;

							while(entityIterator.hasNext())
							{
								entity = (Entity)entityIterator.next();
								name = entity.getName();
								createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getCreatedDate());
						%>
						
						<tr class="formRequiredNotice">
							<td align='center'>
								<input type='checkbox' />
							</td>
													
							<td>
								<%= name%>
							</td>

							<td>
								<%= createdDate%>
							</td>

							<td> Robert Lloyd </td>
							
							<td> In Progress </td>						
						</tr>
						
						<%
								i++;
							}
						%>
						
					</table>
				</td>
			</tr>

			<tr><td></td></tr>
			<tr><td></td></tr>

			<tr>
				<td>
					<html:button styleClass="actionButton" property="delete" disabled='true' >
						<bean:message  key="buttons.delete"/>
					</html:button>
				</td>
			</tr>
		</table>
			<html:hidden property="mode" value=""/>
	</body>
	</html:form>
</html>
