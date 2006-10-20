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
	import="edu.common.dynamicextensions.domaininterface.EntityInterface"
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
		<c:set var="entityCollection" value="${formsIndexForm.entityCollection}"/>
 		<jsp:useBean id="entityCollection" type="java.util.Collection"/>
		
		<table width='70%' align='center' cellspacing="5" cellspacing="0" border='0'>
			<tr class="formMessage">
				<h3><bean:message key="table.heading" /><h3>
			</tr>
			<tr class="formTitle">
				<td align="center">
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

			<tr><td></td></tr>
			
			<tr>
				<td>
					<div style="border : solid 1px ; padding : 1px; width : 800px; height : 400px; overflow : auto; ">
						<table class="dataTable" width='100%' cellpadding="4" cellspacing="0" border='1' >				
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
								EntityInterface entityInterface = null;
								String name = "";
								String createdDate = "";
								
								if(entityCollection != null)
								{
									Iterator entityIterator = entityCollection.iterator();
									while(entityIterator.hasNext())
									{
										entityInterface = (EntityInterface)entityIterator.next();
										if(entityInterface.getName() != null)
										{
											name = entityInterface.getName();
										}
										if(entityInterface.getCreatedDate() != null)
										{
											createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entityInterface.getCreatedDate());
										}
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
								}							
							%>
							
						</table>
					</div>
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
