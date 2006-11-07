<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!--<c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
<jsp:useBean id="displayChoiceList" type="java.util.List"/>
-->

<div id="UserDefinedValues" style="display:none">
	<input type="hidden" value="1" name="choiceListCounter" >
	<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>

	 <tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder">Option Label</td>
				<td  class="formFieldWithoutBorder">
					<input type="text">
				</td>
	</tr>

	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder">Concept Code</td>
					<td  class="formFieldWithoutBorder">
						<input type="text">
					</td>
	</tr>

	<tr valign="top">
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder">Definition</td>
					<td  class="formFieldWithoutBorder">
						<input type="text">
					</td>
	</tr>

	<tr>
		<td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>
		<td class="formRequiredLabelWithoutBorder">&nbsp;</td>
		<td  class="formFieldWithoutBorder">
				<table summary="" cellpadding="0" cellspacing="0" border="0" align = 'center' valign="top" width='100%'>
					<THEAD>
						<tr valign="top">
							<th class="formRequiredLabelWithoutBorder" width="100%"  >
								<input type="text"  name="choiceValue" size="28">
								<input type="button" name="addChoiceValue" value="Add" onclick="addChoiceToList(true);">
							</th>
						</tr>
					</thead>
					<tr  class="formRequiredLabelWithoutBorder">
						<td class="formRequiredLabelWithoutBorder" >
							<div style="padding : 1px;  height : 100px; width:'85%'; overflow : auto; " >
							<table id="choiceListTable" summary="" cellpadding="0" cellspacing="0" border="1" >
								<tr>
									<th width="10%" class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.att.DefaultValue"/>
									</th>
									<th class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.att.values"/>
									</th>
									<th width="15%" class="formRequiredLabelWithoutBorder">
										<input type="button" name="deleteChoiceValue" value="Delete" onclick="deleteElementsFromChoiceList()" >
									</th>
								</tr>
							</table>
						</td>
					</tr>
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