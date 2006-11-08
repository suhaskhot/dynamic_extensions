<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!--<c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
<jsp:useBean id="displayChoiceList" type="java.util.List"/>
-->

<div id="UserDefinedValues" style="display:none">
	<input type="hidden" value="1" name="choiceListCounter" >
	<table summary="" valign="top" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
<html:hidden property="choiceList" />
	 <tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder"><bean:message key="eav.att.OptionLabel"/></td>
				<td  class="formFieldWithoutBorder">
					<input type="text" name="optionName" class="formDateSized" value='' maxlength="100" size="60" >
				</td>
	</tr>

	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder"><bean:message key="eav.att.OptionConceptCode"/></td>
					<td  class="formFieldWithoutBorder">
						<input type="text" name="optionConceptCode" class="formDateSized" value='' maxlength="100" size="60" >
					</td>
	</tr>

	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder"><bean:message key="eav.att.OptionDescription"/></td>
					<td  class="formFieldWithoutBorder">
						<textarea rows = "3" cols="28" name="optionDescription" class="formDateSized" ></textarea>
					</td>
	</tr>
	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder">&nbsp;</td>
					<td  class="formFieldWithoutBorder">
							<input type="button" name="addChoiceValue" value="Add Option" onclick="addChoiceToList(true);">
					</td>
	</tr>
	<tr valign="top">
		<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder">&nbsp;</td>
		<td  class="formFieldWithoutBorder">
				<table summary="" cellpadding="0" cellspacing="0" border="0" align = 'center' valign="top" width='100%'>
					<!-- <THEAD>
						<tr valign="top">
							<th class="formRequiredLabelWithoutBorder" width="100%"  >
							<!--<input type="text"  name="choiceValue" size="28">
								<input type="button" name="addChoiceValue" value="Add" onclick="addChoiceToList(true);">
							</th>
						</tr>
					</thead>-->
					<tr  class="formRequiredLabelWithoutBorder">
						<td class="formRequiredLabelWithoutBorder" >
							<div style="padding : 1px;  height : 50px; width:'95%'; overflow : auto; " >
							<table id="choiceListTable" summary="" cellpadding="0" cellspacing="0" border="1" width="100%">
								<!--<tr>
									<td width="10%" class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.att.DefaultValue"/>
									</th>
									<td class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.att.values"/>
									</th>
									<td width="15%" class="formRequiredLabelWithoutBorder">
										<input type="button" name="deleteChoiceValue" value="Delete" onclick="deleteElementsFromChoiceList()" >
									</th>
								</tr>-->
							</table>
						</td>
					</tr>
					<tr valign="top">
						<td align="left" class="formRequiredLabelWithoutBorder" width="100%"  >
							<input type="button" name="btnSetDefaultValue" value="Make Default" onclick="setDefaultValue()">
							<input type="button" name="btnDeleteChoiceValue" value="Delete" onclick="deleteElementsFromChoiceList()" >
						</td>
					</tr>
					<html:hidden property="attributeDefaultValue" />
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