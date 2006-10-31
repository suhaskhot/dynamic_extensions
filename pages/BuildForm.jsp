
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeData"%>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeGenerator"%>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
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
						<td class="formTitle" align='left' colspan="3" >
						<!-- Menu tabs -->
							 <table valign="top" summary="" cellpadding="0" cellspacing="0" border="0" height="5%">
							             <tr>
							               <!-- link 1 begins -->
							               <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" >
							                 <a href="pages/DefineGroup.jsp" class="mainMenuLink"><bean:message key="app.title.DefineGroupTabTitle" /></a>
							               </td>
							               <!-- link 1 ends -->
							 			   <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
							 			  <!-- link 2 begins -->
							 			  <td height="20" class="mainMenuItem" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()"  onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" >
											  <a href="javascript:showNextActionConfirmDialog()" class="mainMenuLink"><bean:message key="app.title.DefineFormTabTitle" /></a>
							               </td>
							               <!-- link 2 ends -->
							 			 	<td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>

							               <!-- link 3 begins -->
							                <td height="20" class="mainMenuItemSelected" >
										   		<a href="#" class="mainMenuLink"><bean:message key="app.title.BuildFormTabTitle" /></a>
							               </td>

							              <!-- link 3 ends -->
							               <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
							 		      <!-- link 4 begins -->
							               <td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" >
							                 <a href="javascript:showFormPreview()" class="mainMenuLink"><bean:message key="app.title.PreviewTabTitle" /></a>
							               </td>
							               <!-- link 4 ends -->
							               <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
							 			</tr>
							     </table>
<!-- menu ends -->
						 </td>
			   		</tr>
			<tr><td ><%=rootName%> </td></tr>

			<tr>
			<td valign="top" >
		  	<table align = 'center' width='100%'  class="tbBorders1" cellspacing="0" cellpadding="0" >

			   <!--<tr class="formTitle">
			   		<td colspan="3" align="center">
			   			<bean:message  key="app.formControlsPage.heading" />
			   		</td>
			   	</tr>-->
				<tr>
					<td class="formFieldSized1" >
						&nbsp;
					</td>

					<td class="formFieldSized1" >
						<%=userSelectedTool%> <bean:message  key="app.formControl.properties" />
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
		<td valign="top" >
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