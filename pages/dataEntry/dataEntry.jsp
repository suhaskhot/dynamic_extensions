<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>

<%-- Imports --%>
<%@ page language="java" contentType="text/html" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>


<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
<link href="<%=request.getContextPath()%>/css/calanderComponent.css" type=text/css rel=stylesheet />

<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
<script>var imgsrc="images/";</script>
<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/overlib_mini.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/calender.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>

<c:set var="containerInterface" value="${dataEntryForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<c:set var="showFormPreview" value="${dataEntryForm.showFormPreview}"/>
<jsp:useBean id="showFormPreview" type="java.lang.String"/>

<c:set var="errorList" value="${dataEntryForm.errorList}"/>
<jsp:useBean id="errorList" type="java.util.List"/>

<c:set var="recordIdentifier" value="${dataEntryForm.recordIdentifier}" />
<jsp:useBean id="recordIdentifier" type="java.lang.String"/>

<c:set var="mode" value="${dataEntryForm.mode}" />
<jsp:useBean id="mode" type="java.lang.String"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId="dataEntryForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" method="post">
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		
			<c:choose>
				<c:when test='${showFormPreview == "true"}'>
			    	<table valign="top" style="border-right:1px" border=1 align='center' width='90%' height="90%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
				</c:when>				
				<c:otherwise>
					<table valign="top" align='center' width='90%' height="90%" border='0' cellspacing="0" cellpadding="0"  >
				</c:otherwise>
			</c:choose>
			
			
	 				<!-- Main Page heading -->
					<tr>
						<td class="formFieldNoBorders">
							<c:choose>
								<c:when test='${showFormPreview  == "true"}'> 
									<bean:message key="app.title.MainPageTitle" />
								</c:when>
							</c:choose>
						</td>
					</tr>
		 
		  			<tr valign="top">
						<td>
							<table valign="top" summary="" align='center' width='100%' cellspacing="0" cellpadding="3">
								<c:choose>
								    <c:when test='${showFormPreview  == "true"}'> 
										<tr valign="top">
										   	<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="alert('This page is still under construction and will be available in the next release');">
												<bean:message key="app.title.DefineGroupTabTitle" />
										   	</td>
										  	<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showFormDefinitionPage()">
												<bean:message key="app.title.DefineFormTabTitle" />
										   	</td>
										   	<td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="backToControlForm()">
												<bean:message key="app.title.BuildFormTabTitle" />
										   	</td>
										  	<td height="20" class="tabMenuItemSelected"  >
												<bean:message key="app.title.PreviewTabTitle" />
										   	</td>
										   	<td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
										</tr>
									</c:when>	
								</c:choose>
								
								<tr valign="top">
									<td colspan="7">
										<table align='center' width='80%'>
											<tr>
												<td align="center" class="formTitle">
																								
													<%
														if(errorList.size() != 0)
														{
													%>
															<c:forEach items="${errorList}" var="error">
															<jsp:useBean id="error" type="java.lang.String" />
																<c:out value="${error}"/><br />
															</c:forEach>
													<%
														}
													%>
													<logic:messagesPresent message="true">
														<ul>
															<html:messages id="msg" message="true"> 
																<li><bean:write name="msg"/></li>
														 	</html:messages>
														</ul>
													</logic:messagesPresent>
												</td>
											</tr>
											<tr><td>&nbsp;</td></tr>
											<tr>
												<td>
													<dynamicExtensions:dynamicUIGenerator containerInterface="<%=containerInterface%>" />
												</td>
											</tr>
										</table>
	           						</td>
	           					</tr>
								<tr>
									<td valign="top" colspan="7">
										<table cellpadding="4" cellspacing="5" border="0"  align='center'>
											<tr height="5"></tr>
											<tr>
												<td align='left'>
													<c:if test='${showFormPreview  == "true"}'>
														<html:button styleId = 'backToPrevious' property="backToPrevious"  styleClass="actionButton" onclick="backToControlForm()">
															<bean:message  key="buttons.backToPrevious" />
														</html:button>
													</c:if>
												</td>
							
												<td align='right'>						
													<c:choose>
							  	    					<c:when test='${showFormPreview  == "true" || mode  == "view"}'>
															<html:submit styleClass="actionButton"  onclick="addDynamicData()" disabled="true">
																<bean:message  key="buttons.submit" />
															</html:submit>
														</c:when>
							 							<c:otherwise>
							 								<html:hidden styleId='isEdit' property="isEdit" value=""/>
							 								<% 
							 									String target = "addDynamicData(" + recordIdentifier + ")";
							 								%>
							 								<html:submit styleClass="actionButton" onclick="showParentContainerInsertDataPage()">
															 	<bean:message  key="buttons.back" />
															</html:submit>
															
															
													 		<html:submit styleClass="actionButton">
															 	<bean:message  key="buttons.submit" />
															</html:submit>
						 								</c:otherwise>
				     								</c:choose>	
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>			
			<input type="hidden" name="recordIdentifier" value="<%=recordIdentifier%>"/> 
			<html:hidden styleId='entitySaved' property="entitySaved" />
					
			
			<input type="hidden" id = "childContainerId" name="childContainerId" value=""/> 
			<input type="hidden" id = "childRowId" name="childRowId" value=""/> 
			<input type="hidden" id = "dataEntryOperation" name="dataEntryOperation" value=""/> 
		</html:form>		
	</body>
</html>