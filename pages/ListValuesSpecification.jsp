<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<link rel="stylesheet" type="text/css" href="css/stylesheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

<c:set var="sourceValues" value="${requestScope.jspParams}"/>
 <jsp:useBean id="sourceValues" type="java.util.List"/>

<c:set var="displayChoice" value="${controlsForm.displayChoice}"/>
 <jsp:useBean id="displayChoice" type="java.lang.String"/>


<tr>
	<td >&nbsp;</td>
	<td >Source For Values</td>
	<td >
		<html:select property="displayChoice"  onchange="changeSourceForValues(this)" >
			<html:options collection="sourceValues" labelProperty="name" property="value" />
		</html:select>
	</td>
</tr>

<tr>
<td colspan='3'>
	<div id="UserDefinedValues" style="display:none">
		<input type="hidden" value="1" name="choiceListCounter" >
		<table cellspacing='3' cellpadding='3' border='0' align='center' width="80%">
			<tr>
				<td ><input type="text" name="choiceValue"></td>
				<td><input type="button" name="addChoiceValue" value="Add Value" onclick="addChoiceToList();"></td>
			</tr>
			<tr>
				<td colspan='2'>
					<table id="choiceList" border='3' width="80%">
						<tr>
							<th width="20%"><input type="button" name="btnDelete" value="Delete" onclick="deleteElementsFromChoiceList()" ></th>
							<th>Values</th>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<div id="CADSRValues" style="display:none">
			<table cellspacing='3' cellpadding='3' border='0' align='center'>
				<tr>
					<td>Public Domain Id</td>
					<td><input type="text" name="publicDomainId"></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
		</table>
	</div>
</td>
</tr>

