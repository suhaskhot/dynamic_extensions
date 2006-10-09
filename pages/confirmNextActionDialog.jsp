<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />


<html>
<head>
<title>Dynamic Extensions</title>

	 <script language="javascript" src="<%= request.getContextPath()%>/jss/dynamicExtensions.js"></script>
	</head>
  <body >
<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
	  	<html:errors />
		<table>
			<tr>
				<td class='standardBoldText' align='center'> Before going to previous page please select the action to be performed
				</td>
			</tr>
		</table>
			<tr>
			<table align='center'>
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
			</table>
		</tr>
		 </html:form>
 	</body>

</html>