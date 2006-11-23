<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.ui.webui.util.OptionValueObject"%>

<div id="UserDefinedValues" style="display:none">
	<input type="hidden" value="1" id ="choiceListCounter"  name="choiceListCounter" >
	<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>

	 <tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%"><bean:message key="eav.att.OptionLabel"/></td>
				<td  class="formFieldWithoutBorder">
					<input type="text"  id = 'optionName' name="optionName" class="formDateSized" value='' maxlength="100" size="60" >
				</td>
	</tr>

	<tr valign="top" >
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="25%"><bean:message key="eav.att.OptionConceptCode"/></td>
					<td  class="formFieldWithoutBorder">
						<input type="text" id = 'optionConceptCode' name="optionConceptCode" class="formDateSized" value='' maxlength="100" size="60" >
					</td>
	</tr>

	<tr valign="top" >
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder"><bean:message key="eav.att.OptionDescription"/></td>
					<td  class="formFieldWithoutBorder">
						<textarea rows = "3" cols="28" id = 'optionDescription' name="optionDescription" class="formFieldSmallSized" ></textarea>
					</td>
	</tr>
	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="25%">&nbsp;</td>
					<td  class="formFieldWithoutBorder" align="left" >
							<html:button styleClass="actionButton" styleId = 'addChoiceValue'  property="addChoiceValue" onclick="addChoiceToList(true)" >
									<bean:message  key="eav.button.AddOption" />
							</html:button>
					</td>
	</tr>
	<tr valign="top" id="optionsListRow" style="display:block;" >
		<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder" width="25%"><bean:message key="eav.att.OptionList"/></td>
		<td  class="formFieldWithoutBorder">

				<table summary="" cellpadding="0" cellspacing="0" border="0" align = 'center' valign="top" width='100%'>
					<tr  class="formRequiredLabelWithoutBorder">
						<td class="formRequiredLabelWithoutBorder" >
							<div style="padding : 1px;  height : 50px; width:'80%'; overflow : auto; " >
							<table id="choiceListTable" summary="" cellpadding="0" cellspacing="0"  width="100%" class="tbBordersAllbordersBlack">

								<c:forEach items="${controlsForm.optionDetails}" var="optionDetail"  varStatus="elements">
									<jsp:useBean id="optionDetail" type="edu.common.dynamicextensions.ui.webui.util.OptionValueObject" />

									<c:set var="optionName" value="${optionDetail.optionName}"/>
								    <jsp:useBean id="optionName" type="java.lang.String"/>

									<c:set var="optionConceptCode" value="${optionDetail.optionConceptCode}"/>
								    <jsp:useBean id="optionConceptCode" type="java.lang.String"/>

									<c:set var="optionDesc" value="${optionDetail.optionDescription}"/>
								    <jsp:useBean id="optionDesc" type="java.lang.String"/>

								    <c:set var="rowId" value="${elements.count}"/>
								    <jsp:useBean id="rowId" type="java.lang.Integer"/>
								<tr id="<%=optionName%>" >
									<td  class="formFieldBottom" width="10%" >
										<input type='checkbox' id='chkBox<%=rowId%>' name='chkBox<%=rowId%>' id='chkBox<%=rowId%>' value="<%=optionName%>" >
										<%=optionName%>
									</td>
									<td>
										<input type='hidden' id='tempOptionNames' name='tempOptionNames' value='<%=optionName%>' >
										<input type='hidden' id='tempOptionDescriptions' name='tempOptionDescriptions' value='<%=optionDesc%>' >
										<input type='hidden' id='tempOptionConceptCodes' name='tempOptionConceptCodes' value='<%=optionConceptCode%>' >
									</td>
								</tr>
								</c:forEach>

							</table>
						</td>
					</tr>
					<tr valign="top">
						<td align="right" class="formRequiredLabelWithoutBorder" width="100%"  >
							<html:button styleClass="actionButton" property="btnSetDefaultValue"  onclick="setDefaultValue()">
									<bean:message  key="eav.button.SetDefaultOption" />
							 </html:button>
							 <html:button styleClass="actionButton" property="btnDeleteChoiceValue"  onclick="deleteElementsFromChoiceList()">
									<bean:message  key="eav.button.DeleteOption" />
							 </html:button>
						</td>
					</tr>
					<html:hidden styleId = 'attributeDefaultValue' property="attributeDefaultValue" />
				</table>
			</td>


	</tr>
	</table>
</div>

	<!--<div id="CADSRValues" style="display:none">
			<table summary="" cellpadding="1" cellspacing="0" border="1" align = 'center' width='100%'>
				<tr>
					<td class="formRequiredLabel"> Public Domain Id</td>
					<td class="formField"><input type="text" name="publicDomainId"></td>
				</tr>
		</table>
	</div>-->