
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeData"%>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeGenerator"%>

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
 	

<%

	TreeGenerator treeGenerator = new TreeGenerator();
	treeGenerator.setContextPath(request.getContextPath());
	TreeData treedataObj = treeGenerator.getTreeData(rootName,childList);
%>


<html>
	<head>
		<title>Dynamic Extensions</title>

  		<body onload="initBuildForm()">

		<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
		  <html:errors />
<table valign="top"  align='left' width='90%' height="90%" border='0' cellspacing="0" cellpadding="0" class="tbBorders1" >
         <!-- Main Page heading -->
         <tr><td class="formFieldSized1" ><bean:message key="app.title.MainPageTitle" /></td></tr>

	          <tr>
	     		<td valign="top" >
			  	 <table valign="top" summary="" align='left' width='100%' cellspacing="0" cellpadding="3"  >
					<tr >
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


			<tr>
			<td valign="top" colspan="7" >
					<table align = 'center' width='100%'  class="tbBorders1" cellspacing="0" cellpadding="0" >

					   <!--<tr class="formTitle">
							<td colspan="3" align="center">
								<bean:message  key="app.formControlsPage.heading" />
							</td>
						</tr>-->
						<tr>
							<td class="formFieldSized1" >
								<%=rootName%>
							</td>

							<td class="formFieldSized1" >
								<%=selectedControlCaption%> <bean:message  key="app.formControl.properties" />
							</td>

							<td class="formFieldSized1" >
								<bean:message  key="app.formControlsTree.heading" />
							</td>
						</tr>

						<tr>
							<td class="toolBoxTable" width="10%" align="left">
								<dynamicExtensions:ToolsMenu id="BuildForm"
										toolsList = "<%=toolsList%>"
										onClick="controlSelectedAction"
										selectedUserOption="<%= userSelectedTool%>"
										height="100%" width="100%">
								 </dynamicExtensions:ToolsMenu>
							</td>

							<td align="top" width="70%" >
									<jsp:include page="<%=htmlFile%>" />

							</td>

							<td  valign="top">
								<label class="formRequiredLabel"><bean:message  key="app.FormControlsTreePageHeading" /></label>
								<dynamicExtensions:tree treeDataObject="<%=treedataObj%>" />
							</td>
						</tr>

						<tr>
							<td colspan="2" align="right">
									<html:button styleClass="actionButton" property="addControlToFormButton" onclick="addControlToFormTree()" >
												<bean:message  key="buttons.addControlToForm" />
									</html:button>

									<!--<html:reset styleClass="actionButton" property="clearButton" onclick="clearForm()" >
												<bean:message  key="buttons.clear" />
									</html:reset>-->
							 </td>
						</tr>
				</table>
			</td>
			</tr>
		<tr>
		<td valign="top" colspan="7">
			 <table summary="" align = 'left' cellpadding="5" cellspacing="0"  class='bodyStyle'>
				<tr height="5">

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
			<html:hidden property="controlOperation" />
			<html:hidden property="selectedControlId" />
			<html:hidden property="toolBoxClicked" value=""/>
			<html:hidden property="showPreview" value=""/>



	  	</html:form>
  	</body>
  </head>
	
</html>