<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.ui.webui.util.OptionValueObject"%>
<%@page import="java.util.List"%>

<c:set var="groupNamesList" value="${controlsForm.groupNames}"/>
<jsp:useBean id="groupNamesList" type="java.util.List"/>

<c:set var="selectedAttributes" value="${controlsForm.selectedAttributes}"/>
<jsp:useBean id="selectedAttributes" type="java.util.List"/>

<c:set var="separatorList" value="${controlsForm.separatorList}"/>
<jsp:useBean id="separatorList" type="java.util.List"/>


<script language="JavaScript" type="text/javascript" src="jss/ajax.js"></script>

<!--User defined values specification-->
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>
<div id="UserDefinedValues" style="display:none">
	<input type="hidden" value="1" id ="choiceListCounter"  name="choiceListCounter" >
	<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<hr/>
	 <tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="30%"><bean:message key="eav.att.OptionLabel"/> : </td>
				<td  class="formFieldWithoutBorder">
					<input type="text"  id = 'optionName' name="optionName" class="formDateSized" value='' maxlength="100" size="60" >
				</td>
	</tr>

	<tr valign="top" >
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="30%"><bean:message key="eav.att.OptionConceptCode"/> : </td>
					<td  class="formFieldWithoutBorder">
						<input type="text" id = 'optionConceptCode' name="optionConceptCode" class="formDateSized" value='' maxlength="100" size="60" >
					</td>
	</tr>

	<tr valign="top" >
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message key="eav.att.OptionDescription"/> :
					</td>
					<td  >
						<textarea rows = "2" cols="28" id = 'optionDescription' name="optionDescription" class="formFieldSmallSized" ></textarea>
					</td>
	</tr>
	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">&nbsp;</td>
					<td  class="formFieldWithoutBorder" align="left" >
							<html:button styleClass="actionButton" styleId = 'addChoiceValue'  property="addChoiceValue" onclick="addChoiceToList(true)" >
									<bean:message  key="eav.button.AddOption" />
							</html:button>
					</td>
	</tr>
	<tr valign="top" id="optionsListRow" >
		<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder" width="30%"><bean:message key="eav.att.OptionList"/> : </td>
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
<!--CDE Spcfn-->
	<div id="CDEValues" style="display:none">
			<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
				<hr/>
				<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
					<td class="formRequiredLabelWithoutBorder" width="30%">
						<bean:message  key="eav.att.CDEPublicDomainId" /> :
					</td>
					<td class="formFieldWithoutBorder">
						<html:text styleId="publicDomainId"  property="publicDomainId" styleClass="formDateSized" > </html:text>
					</td>
				</tr>
		</table>
	</div>
<!--Lookup specification-->
	<div id="LookupValues" style="display:none">
		<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
			<hr/>
			 <tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
				<td class="formRequiredLabelWithoutBorder" width="30%">
					<bean:message  key="eav.att.LookupFormTypeSelection" /> :
				</td>
				<td  class="formFieldWithoutBorder">
					<html:radio styleId="formTypeForLookup"  property="formTypeForLookup" value="<%=ProcessorConstants.LOOKUP_USER_FORMS %>" >
						<bean:message  key="eav.att.LookupUserForms" />
					</html:radio>
					<html:radio styleId="formTypeForLookup" property="formTypeForLookup" value="<%=ProcessorConstants.LOOKUP_SYSTEM_FORMS %>"  disabled="true">
						<bean:message  key="eav.att.LookupSytsemForms" />
					</html:radio>
				</td>
			</tr>

			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="30%">
					<bean:message  key="eav.att.Group" /> :
				</td>
				<td >
					<html:select styleClass="formFieldVerySmallSized" property="groupName" styleId="groupName" onchange="groupChanged(true)">
						<html:options collection="groupNamesList" labelProperty="name" property="value" />

					</html:select>
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="30%">
					<bean:message  key="eav.att.Form" /> :
				</td>
				<td  >
					<html:select styleClass="formFieldVerySmallSized" property="formName" styleId="formName" onchange="formChanged(true)">

					</html:select>
				</td>
			</tr>
			<tr valign="top">
				<td colspan="3">
					<table valign="top" align = 'center' width='100%'>
						<thead>
						<tr>
							<th  width="45%" class="formRequiredLabelWithoutBorder">
								Available Attributes
							</th>
							<th  width="10%" valign="middle" ALIGN="left" class="formMessage" >
								&nbsp;
							</th>
							<th  width="45%" class="formRequiredLabelWithoutBorder">
								Selected Attributes
							</th>
						</tr>
						</thead>
						<tr>
							<td  width="45%" >
								<select class="formFieldVerySmallSized" multiple size="3" id="formAttributeList" name="formAttributeList">
								</select>
							</td>
							<td  width="10%" valign="middle" ALIGN="center"  >
								<input type="button" name="addFormAttribute" value="Add" onclick="selectFormAttribute()" />
								<input type="button" name="removeFormAttribute" value="Remove" onclick="unSelectFormAttribute()" />
							</td>
							<td  width="45%" >
								<html:select multiple="true" size="3" property="selectedAttributeIds" styleId="selectedAttributeIds" styleClass="formFieldVerySmallSized">
									<html:options collection="selectedAttributes" labelProperty="name" property="value" />
								</html:select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
				<td class="formRequiredLabelWithoutBorder" width="30%">
					Separate with :
				</td>
				<td  >
					<html:select styleClass="formFieldVerySmallSized" property="separator" styleId="separator" >
						<html:options collection="separatorList" labelProperty="name" property="value" />
					</html:select>
				</td>
			</tr>
		</table>
	</div>
