<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/HTMLGeneration.tld" prefix="htmlgenerate" %>

<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="toolsList" value="${controlsForm.toolsList}"/>
 <jsp:useBean id="toolsList" type="java.util.List"/>
<c:set var="selectedControlAttributesList" value="${controlsForm.selectedControlAttributesList}"/>
 <jsp:useBean id="selectedControlAttributesList" type="java.util.List"/>
<html>
<head>Build Form 
<title>Dynamic Extensions</title>
	 
  <body>
<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
	  	<html:errors />
	  	<table align = 'center' width='100%' border="3">
	  		<tr>
	  			<td>  		
				    <dynamicExtensions:ToolsMenu id="BuildForm" 
							toolsList = "<%=toolsList%>" 
							onClick="controlSelectedAction"
							height="100%" width="100%">						
			    </dynamicExtensions:ToolsMenu>
	  			</td>
	  			<td align="top">
	  			Controls definition
	  			<htmlgenerate:generatehtml uiControlsList="<%=selectedControlAttributesList%>"/>
	  			
	  			</td>
	  			<td>
	  			Form Controls Tree
	  			</td>
	  		</tr><tr>
			<td>
			</td>
	<td align="right">
					<html:button styleClass="actionButton" property="addButton" >
							<bean:message  key="buttons.addControlToForm" />
					</html:button>
			
					<html:reset styleClass="actionButton" property="clearButton"  >
							<bean:message  key="buttons.clear" />
					</html:reset>
				</td>
				</tr>
			
	  	</table>
		 <table summary="" align = 'left' cellpadding="5" cellspacing="0" border="0">
		    <tr height="5">
			
				<td>
					<html:submit styleClass="actionButton">
							<bean:message  key="buttons.save" />
					</html:submit>
				</td>
	
				<td>
<html:reset styleClass="actionButton" property="cancelButton" onclick="showHomePageFromBuildForm()"> <bean:message  key="buttons.cancel" />
</html:reset>
				</td>	  <td width="275">
				</td>
					  <td width="45%">
				</td>
					<td>
					<html:button styleClass="actionButton" property="prevButton" onclick="showCreateFormView()" >
							<bean:message  key="buttons.prev" />
					</html:button>
				</td>
				<td>
					<html:button styleClass="actionButton" property="nextButton" onclick="showPreview()" >
							<bean:message  key="buttons.showPreview" />
					</html:button>
				</td>
			
		</table>
	  	<html:hidden property="operation" value=""/>
	  	<html:hidden property="selectedTool" value=""/>
	  	</html:form>
	  	</body>
		</head>
		</html>