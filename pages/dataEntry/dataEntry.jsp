<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@ page language="java" contentType="text/html" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

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

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId="dataEntryForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" method="post">
		
			<c:set var="recordIdentifier" value="${dataEntryForm.recordIdentifier}" />
			<jsp:useBean id="recordIdentifier" type="java.lang.String"/>
			
			<html:hidden styleId='entitySaved' property="entitySaved" />
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
													<!--<logic:messagesPresent message="false">
														<ul>
													 		<html:messages id="error" message="false">
													 			<li><bean:write name="error"/></li>
													 		</html:messages> 
														</ul> 
													</logic:messagesPresent>-->
													
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
												<td >
													<table summary="" cellpadding="3" cellspacing="0"  align='center' width = '100%'>
														<tr>
															<td class="formMessage" colspan="3">
																<c:out value="${containerInterface.requiredFieldIndicatior}" escapeXml="false" />&nbsp;
																<c:out value="${containerInterface.requiredFieldWarningMessage}" escapeXml="false" />
															</td>
														</tr>
														<tr>
															<td class='formTitle' colspan="3" align='left'>
																<c:set var="entityInterface" value="${containerInterface.entity}" />
																<jsp:useBean id="entityInterface" type="edu.common.dynamicextensions.domaininterface.EntityInterface" />
					
																ADD &nbsp;<c:out value="${entityInterface.name}" escapeXml="false" />
															</td>
														</tr>
	
														<c:set var="dummyControlCollection" value="${containerInterface.controlCollection}" />
														<jsp:useBean id="dummyControlCollection" type="java.util.Collection" />
														
														<% 
															for(int sequenceNumber = 1; sequenceNumber <= dummyControlCollection.size(); sequenceNumber++)
															{
														%>
																<c:forEach items="${containerInterface.controlCollection}" var="control">
																<jsp:useBean id="control" type="edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface" />
																<% 
																	if(sequenceNumber == control.getSequenceNumber().intValue())
																	{
																%>
																		<tr>
																			<%																			
																				AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
																				Collection<RuleInterface> ruleCollection = abstractAttribute.getRuleCollection();
																				boolean required = false;
																				if(ruleCollection != null && !ruleCollection.isEmpty())
																				{
																					for(RuleInterface attributeRule : ruleCollection)
																					{
																						if(attributeRule.getName().equals("required"))
																						{
																			%>
																							<td class="formRequiredNotice" width="2%">
																								<c:out value="${containerInterface.requiredFieldIndicatior}" />
																							</td>
																							<td class="formRequiredLabel" width="20%">
																								<label for="<c:out value="control_${control.sequenceNumber}"/>">
																									<c:out value="${control.caption}"/>
																								</label>
																							</td>
																			<%
																							required = true;
																							break;
																						}
																					}
																				}
																				if(required == false)
																				{
																			%>
																					<td class="formRequiredNotice" width="2%">&nbsp;</td>
																					<td class="formLabel" width="20%">
																						<label for="<c:out value="control_${control.sequenceNumber}"/>">
																							<c:out value="${control.caption}"/>
																						</label>
																					</td>
																			<%
																				}
																			%>
																																					
																			<td class="formField">
																				<% String generateHTMLStr = control.generateHTML(); %>
																				<% pageContext.setAttribute("generateHTMLStr", generateHTMLStr); %>
																				<c:out value="${generateHTMLStr}" escapeXml="false" />
																			</td>
																		</tr>
																<%
																	}
																%>
																</c:forEach>
														<%
															}
														%>
													</table>
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
							  	    					<c:when test='${showFormPreview  == "true"}'>
															<html:submit styleClass="actionButton"  onclick="addDynamicData()" disabled="true">
																<bean:message  key="buttons.submit" />
															</html:submit>
														</c:when>
							 							<c:otherwise>
							 								<html:hidden styleId='isEdit' property="isEdit" value=""/>
							 								<% 
							 									String target = "addDynamicData(" + recordIdentifier + ")";
							 								%>
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
		</html:form>		
	</body>
</html><%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@ page language="java" contentType="text/html" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="javax.servlet.jsp.JspWriter" %>

<%@ page import="edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.AssociationInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.AttributeInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.EntityInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.RoleInterface" %>

<%@ page import="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface" %>
<%@ page import="edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface" %>

