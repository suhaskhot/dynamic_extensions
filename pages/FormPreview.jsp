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
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<c:set var="containerInterface" value="${previewForm.containerInterface}"/>
<jsp:useBean id="containerInterface" type="edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface"/>

<html>
	<head>
		<title><bean:message key="table.heading" /></title>
	</head>

	<body onload="loadPreviewForm()">
		<html:form styleId = "previewForm" action="/ApplyDataEntryFormAction" enctype="multipart/form-data" >
			<html:hidden property="entitySaved" />
			<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

			<table align='center' width='80%'  >
				<tr class="formTitle">
					<td align="center">
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
						<html:submit styleClass="actionButton" >
							<bean:message  key="buttons.submit" />
						</html:submit>
					</td>
				</tr>
			</table>
		</html:form>
	</body>

</html>