<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : Deepti Shelar

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/css/stylesheet.css" />


<html>
<head>
<title>Dynamic Extensions</title>

	 <script language="javascript" src="<%= request.getContextPath()%>/jss/dynamicExtensions.js"></script>
	</head>
  <body  class='bodyStyle' >
<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
	  	<html:errors />
		<table class='bodyStyle' border='1'>
		<tr>
		<table class='bodyStyle'>
			<tr>
				<td class='standardBoldText' align='center'> Before going to previous page please select the action to be performed
				</td>
			</tr>
		</table>
		</tr>
			<tr>
			<table align='center'>
			<tr height="70%">
				<td/>
				</tr>	<tr>
				<td/>
			</tr>
			<tr>
			<td>
					<html:button styleClass="actionButton" property="prevButton" onclick='showCreateFormJSP();' >
							<bean:message  key="buttons.prev" />
					</html:button>
			</td>
			<td>
					<html:button styleClass="actionButton" property="addControlToFormButton" onclick='addControlToForm();' >
							<bean:message  key="buttons.addControlToForm" />
					</html:button>
			</td>
			<td>
					<html:reset styleClass="actionButton" property="cancelButton" onclick='closeWindow();'>
							<bean:message  key="buttons.cancel" />
					</html:reset>
			</td>	
			</tr>
			</table>
		</tr>
		</table>
		 </html:form>
 	</body>

</html>