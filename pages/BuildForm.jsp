
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeData"%>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeGenerator"%>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.ControlInformationObject"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/script.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

	<c:set var="toolsList" value="${controlsForm.toolsList}"/>
	<jsp:useBean id="toolsList" type="java.util.List"/>

	<c:set var="htmlFile" value="${controlsForm.htmlFile}"/>
	<jsp:useBean id="htmlFile" type="java.lang.String"/>

	<% htmlFile = "/pages/toolTypeHTML/"+ htmlFile;%>

	<c:set var="childList" value="${controlsForm.childList}"/>
	<jsp:useBean id="childList" type="java.util.List"/>

	<c:set var="rootName" value="${controlsForm.rootName}"/>
 	<jsp:useBean id="rootName" type="java.lang.String"/>

	<c:set var="userSelectedTool" value="${controlsForm.userSelectedTool}"/>
 	<jsp:useBean id="userSelectedTool" type="java.lang.String"/>

 	<c:set var="selectedControlCaption" value="${controlsForm.selectedControlCaption}"/>
 	<jsp:useBean id="selectedControlCaption" type="java.lang.String"/>

	<c:set var="controlInformationObjectList" value="${controlsForm.childList}"/>
	<jsp:useBean id="controlInformationObjectList" type="java.util.List"/>


<html>
	<head>
		<title>Dynamic Extensions</title>

  		<body onload="initBuildForm()">

		<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
		  <html:errors />
