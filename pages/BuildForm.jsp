<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeData"%>
<%@ page import="edu.common.dynamicextensions.ui.webui.util.TreeGenerator"%>

<%
	
	TreeGenerator treeGenerator = new TreeGenerator();
	treeGenerator.setContextPath(request.getContextPath());
	TreeData treedataObj = treeGenerator.getTreeData();
%>
<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="toolsList" value="${controlsForm.toolsList}"/>
 <jsp:useBean id="toolsList" type="java.util.List"/>
<c:set var="selectedControlAttributesList" value="${controlsForm.selectedControlAttributesList}"/>
 <jsp:useBean id="selectedControlAttributesList" type="java.util.List"/>
 <c:set var="userSelectedTool" value="${controlsForm.userSelectedTool}"/>
 <jsp:useBean id="userSelectedTool" type="java.lang.String"/>
<html>
<head>
<title>Dynamic Extensions</title>
	 
  <body onload="initBuildForm()">
<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
	  	<html:errors />
	  	<table align = 'center' width='100%' border="3" class="bodyStyle">
		   <tr height = 40><td/>	<td class='standardBoldText' align='center'>Build Form</td>	</tr>
		   <tr>	<td/>	   	<td align="center" class="standardBoldText"> <%= userSelectedTool %> </td></tr>
	  		<tr>
	  			<td class="toolBoxTable">  		
				    <dynamicExtensions:ToolsMenu id="BuildForm" 
							toolsList = "<%=toolsList%>" 
							onClick="controlSelectedAction"
							selectedUserOption="<%= userSelectedTool%>"
							height="100%" width="100%">						
			    </dynamicExtensions:ToolsMenu>
	  			</td>
	  			<td align="top">
	  			<dynamicExtensions:generatehtml uiControlsList="<%=selectedControlAttributesList%>"/>
	  			
	  			</td>
	  			<td  valign="top" >
	  			Form Controls Tree
	  			<dynamicExtensions:tree treeDataObject="<%=treedataObj%>" />
	  			</td>
	  		</tr><tr>
			<td>
			</td>
	<td align="right">
					<html:button styleClass="actionButton" property="addControlToFormButton" onclick="addControlToFormTree()" >
							<bean:message  key="buttons.addControlToForm" />
					</html:button>
			
					<html:reset styleClass="actionButton" property="clearButton"  >
							<bean:message  key="buttons.clear" />
					</html:reset>
				</td>
				</tr>
			
	  	</table>
		 <table summary="" align = 'left' cellpadding="5" cellspacing="0" border="0"  class='bodyStyle'>
		    <tr height="5">
			
				<td>
					<html:submit styleClass="actionButton">
							<bean:message  key="buttons.save" />
					</html:submit>
				</td>
	
				<td>
					<html:reset styleClass="actionButton" property="cancelButton" onclick='showHomePageFromBuildForm()'>
							<bean:message  key="buttons.cancel" />
					</html:reset>
				</td>	  <td width="275">
				</td>
					  <td width="45%">
				</td>
					<td>
					<html:button styleClass="actionButton" property="prevButton" onclick="showNextActionConfirmDialog()" >
							<bean:message  key="buttons.prev" />
					</html:button>
				</td>
				<td>
					<html:button styleClass="actionButton" property="showPreviewButton" onclick="showPreview()" >
							<bean:message  key="buttons.showPreview" />
					</html:button>
				</td>
			
		</table>
	  	<html:hidden property="operation" value=""/>
	  	<html:hidden property="selectedAttrib" value=""/>
	  	</html:form>
	  	</body>
		</head>
		</html>