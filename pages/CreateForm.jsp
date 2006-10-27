<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : Deepti Shelar--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
 <%@ page import="java.util.Iterator"%>
  <%@ page import="java.util.List"%>
<html>
<head>
	<title>Dynamic Extensions</title>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
</head>


<c:set var="createAs" value="${formDefinitionForm.createAs}"/>
<jsp:useBean id="createAs" type="java.lang.String"/>


<html:form styleId = "formDefinitionForm" action="/ApplyFormDefinitionAction" >
  <body>
  	<html:errors />

         <table align = 'center' width='50%' border='0'>
         <tr><td/></tr>
         <tr><td/></tr>
	          <tr>
	     		<td>
			  	 <table summary="" align = 'center' cellpadding="3" cellspacing="0" border="0">
					<tr >
						<td class="formTitle" align='left' colspan="3" >
							 <bean:message key="app.CreateFormTitle"/>
						 </td>
			   		</tr>
			   		<tr><td/></tr>
			   		<tr><td/></tr>
				     <tr>
					  	 <td class="formMessage" colspan="3">
						  	 <bean:message key="app.requiredMessage"/>
					  	 </td>
				     </tr>
					  <tr class='formMessage' align='left'>
						  <%
						  List errorsList =(List) request.getAttribute("errorsList");
						  if(errorsList != null ) { %>
								  <td>Errors : </td> <td>
								  <%
											Iterator iter = errorsList.iterator();
											while(iter.hasNext()) {
												String errormsg = (String) iter.next();
													if(errormsg != null) {
									%>
										<%= errormsg%>
									<% }} }
							%>
						</td>
					</tr>
				     <tr>
					     <td class="formTitle" colspan="3">
								<bean:message key="eav.new.form"/>
						 </td>
				     </tr>
				    <!-- <tr>
						<td class="formRequiredNotice" width="5%">*</td>
						<td class="formRequiredLabel">
							<bean:message key="eav.group.title"/>
						</td>
						<td class="formField">
							<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="formName" />
						</td>
					</tr>-->
					 <tr>
						<td class="formRequiredNotice" width="5%">*</td>
						<td class="formRequiredLabel">
							<bean:message key="eav.form.title"/>
						</td>
						<td class="formField">
							<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="formName" />
						</td>
					</tr>
					 <tr>
					 <td class="formRequiredNotice" >*</td>
						<td class="formRequiredLabel">
							<bean:message key="eav.form.conceptCode"/>
						</td>
						<td class="formField">
							<html:text styleClass="formDateSized"  maxlength="20" size="40"  property="conceptCode" />
						</td>
					</tr>
				    <tr>
						<td class="formRequiredNotice" >&nbsp;</td>
						<td class="formRequiredLabel">
								<bean:message key="eav.form.description"/>
					 	</td>
						<td class="formField">
								<html:textarea styleClass="formDateSized"  rows = "3" cols="40"  property="formDescription" />
						</td>
				     </tr>
				      <tr>
							<td class="formRequiredNotice" >*</td>
						 <td class="formRequiredLabel">
							<bean:message key="eav.form.createAs"/>
						 </td>
				      	 <td class="formField">
							<table border='0'>
								<tr class="formMessage">
									 <td >
										<html:radio property="createAs" value="NewForm">
											<bean:message key="eav.createnewentity.title"/>
										</html:radio>

										<html:radio property="createAs" value="ExistingForm">
											<bean:message key="eav.existingentity.title"/>
										</html:radio>
									</td>
								</tr>
					 		</table>
			 		 	</td>
			 		 </tr>
			</table>
	 	</td>
	 </tr>
	 <tr >
	 	<td>
		 <table summary="" align = 'center' cellpadding="5" cellspacing="0" border="0">
	    	<tr height="15">
				<td align="center">
					<html:submit styleClass="actionButton">
						<bean:message  key="buttons.save" />
					</html:submit>
				</td>
				<td>
					<html:button styleClass="actionButton" property="cancelButton" onclick="showHomePageFromCreateForm()">
						<bean:message  key="buttons.cancel" />
					</html:button>
				</td>
				<td width="60%"/>
				<td align="right">
					<html:button styleClass="actionButton" property="nextButton" onclick="showBuildFormJSP()" >
						<bean:message  key="buttons.next" />
					</html:button>
				</td>
		</table>
 	</td>
 	 </tr>
 </table>
	<html:hidden property="operation" value=""/>
	<html:hidden property="entityIdentifier" value=""/>
  </body>
</html:form>
</html>