<table valign="top" style = "border-right:0px"  border = 1 align='right' width='90%' height="100%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
         <!-- Main Page heading -->
         <tr style = "border-bottom:0px"><td class = "tbBordersAllbordersNone" width = '30px'>&nbsp;</td><td class="tbBordersAllbordersNone formFieldSized1" ><bean:message key="app.title.MainPageTitle" /></td></tr>

	          <tr valign = "top">
			  <td class = "tbBordersAllbordersNone" width = '30px'>&nbsp;</td>
	     		<td class="tbBordersAllbordersNone" valign="top" >
			  	 <table valign="top" summary="" align='left' width='95%' height = 95% cellspacing="0" cellpadding="3" class = "tbBordersAllbordersBlack" >
					<tr valign = "top" >
					   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="alert('This page is still under construction and will be available in the next release');">
						 <bean:message key="app.title.DefineGroupTabTitle" />
					   </td>

					   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showNextActionConfirmDialog()">
						 <bean:message key="app.title.DefineFormTabTitle" />
					   </td>

					   <td height="20" class="tabMenuItemSelected" >
						 <bean:message key="app.title.BuildFormTabTitle" />
					   </td>

					   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onClick="showFormPreview()" >
						 <bean:message key="app.title.PreviewTabTitle" />
					   </td>
					   <td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
					</tr>


			<tr valign = "top">
			<td valign="top" colspan="7" >
					<table align = 'center' width='100%' height = '100%' class="tbBordersAllbordersNone" cellspacing="0" cellpadding="0" >

					   <!--<tr valign = "top" class="formTitle">
							<td colspan="3" align="center">
								<bean:message  key="app.formControlsPage.heading" />
							</td>
						</tr>-->
						<tr style = "padding-left:5px" valign = "top">
							<td class="formMessage" >
								<%=rootName%>
							</td>

							<td style = "padding-left:10px" class="formMessage" >
								<%=selectedControlCaption%> <bean:message  key="app.formControl.properties" />
							</td>

							<td style = "padding-left:30px" class="formMessage" >
								&nbsp;
							</td>
						</tr>

						<tr valign = "top">
							<td style = "padding-left:5px" height = 100% >
								<table height = '100%'  class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0"  valign = "top">
									<tr valign = "top" height = '100%' >
										<td  height = '100%' width = '100%' class="toolBoxTable"  align="center">
											<dynamicExtensions:ToolsMenu id="BuildForm"
													toolsList = "<%=toolsList%>"
													onClick="controlSelectedAction"
													selectedUserOption="<%= userSelectedTool%>"
													>
											 </dynamicExtensions:ToolsMenu>
										</td>
									</tr>
									<tr height = 100%>
										<td>&nbsp;
										</td>
									</tr>
								</table>
							</td>

							<td valign = "top"  style = "padding-left:10px" valign="top" width = 50%>
							<table valign = "top" align ="left" height = '100%' width = '100%' class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
								<tr height = '100%' width = '100%' valign = "top">
									<td height = '100%' width = '100%'>
										<jsp:include page="<%=htmlFile%>" />
									</td>
								<tr>
							</table>
							</td>

							<td style = "padding-left:30px" valign="top">
								<table height = '100%' width = 95%  class="tbBordersAllbordersBlack" cellspacing="0" cellpadding="0">
									<tr valign = "top" height = '100%' width = 100%>
										<td  height = '100%' width = 100%>
											<table border = 1  height = 100% width =100% cellpadding="0" cellspacing="0" >
											<thead>
												<tr>
													<th class="formLabel" colspan="3"><bean:message  key="app.formControlsTree.heading" /></th>
												</tr>
											</thead>
												<c:forEach var="controlInfoObj" items = "${controlInformationObjectList}" varStatus="counter" >
												<c:set var="controlName" value="${controlInfoObj.controlName}"/>
												<jsp:useBean id="controlName" type="java.lang.String"/>

												<c:set var="controlType" value="${controlInfoObj.controlType}"/>
												<jsp:useBean id="controlType" type="java.lang.String"/>

												<c:set var="identifier" value="${controlInfoObj.identifier}"/>
												<jsp:useBean id="identifier" type="java.lang.String"/>
												<tbody>
													<tr height = "5%" id = "<%=identifier%>" onclick = "controlSelected(this);" style = "cursor:hand">
														<td class="formMessage">

															<input type = "checkbox" name = "check" disabled id = "<%=identifier%>"/>

														</td>
														<td class="formMessage" style="padding-left:2px">
														<div noWrap='true' style='overflow-x:hidden; text-overflow:ellipsis; width:60px;' onmouseout = "hideTooltip();" onmouseover = "showTooltip(this.innerHTML,this,this.innerHTML);">
															<%=controlName%>
															</div>
														</td>
														<td class="formMessage" style="padding-left:2px">
														<div noWrap='true' style='overflow-x:hidden; text-overflow:ellipsis; width:60px;' onmouseover = "showTooltip(this.innerHTML,this,this.innerHTML);" onmouseout = "hideTooltip();">
															<%=controlType%>
															</div>
														</td>
													</tr>
												</tbody>
												</c:forEach>
												<tr height = 100%>
													<td>&nbsp;
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>

							</td>
						</tr>

						<tr colspan="2" valign = "top" align="right">
							<td style = "padding-top:3px" colspan="2" align="right">
									<html:button styleClass="actionButton" property="addControlToFormButton" onclick="addControlToFormTree()" >
												<bean:message  key="buttons.addControlToForm" />
									</html:button>

									<!--<html:reset styleClass="actionButton" property="clearButton" onclick="clearForm()" >
												<bean:message  key="buttons.clear" />
									</html:reset>-->
							 </td>
							 <td style = "padding-left:30px" >
								<table summary="" align = 'left' cellpadding="3" cellspacing="0">
									<tr>
										<td>
											<input type = "button" name = "upButton" value = "Up" disabled onclick = ""/>
										</td>
										<td>
											<input type = "button" name = "upButton" value = "down" disabled onclick = ""/>
										</td>
										<td>
											<input type = "button" name = "upButton" value = "Delete" disabled onclick = ""/>
										</td>

									</tr>
								</table>
							 </td>
						</tr>
				</table>
			</td>
			</tr>
		<tr valign = "top" >
		<td valign="top" colspan="7">
			 <table class= "formLabelBorderless" summary="" align = 'left' cellpadding="5" cellspacing="0"  class='bodyStyle'>
				<tr valign = "top" height="5">

					<td>
							<html:submit styleClass="actionButton" onclick="saveEntity()">
								<bean:message  key="buttons.save" />
							</html:submit>
					</td>

					<td>
							<html:reset styleClass="actionButton" property="cancelButton" onclick='showHomePageFromBuildForm()'>
									<bean:message  key="buttons.cancel" />
							</html:reset>
					</td>

					<td width="275">
					</td>


					<td width="45%">
					</td>

					<td>
							<html:button styleClass="actionButton" property="prevButton" onclick="showNextActionConfirmDialog()" >
									<bean:message  key="buttons.prev" />
							</html:button>
					</td>

					<td>
							<html:button styleClass="actionButton" property="showPreviewButton" onclick="showFormPreview()" >
									<bean:message  key="buttons.showPreview" />
							</html:button>
					</td>

				</table>
			</td>
			</tr>
		</table>
		  	<html:hidden property="operation" value=""/>
		  	<html:hidden property="selectedAttrib" value=""/>
			<input type="hidden" name="entitySaved" />
			<input type="hidden" id = "previousControl" name="previousControl" value = "" />
			<html:hidden property="controlOperation" />
			<html:hidden property="selectedControlId" />
			<html:hidden property="toolBoxClicked" value=""/>
			<html:hidden property="showPreview" value=""/>



	  	</html:form>
  	</body>
	</head>
</html>