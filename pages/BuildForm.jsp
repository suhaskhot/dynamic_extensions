<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>
<%@ page import="java.util.List"%>

<html>
	<!-- HTML Head section -->
	<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/styleSheet.css" />
		<LINK href="<%=request.getContextPath()%>/css/calanderComponent.css" type=text/css rel=stylesheet>
		<script src="<%=request.getContextPath()%>/jss/dynamicExtensions.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/script.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/overlib_mini.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/calender.js" type="text/javascript"></script>
		<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>

		<title>Dynamic Extensions</title>
	 <script language="JavaScript" type="text/javascript">
		function initCancelOperation()
	 	{
	    	var addControlBtnCaption = '<bean:message  key="buttons.addControlToForm" />';
	    	var addControlFormTitle = '<bean:message key="app.title.addAttributes" />';
	    	cancelControlOpern(addControlBtnCaption,addControlFormTitle);
	    }
 	 </script>
	</head>

	<!-- Initializations -->
	<c:set var="toolsList" value="${controlsForm.toolsList}"/>
	<jsp:useBean id="toolsList" type="java.util.List"/>

	<c:set var="htmlFile" value="${controlsForm.htmlFile}"/>
	<jsp:useBean id="htmlFile" type="java.lang.String"/>

	<c:set var="rootName" value="${controlsForm.rootName}"/>
 	<jsp:useBean id="rootName" type="java.lang.String"/>

	<c:set var="userSelectedTool" value="${controlsForm.userSelectedTool}"/>
 	<jsp:useBean id="userSelectedTool" type="java.lang.String"/>

	<c:set var="controlInformationObjectList" value="${controlsForm.childList}"/>
	<jsp:useBean id="controlInformationObjectList" type="java.util.List"/>

	<c:set var="selectedControlCaption" value="${controlsForm.selectedControlCaption}"/>
 	<jsp:useBean id="selectedControlCaption" type="java.lang.String"/>

	<!-- Main HTML Code -->
  	<body onload="initBuildForm()">

	<html:form styleId = "controlsForm"  action="/LoadFormControlsAction">
	<html:errors />

  	    <% int generator = 0; %>
		<table valign="top" style = "border-right:0px"  border = 1 align='right' width='90%' height="100%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
		     <!-- Main Page heading -->
	         <tr style = "border-bottom:0px">
	         	<td class = "tbBordersAllbordersNone" >&nbsp;</td>
	         	<td class="formFieldNoBorders" ><bean:message key="app.title.MainPageTitle" /></td>
	         </tr>

	         <!-- Tabs -->
	         <tr valign = "top">
				 <td class = "tbBordersAllbordersNone" >&nbsp;</td>
	     		 <td class="tbBordersAllbordersNone" valign="top" >
		     		<!-- tabs start -->
				  	 <table valign="top" summary="" align='left' width='95%' height = "95%" cellspacing="0" cellpadding="3" class = "tbBordersAllbordersBlack" >
						<tr valign = "top" >
						   <td class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showDefineGroupPage('controlsForm')">
							 <bean:message key="app.title.DefineGroupTabTitle" />
						   </td>

						   <td class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showNextActionConfirmDialog()">
							 <bean:message key="app.title.DefineFormTabTitle" />
						   </td>

						   <td class="tabMenuItemSelected" >
							 <bean:message key="app.title.BuildFormTabTitle" />
						   </td>

						   <td class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showFormPreview()" >
							 <bean:message key="app.title.PreviewTabTitle" />
						   </td>
						   <td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
						</tr>
					<!-- tab end -->
						<tr valign = "top" >
							<td valign="top" colspan="7" class="formFieldNoBorders" >
									 <bean:message key="app.title.formName" /><%=rootName%>
							</td>
						</tr>
						<tr valign = "top">
								<td style = "padding-left:5px" colspan="7"  height = '100%' width="100%">
									<table width="100%" height = '100%'   cellspacing="0" cellpadding="0"  valign = "top">
										<tr valign = "top" height = '100%' >
											<td valign = "top" valign="top" width = "75%">

												<table class="tbBordersAllbordersBlack" height = "100%" width ="100%" >
													<thead>
														<tr>

														<c:choose>
															<c:when test='${controlsForm.controlOperation == "Edit"}'>
																<td id="formTitle" align="left" class="formTitleGray"><bean:message key="app.title.editAttributes" /></th>
															</c:when>

															<c:otherwise>
																<td id="formTitle"  align="left" class="formTitleGray"><bean:message key="app.title.addAttributes" /></th>
															</c:otherwise>
														</c:choose>
														</tr>
													</thead>
													<tr>
														<td>
															<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />
														</td>
													</tr>
													<tr>
													<td>
														<table width="100%" height = '100%'  >
															<tr>
																<td width="15%" height = '100%'  class="toolBoxTable"  align="center">
																	<table valign = "top" align ="center" height = '100%' width = '100%' class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
																		<tr height = '100%' width = '100%' valign = "top" style="background-color:#F4F4F5;">
																			<td height = '100%' width = '100%' align="center">
																				<dynamicExtensions:ToolsMenu id="BuildForm"
																						toolsList = "<%=toolsList%>"
																						onClick="controlSelectedAction"
																						selectedUserOption="<%= userSelectedTool%>">
																				 </dynamicExtensions:ToolsMenu>
																			</td>
																		</tr>
																	</table>

																</td>
																<td height = '100%'>
																	<table valign = "top" align ="left" height = '100%' width = '100%' class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
																		<thead>
																			<tr >
																				<th align="left" class="formTitleGray">
																					<%=selectedControlCaption%> <bean:message key="app.formControl.properties" />
																				</th>
																			</tr>
																		</thead>
																		<tr height = '100%' width = '100%' valign = "top">
																			<td height = '100%' width = '100%'>
																				<jsp:include page="<%=htmlFile%>" />
																			</td>
																		<tr>
																	</table>
																</td>
															</tr>
														</table>
													</td>
													</tr>
												</table>
											</td>
											<td width="2%">&nbsp;
											</td>
											<td  valign="top" height="100%" >
												<table valign="top" height = '100%' width = "100%"  cellspacing="0" cellpadding="0">
													<tr valign = "top" height = '100%' >
														<td  height = '100%' width = 100%>
															<table id='controlList' border="1" cellspacing="0"  class="tbBordersAllbordersBlack" height = "100%" width ="100%" >
															<thead>
																<tr>
																	<th colspan="3" align="left" class="formTitleGray"><bean:message  key="app.formControlsTree.heading" /></th>
																</tr>
															</thead>
															<tbody>
																<c:forEach var="controlInfoObj" items = "${controlInformationObjectList}" varStatus="counter" >
																<c:set var="controlName" value="${controlInfoObj.controlName}"/>
																<jsp:useBean id="controlName" type="java.lang.String"/>

																<c:set var="controlType" value="${controlInfoObj.controlType}"/>
																<jsp:useBean id="controlType" type="java.lang.String"/>

																<c:set var="identifier" value="${controlInfoObj.identifier}"/>
																<jsp:useBean id="identifier" type="java.lang.String"/>




															<tr height = "5%"   style = "cursor:hand">
																<td class="formMessage">
																	<input type = "checkbox" name = "checkAttribute"  id = "<%=identifier%>" value = "<%=identifier%>"  />
																	<input type = "hidden"  id = "sequenceNumbers" name = "sequenceNumbers" value = "<%=generator+1%>"  />
																	<%String hiddenRowId = identifier + "rowNum";%>
																	<input type = "hidden"  id = "<%=hiddenRowId%>" name = "<%=hiddenRowId%>" value = "<%=generator++ + 1%>"  />
																</td>
																<td class="formMessage" style="padding-left:2px">
																	<div id = "<%=identifier%>" noWrap='true' style='overflow-x:hidden; text-overflow:ellipsis; width:60px;' onmouseout = "hideTooltip();" onmouseover = "showTooltip(this.innerHTML,this,this.innerHTML);" onclick = "controlSelected(this);">
																		<%=controlName%>
																		</div>
																</td>

																<td class="formMessage" style="padding-left:2px">
																	<div noWrap='true' style='overflow-x:hidden; text-overflow:ellipsis; width:60px;' onmouseover = "showTooltip(this.innerHTML,this,this.innerHTML);" onmouseout = "hideTooltip();">
																		<%=controlType%>
																	</div>
																</td>
															</tr>



																</c:forEach>

																<tr height = "100%">
																	<td>&nbsp;
																	</td>
																</tr>
																</tbody>
															</table>
														</td>
													</tr>
												</table>

											</td>
										</tr>

								</table>

							</td>
						</tr>

						<!--Add attributes btn + Controls (UP + Down + Delete) btn) -->
						<tr>
							<td colspan="7">
								<table width="100%" height = '100%'  valign = "top">
									<tr valign = "top" height = '100%' >
										<td align="right" valign="top" width = "75%" >
											<html:button styleClass="actionButton" property="addControlToFormButton" onclick="addControlToFormTree()" >
												<bean:message  key="buttons.addControlToForm" />
											</html:button>
											<html:button styleClass="actionButton" property="cancelControlOperation" onclick="initCancelOperation()">
												<bean:message  key="buttons.cancel" />
											</html:button>
										</td>
										<td width="2%">&nbsp;</td>
										<td  valign="top" height="100%" >
											<input type = "button" name = "upButton" value = "Up"  onclick = "decreaseSequencenumber()"/>
											<input type = "button" name = "downButton" value = "down"  onclick = "increaseSequencenumber()"/>
											<input type = "button" name = "deleteButton" value = "Delete"  onclick = "deleteControl()"/>

										</td>
									</tr>

								</table>
							</td>
						</tr>
						<tr class="formLabelBorderless">
							<td colspan="2" align="left">
								<html:submit styleClass="actionButton" onclick="saveEntity()">
																<bean:message  key="buttons.save" />
								</html:submit>
								&nbsp;
								<html:reset styleClass="actionButton" property="cancelButton" onclick='showHomePageFromBuildForm()'>
																	<bean:message  key="buttons.cancel" />
								</html:reset>
							</td>
							<td colspan="5" align="right">
								<html:button styleClass="actionButton" property="prevButton" onclick="showNextActionConfirmDialog()" >
										<bean:message  key="buttons.prev" />
								</html:button>
									&nbsp;
								<html:button styleClass="actionButton" property="showPreviewButton" onclick="showFormPreview()" >
										<bean:message  key="buttons.showPreview" />
								</html:button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		  	<html:hidden styleId ='operation'  property="operation" value=""/>
		  	<html:hidden styleId = 'selectedAttrib' property="selectedAttrib" value=""/>
			<input type="hidden" name="entitySaved" id = 'entitySaved'  />
			<input type="hidden" id = "previousControl" name="previousControl" value = "" />
			<html:hidden styleId = 'controlOperation' property="controlOperation" />
			<html:hidden styleId = 'selectedControlId' property="selectedControlId" />
			<html:hidden styleId = 'sequenceNumbers' property="sequenceNumbers"  value=""/>
			<input type="hidden" id = "checkAttribute" name="checkAttribute" value = "" />


	  	</html:form>
  	</body>

</html>