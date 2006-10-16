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
		<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />
		 <tr>
			<td class="formRequiredLabel">
				<bean:message key="eav.att.SourceForValues"/>
			</td>
			<td class="formField">
				<html:select property="displayChoice"  onchange="changeSourceForValues(this)" >
					<html:options collection="displayChoiceList" labelProperty="name" property="value" />
				</html:select>
			</td>
			<html:hidden property="choiceList" />
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
<jsp:include page="/pages/toolTypeHTML/Datatypes.jsp" />
<jsp:include page="/pages/toolTypeHTML/ListValuesSpecification.jsp" />














