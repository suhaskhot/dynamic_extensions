<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
 <jsp:useBean id="dataTypeList" type="java.util.List"/>

 <c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
  <jsp:useBean id="displayChoiceList" type="java.util.List"/>


<table>
  <tr>
    <td>

	<table summary="" cellpadding="3" cellspacing="0" border="0" align = 'center' width='70%'>
		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Label"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="caption" />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Name"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="name" />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.DataInput"/>
			</td>

			<td class="formField">
					<html:select property="dataType"  onchange="changeDataType(this)" >
						<html:options collection="dataTypeList" labelProperty="name" property="value" />
					</html:select>
			</td>
		</tr>


		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Description"/>
			</td>

			<td class="formField">
					<html:textarea styleClass="formFieldSized"  rows = "5" cols="40"  property="description"  />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.CSSClassName"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="cssClass" />
			</td>
		</tr>


		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Tooltip"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="tooltip" />
			</td>
		</tr>
		 <tr>
			<td class="formRequiredLabel">
				<bean:message key="eav.att.SourceForValues"/>
			</td>
			<td class="formField">
				<html:select property="displayChoice"  onchange="changeSourceForValues(this)" >
					<html:options collection="displayChoiceList" labelProperty="name" property="value" />
				</html:select>
			</td>
		</tr>
	 </table>
	</td>
 </tr>

	<tr>
		<td>
			<div id="substitutionDiv">
			</div>

		</td>
	</tr>
</table>

<div id="UserDefinedValues" style="display:none">
		<input type="hidden" value="1" name="choiceListCounter" >
		<table>
			<tr>
				<td class="formRequiredLabel"><input type="text" name="choiceValue"></td>
				<td class="formField"> <input type="button" name="addChoiceValue" value="Add Value" onclick="addChoiceToList();"></td>
			</tr>
			<tr>
				<td colspan='2'>
					<table id="choiceList" border="1" width="100%" >
						<tr>
							<th width="20%"><input type="button" name="btnDelete" value="Delete" onclick="deleteElementsFromChoiceList()" ></th>
							<th ><bean:message key="eav.att.values"/></th>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<div id="CADSRValues" style="display:none">
			<table >
				<tr>
					<td class="formRequiredLabel"> Public Domain Id</td>
					<td class="formField"><input type="text" name="publicDomainId"></td>
				</tr>
		</table>
	</div>


		<div id="StringDataType" style="display:none">
			    <table>
			       <tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.TextFieldWidth"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributenoOfCols" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.MaxCharacters"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>
				 </table>
			</div>

			<div id="NumberDataType" style="display:none">
			    <table>
			       <tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeSize"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeDigits"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDigits" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeDecimalPlaces"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDecimalPlaces" />
						</td>
					</tr>


					<tr>
						<td class="formRequiredLabel">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeMeasurementUnits"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeMeasurementUnits" />
						</td>
					</tr>
				 </table>
			</div>
