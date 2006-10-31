<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : sujay narkar, chetan_patil --%>

<%-- TagLibs --%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%-- Imports --%>
<%@
	page language="java" contentType="text/html"
%>

<%-- Stylesheet --%>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/calendarComponent.js"></script>
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>

<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<c:set var="containerInterface" value="${previewForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId = "previewForm" action="/LoadFormPreviewAction" enctype="multipart/form-data" >
		<html:errors />
				<html:hidden property="entitySaved" />
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
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
									  <a href="#" class="mainMenuLink"><bean:message key="app.title.DefineFormTabTitle" /></a>
								   </td>
								   <!-- link 2 ends -->
									<td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>

								   <!-- link 3 begins -->
									<td height="20" class="mainMenuItem" onmouseover="changeMenuStyle(this,'mainMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'mainMenuItem'),hideCursor()" >
										<a href="#" onclick="javascript:backToControlForm()" class="mainMenuLink"><bean:message key="app.title.BuildFormTabTitle" /></a>
								   </td>

								  <!-- link 3 ends -->
								   <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
								  <!-- link 4 begins -->
								   <td height="20" class="mainMenuItemSelected" >
									 <a href="#" class="mainMenuLink"><bean:message key="app.title.PreviewTabTitle" /></a>
								   </td>
								   <!-- link 4 ends -->
								   <td><img src="images/mainMenuSeparator.gif" width="1" height="16" alt="" /></td>
								</tr>
						 </table>
<!-- menu ends -->
				 </td>
			</tr>
			<tr>
			<td valign="top" >
			<table align='center' width='80%'  >
				<tr >
					<td align="center" class="formTitle">
						<logic:messagesPresent message="true">
									<html:messages message="true" id="msg">
										<bean:write name="msg" ignore="true"/>
									</html:messages>
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

									<c:out value="${entityInterface.name}" escapeXml="false" />
								</td>
							</tr>


						<c:forEach items="${containerInterface.controlCollection}" var="controlInterface">
						<jsp:useBean id="controlInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface" />
							<tr>
								<td class="formRequiredNotice" width="2%">
									&nbsp;
								</td>
								<td class="formRequiredLabel" width="20%">
									<c:out value="${controlInterface.caption}"/>
								</td>
								<td class="formField">
									<% String generateHTMLStr = controlInterface.generateHTML(); %>
									<% pageContext.setAttribute("generateHTMLStr", generateHTMLStr); %>
									<c:out value="${generateHTMLStr}" escapeXml="false" />
								</td>
							</tr>
						</c:forEach>

                       	</table>
                	</td>
                </tr>
            </table>
            </td></tr>
			<tr><td>
            <table cellpadding="4" cellspacing="5" border="0"  align='center'>
			    <tr height="5">
			    </tr>
				<tr>
					<td align='left'>
						<html:button property="backToPrevious"  styleClass="actionButton" onclick="backToControlForm()">
							<bean:message  key="buttons.backToPrevious" />
						</html:button>
					</td>
					<td align='right'>
						<html:submit styleClass="actionButton"  onclick="addDynamicData()">
							<bean:message  key="buttons.submit" />
						</html:submit>
					</td>
				</tr>
			</table>
			</td></tr>
		</table>
		</html:form>
	</body>
</html>