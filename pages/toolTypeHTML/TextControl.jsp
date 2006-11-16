<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<c:set var="selectedDataType" value="${controlsForm.dataType}"/>
<jsp:useBean id="selectedDataType" type="java.lang.String"/>


<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
  <tr>
    <td>

	<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
		<!--<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />-->

		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">
		 			&nbsp;
		 	</td>
			<td class="formRequiredLabelWithoutBorder" width="25%">
					<bean:message key="eav.att.TextFieldWidth"/>
			</td>

			<td class="formFieldWithoutBorder">
				<html:text styleClass="formFieldSized5"  maxlength="100" size="60"  property="attributenoOfCols" />
			</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">
				 			*
		 	</td>
			<td class="formRequiredLabelWithoutBorder" width="25%">
					<bean:message key="eav.att.DataInput"/>
			</td>

			<td class="formFieldWithoutBorder">
				<c:forEach items="${dataTypeList}" var="dataTypeObj">
					<jsp:useBean id="dataTypeObj" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="dataTypeValue" value="${dataTypeObj.value}" />
						<jsp:useBean id="dataTypeValue" type="java.lang.String" />
						<html:radio property="dataType" value="<%=dataTypeValue%>" onclick="dataFldDataTypeChanged(this)" >
							<c:out value="${dataTypeObj.name}"/>
						</html:radio>
				</c:forEach>
			</td>
		</tr>

	 </table>
	</td>
 </tr>
<input type="hidden" name="initialDataType" value="<%=selectedDataType%>">
	<tr valign="top">
		<td>
			<div id="substitutionDiv">
			</div>

		</td>
	</tr>
	<tr valign="top">
	<td>
		<table summary="" cellpadding="3" cellspacing="0"  align = 'center' valign="top">
		<!-- <tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					&nbsp;
			</td>
			<td class="formRequiredLabelWithoutBorder" width="25%">&nbsp;</td>
			<td class="formFieldWithoutBorder" align="left">
				<html:checkbox property="attributeIdentified" value="true"><bean:message key="app.att.isIdentified" /></html:checkbox>
			</td>
		</tr>
		-->
		<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
						&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%">&nbsp;</td>
				<td class="formFieldWithoutBorder" align="left">
					<html:checkbox  property="attributeIsPassword" value="true"><bean:message key="app.att.isPassword" /></html:checkbox>
				</td>
		</tr>
		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					&nbsp;
			</td>
			<td class="formRequiredLabelWithoutBorder" width="25%">&nbsp;</td>
			<td class="formFieldWithoutBorder" align="left">
				<html:checkbox  property="attributeDisplayAsURL" value="true">
					<bean:message key="app.att.displayAsURL" />
				</html:checkbox>
			</td>
		</tr>
		</table>
	</td>
	</tr>
	<tr>
		<td>
			<div id="substitutionDivRules">
			</div>

		</td>
	</tr>
</table>
<jsp:include page="/pages/toolTypeHTML/Datatypes.jsp" />
<jsp:include page="/pages/ValidationRules.jsp" />