<%@ page import="java.lang.String" %>
<%@ page import="java.util.Collection" %>

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

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">

	<%! 
		private void displayControls(ContainerInterface container, JspWriter out)
		{
			for(int sequenceNumber = 1; sequenceNumber <= dummyControlCollection.size(); sequenceNumber++)
			{
				Collection<ControlInterface> controlCollection = container.getControlCollection();
				for(ControlInterface control : controlCollection)
				{
					String controlSequence = "control_" + container.getId() + "_" + control.getSequenceNumber();
					if(sequenceNumber == control.getSequenceNumber().intValue())
					{
						String controlsHtml = "<tr>";
						AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
						if(abstractAttribute instanceOf AttributeInterface)
						{
							boolean required = false;
							Collection<RuleInterface> ruleCollection = abstractAttribute.getRuleCollection();
							if(ruleCollection != null && !ruleCollection.isEmpty())
							{
								for(RuleInterface attributeRule : ruleCollection)
								{
									if(attributeRule.getName().equals("required"))
									{
										controlsHtml += "<td class=\"formRequiredNotice\" width=\"2%\">" +  container.getRequiredFieldIndicatior() + "</td>";
										controlsHtml += "<td class=\"formRequiredLabel\" width=\"20%\"> + "<label for=\"" + controlSequence + "\">" + control.getCaption() + "</label></td>";
	
										required = true;
										break;
									}
								}
							}
							
							if(required == false)
							{
								controlsHtml += "<td class=\"formRequiredNotice\" width=\"2%\">&nbsp;</td>";
								controlsHtml += "<td class=\"formLabel\" width=\"20%\"> +"<label for=\"" + controlSequence + "\">" + control.getCaption() + "</label></td>";									
							}
							controlsHtml += "<td class=\"formField\">" + control.getHtmlString() + "</td></tr>";
							out.println(controlsHtml);
						}
						else if(abstractAttribute instanceOf AssociationInterface)
						{
							AssociationInterface association = (AssociationInterface) abstractAttribute;
							RoleInterface role = association.getTargetRole();
							if (role != null)
							{
								AssociationType associationType = role.getAssociationsType();
								if (associationType != null)
								{
									String associationTypeName = associationType.getValue();
									if (associationTypeName.equals(AssociationType.CONTAINTMENT))
									{
										EntityInterface targetEntity = association.getTargetEntity();
										ContainerInterface targetContainer = entityManager.getContainerByEntityIdentifier(targetEntity.getId());
										if (targetContainer != null)
										{
											displayControls(targetContainer, out);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	%>

		<html:form styleId="dataEntryForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" method="post">
			<c:set var="recordIdentifier" value="${dataEntryForm.recordIdentifier}" />
			<jsp:useBean id="recordIdentifier" type="java.lang.String"/>
			
			<html:hidden styleId='entitySaved' property="entitySaved" />
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
			
			<c:set var="showFormPreview" value="${dataEntryForm.showFormPreview}"/>
			<jsp:useBean id="showFormPreview" type="java.lang.String"/>		
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
													<c:set var="errorList" value="${dataEntryForm.errorList}"/>
													<jsp:useBean id="errorList" type="java.util.List"/>
													
													<!--<logic:messagesPresent message="false">
														<ul>
													 		<html:messages id="error" message="false">
													 			<li><bean:write name="error"/></li>
													 		</html:messages> 
														</ul> 
													</logic:messagesPresent>-->
													
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
												<td >
													<c:set var="containerInterface" value="${dataEntryForm.containerInterface}"/>
													<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>
													
													<table summary="" cellpadding="3" cellspacing="0"  align='center' width = '100%'>
														<tr>
															<td class="formMessage" colspan="3">
																<c:out value="${containerInterface.requiredFieldIndicatior}" escapeXml="false" />&nbsp;
																<c:out value="${containerInterface.requiredFieldWarningMessage}" escapeXml="false" />
															</td>
														</tr>
														<tr>
															<td class='formTitle' colspan="3" align='left'>
																<c:set var="entityInterface" value="${containerInterface.entity}" />
																<jsp:useBean id="entityInterface" type="edu.common.dynamicextensions.domaininterface.EntityInterface" />
					
																ADD &nbsp;<c:out value="${entityInterface.name}" escapeXml="false" />
															</td>
														</tr>
														
														<%
															displayControls(containerInterface, out);
														%>
	
													</table>
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
							  	    					<c:when test='${showFormPreview  == "true"}'>
															<html:submit styleClass="actionButton"  onclick="addDynamicData()" disabled="true">
																<bean:message  key="buttons.submit" />
															</html:submit>
														</c:when>
							 							<c:otherwise>
							 								<html:hidden styleId='isEdit' property="isEdit" value=""/>
							 								<% 
							 									String target = "addDynamicData(" + recordIdentifier + ")";
							 								%>
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
		</html:form>		
	</body>
</html>