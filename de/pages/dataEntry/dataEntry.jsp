<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>

<%-- Imports --%>
<%@ page language="java" contentType="text/html" %>
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page import="org.apache.struts.action.ActionMessages" %>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/de/css/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/de/css/de_style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/de/css/ext-all.css" />
<link href="<%=request.getContextPath()%>/de/css/calanderComponent.css" type=text/css rel=stylesheet />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/de/css/xtheme-gray.css" />

<script src="<%=request.getContextPath()%>/de/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/de/jss/script.js" type="text/javascript"></script>

<script>var imgsrc="<%=request.getContextPath()%>/de/images/";</script>
<script src="<%=request.getContextPath()%>/de/jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/de/jss/overlib_mini.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/de/jss/calender.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/de/jss/calendarComponent.js"></script>
<script src="<%=request.getContextPath()%>/de/jss/ajax.js"></script>

<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/scriptaculous.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/combobox.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de//jss/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=request.getContextPath()%>/de/jss/combos.js"></script>


<c:set var="containerInterface" value="${dataEntryForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<c:set var="showFormPreview" value="${dataEntryForm.showFormPreview}"/>
<jsp:useBean id="showFormPreview" type="java.lang.String"/>

<c:set var="errorList" value="${dataEntryForm.errorList}"/>
<jsp:useBean id="errorList" type="java.util.List"/>

<c:set var="dataEntryOperation" value="${dataEntryForm.dataEntryOperation}"/>
<jsp:useBean id="dataEntryOperation" type="java.lang.String"/>

<c:set var="recordIdentifier123" value="${dataEntryForm.recordIdentifier}" />
<jsp:useBean id="recordIdentifier123" type="java.lang.String"/>

<c:set var="mode" value="${dataEntryForm.mode}" />
<jsp:useBean id="mode" type="java.lang.String"/>

<c:set var="isTopLevelEntity" value="${dataEntryForm.isTopLevelEntity}" />
<jsp:useBean id="isTopLevelEntity" type="java.lang.Boolean"/>

<script language="JavaScript" >
		resetTimeoutCounter();
