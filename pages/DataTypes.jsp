<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="dataTypes" value="${requestScope.jspParams}"/>
 <jsp:useBean id="dataTypes" type="java.util.List"/>

<c:set var="dataType" value="${controlsForm.dataType}"/>
 <jsp:useBean id="dataType" type="java.lang.String"/>


<tr>
	<td >&nbsp;</td>
	<td >Data Input</td>
	<td >
		<html:select property="dataType"  onchange="changeDataType(this)" >
			<html:options collection="dataTypes" labelProperty="name" property="value" />
		</html:select>
	</td>
</tr>

<tr>
<td colspan='3'>
	<div id="StringDataType" style="display:none">
		<table cellspacing='0' cellpadding='0' border='0' width='100%' align='center'>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Text Field Width</td>
				<td width='50%'><input type="text" name="attributenoOfCols" ></td>
			</tr>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Max Characters</td>
				<td width='50%'><input type="text" name="attributeSize" ></td>
			</tr>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Default Value </td>
				<td width='50%'><input type="text" name="attributeDefaultValue"> </td>
			</tr>
		</table>
	</div>

	<div id="NumberDataType" style="display:none">
			<table cellspacing='0' cellpadding='0' border='0' width='100%' align='center'>
				<tr>
					<td width='8%'>&nbsp;</td>
					<td width='35%'>Text Field Width</td>
					<td width='50%'><input type="text" name="attributeSize" ></td>
				</tr>
				<tr>
					<td width='8%'>&nbsp;</td>
					<td width='35%'>Digits</td>
					<td width='50%'><input type="text" name="attributeDigits" ></td>
				</tr>
				
				<tr>
					<td width='8%'>&nbsp;</td>
					<td width='35%'>Decimals</td>
					<td width='50%'><input type="text" name="attributeDecimalPlaces" ></td>
				</tr>
				
				<tr>
					<td width='8%'>&nbsp;</td>
					<td width='35%'>Default Value </td>
					<td width='50%'><input type="text" name="attributeDefaultValue" ></td>
				</tr>
				<tr>
					<td width='8%'>&nbsp;</td>
					<td width='35%'>Measurement Units </td>
					<td width='50%'><input type="text" name="attributeMeasurementUnits"> </td>
				</tr>

		</table>
	</div>

	<div id="DateDataType" style="display:none">
		<table cellspacing='0' cellpadding='0' border='0' width='100%' align='center'>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Default Value </td>
				<td width='50%'><input type="text" name="attributeDefaultValue" ></td>
			</tr>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Format </td>
				<td width='50%'><input type="text" name="attributeFormat"> </td>
			</tr>
		</table>
	</div>

	<div id="BooleanDataType" style="display:none">
		<table cellspacing='0' cellpadding='0' border='0' width='100%' align='center'>
			<tr>
				<td width='8%'>&nbsp;</td>
				<td width='35%'>Default Value </td>
				<td width='50%'>
					<select name="attributeDefaultValue">
						<option id="true" >true</option>
						<option id="false">false</option>
					</select>
				</td>
			</tr>
		</table>
	</div>
</td>
</tr>

