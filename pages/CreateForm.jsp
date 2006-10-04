<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<title>Dynamic Extensions</title>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
</head>
 <c:set var="existingFormsList" value="${formDefinitionForm.existingFormsList}"/>
 <jsp:useBean id="existingFormsList" type="java.util.List"/>

 <c:set var="createAs" value="${formDefinitionForm.createAs}"/>
 <jsp:useBean id="createAs" type="java.lang.String"/>

 <html:form styleId = "formDefinitionForm" action="/ApplyFormDefinitionAction" >
  <body>
  	<html:errors />  
	 
	         <table align = 'center' width='100%' border='1'>
		          <tr height = 40>	<td>Form Definition</td>	</tr>
				  <tr>
		     		<td>
				  	 <table summary="" align = 'center' cellpadding="3" cellspacing="0" border="1">
					     <tr>
						  	 <td class="formMessage" colspan="3">* indicates a required field</td>
					     </tr>
					     <tr>
						     <td class="formTitle" height="20" colspan="3">
									<bean:message key="eav.new.form"/>
							 </td>
					     </tr>
					     <tr>
							<td class="formRequiredNotice" width="5">*</td>
							<td class="formRequiredLabel">
								<bean:message key="eav.form.name"/> 
							</td>
							<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="formName" />
							</td>
						</tr>
					    <tr>
							<td class="formRequiredNotice" width="5">&nbsp;</td>
							<td class="formRequiredLabel">
									<bean:message key="eav.form.description"/> 
						 	</td>
							<td class="formField">
									<html:textarea styleClass="formDateSized"  rows = "5" cols="40"  property="description" />
							</td>
					     </tr>
					     <tr>
							 <td class="formRequiredNotice" width="5">*</td>
							 <td class="formRequiredLabel">
								<bean:message key="eav.form.createAs"/> 
							 </td>
					      	 <td class="formField">
								<table border='0'>
									<tr class="formMessage">
									 <td >
										<html:radio value="NewForm" property="createAs"> New</html:radio>
										</td>
									</tr>
									<tr class="formMessage">
										<td >
											<html:radio value="ExistingForm" property="createAs" disabled="true">Existing</html:radio>
										</td>
										<td class="formField" width="5">
											 <html:select property="selectForm" onchange="formSelectedAction()" styleClass="formDateSized"  size="1"  styleId="selectForm" disabled="true">
										 	<html:options collection="existingFormsList" labelProperty="name" property="value"/>
											</html:select>
									</td>
								</tr>
							  </table>
						  </td>
					</table>
			 	</td>
			 </tr>
			 <tr height = 10>
			 	<td>
	 <table summary="" align = 'center' cellpadding="5" cellspacing="0" border="0">
		    <tr height="15">
		
							<td align="center">
					<html:submit styleClass="actionButton">
							<bean:message  key="buttons.save" />
					</html:submit>
				</td>
	
				<td>
					<html:reset styleClass="actionButton" property="cancelButton">
							<bean:message  key="buttons.cancel" />
					</html:reset>
				</td>	  <td width="65%"/>

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
		<html:hidden property="createAsTypeChanged" value=""/>
		<html:hidden property="entityIdentifier" value=""/>
	  </body>
   </html:form> 