</script>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm('<%=request.getContextPath()%>')" onclick="window.parent.parent.detectApplicationUsageActivity()" onkeydown="window.parent.parent.detectApplicationUsageActivity()">
		<html:form styleId="dataEntryForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" method="post">
		<div id="dataEntryFormDiv" style="position:absolute;overflow:auto;height:100%;width:100%;z-index:1000;">
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
		
			<c:choose>
				<c:when test='${showFormPreview == "true"}'>
					<table valign="top" style="border-right:1px" align='center' width='100%' height="100%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
				</c:when>
				<c:otherwise>
					<table valign="top" align='center' width='100%' height="100%" border='0' cellspacing="0" cellpadding="0">
				</c:otherwise>
			</c:choose>
				<!-- Main Page heading -->
				<c:if test='${showFormPreview == "true"}'>
				<tr height="7%">
					<td class="formFieldNoBorders">
							<bean:message key="app.title.MainPageTitle" />
					</td>
				</tr>
				</c:if>
	 
				<tr valign="top" height="93%">
					<td>
						<table valign="top" summary="" align='center' width='100%' cellspacing="0" cellpadding="0" border="0">
							<c:if test='${showFormPreview == "true"}'> 
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
						
							</c:if>	
							<tr valign="top">
								<td colspan="7">
									<table  align='center' width='100%'>
										<tr>
											<%
												if(errorList.size() != 0)
												{
											%>
												<table border="0" align="center" cellpadding="1" cellspacing="0" class="td_color_6e81a6">
                                              		<tr>
													<td>
													<table width="100%" height="30"  border="0" cellpadding="4" cellspacing="4" class="td_color_FFFFCC">  
													<c:forEach items="${errorList}" var="error">
														<jsp:useBean id="error" type="java.lang.String"/>
														 <tr>
														<th align="center" class="font_bl_nor"><img src="<%=request.getContextPath()%>/de/images/ic_error.gif" alt="Error" width="28" height="25" hspace="3" align="absmiddle">
															<c:out value="${error}"/><br />
													</c:forEach>
														</th>
                                                    	</tr>
                                                	</table>
													</td>
													</tr>
												</table>
											<%
												} else {
														
												
											%>		<logic:messagesPresent message="true">								
													<table border="0" align="center" cellpadding="1" cellspacing="0" class="td_color_6e81a6">
														<tr>
															<td><table width="100%" height="30"  border="0"		cellpadding="4" cellspacing="4"			class="td_color_FFFFCC">
																	<tr>
																		<th align="left" class="font_bl_nor">
																		<ul>
																			<html:messages id="msg" message="true"> 
																			<img src="<%=request.getContextPath()%>/de/images/ic_success.gif" alt="Success" width="28" height="25" hspace="3" align="absmiddle"><bean:write name="msg"/>
																			</html:messages>
																		</ul>
																		</th>
																	</tr>
																 </table>
																</td>
															</tr>
														</table>
													    </logic:messagesPresent>
													<%
												
												}
											%>
                                          </tr>

										<tr><td>&nbsp;</td></tr>
										<tr>
											<td valign="top">
												<dynamicExtensions:dynamicUIGenerator containerInterface="<%=containerInterface%>" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr class="td_bg_bottom">
								<td valign="top" colspan="7">
									<table cellpadding="0" cellspacing="5" border="0" align='center'>
										<tr height="5"></tr>
										<tr>
											<td align='center'>						
												<c:choose>
													<c:when test='${showFormPreview=="true"}'>
														<html:submit styleClass="actionButton" onclick="showParentContainerInsertDataPage()">
															<bean:message key="buttons.back" />
														</html:submit>
													</c:when>
													<c:otherwise>
														<html:hidden styleId='isEdit' property="isEdit" value=""/>
														
														<c:if test='${(isTopLevelEntity=="false")}'>
														     <img src="<%=request.getContextPath()%>/de/images/b_submit.gif" alt="Submit" width="62" height="21" hspace="3" align="absmiddle" onClick="showParentContainerInsertDataPage()" >
															<map alt="Submit">
														<area href="javascript:showParentContainerInsertDataPage()" shape="default">
															</map>
														</c:if>
																												
														<c:if test='${(mode=="edit") && (isTopLevelEntity=="true")}'>
															<img src="<%=request.getContextPath()%>/de/images/b_submit.gif" alt="Submit" width="62" height="21" hspace="3" align="absmiddle" onClick="setInsertDataOperation()">
															<map alt="Submit">
																<area href="javascript:setInsertDataOperation()" shape="default">
															</map>

														<!-- BUG 7662 FIXED. Each Cancel should take you one level up in the containment hierarchy and finally the Cancel on Main Class should take you to the Add Records page.-->
														</c:if>
															<input type="hidden" id="operation_mode" value="insertChildData"/>
														<c:if test='${(isTopLevelEntity=="true")}'>
															<script>
															document.getElementById('operation_mode').value = "insertParentData";
															</script>
														</c:if>
								
														<c:if test='${!((mode=="view") && (isTopLevelEntity=="false"))}'>
															<img src="<%=request.getContextPath()%>/de/images/b_cancel.gif" alt="Cancel" width="62" height="21" hspace="3" align="absmiddle" onclick="cancelInsertData()">
															<map alt="Cancel">
																<area href="javascript:cancelInsertData()" shape="default">
															</map>
														</c:if>
														

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
			

			<html:hidden styleId='recordIdentifier' property="recordIdentifier"/>
			<html:hidden styleId='entitySaved' property="entitySaved"/>
			<html:hidden property="containerId" styleId="dataEntryForm" value="<%=containerInterface.getId().toString()%>"/>
			<input type="hidden" id="childContainerId" name="childContainerId" value=""/>
			<input type="hidden" id="childRowId" name="childRowId" value=""/>
			<input type="hidden" id="dataEntryOperation" name="dataEntryOperation" value="<%=dataEntryOperation%>"/>
			<input type="hidden" id="showFormPreview" name="showFormPreview" value="<%=showFormPreview%>"/>
			<input type="hidden" id="mode" name="mode" value="<%=mode%>"/>
			</div>
		</html:form>
	</body>
</html>