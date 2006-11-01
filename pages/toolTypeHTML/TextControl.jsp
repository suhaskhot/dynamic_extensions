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


<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%' class = "tbBordersAllbordersBlack" >
  <tr>
    <td>

	<table summary="" cellpadding="3"  cellspacing="0"  align = 'center' width='100%' >
		<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />

		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
		 			&nbsp;
		 	</td>
			<td class="formRequiredLabelWithoutBorder">
					<bean:message key="eav.att.TextFieldWidth"/>
			</td>

			<td class="formFieldWithoutBorder">
				<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributenoOfCols" />
			</td>
		</tr>
		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
				 			*
		 	</td>
			<td class="formRequiredLabelWithoutBorder">
					<bean:message key="eav.att.DataInput"/>
			</td>

			<!--<td class="formFieldWithoutBorder">
					<html:select property="dataType"  onchange="changeDataType(this)" >
						<html:options collection="dataTypeList" labelProperty="name" property="value" />
					</html:select>
			</td>-->
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
	<input type="hidden" name="initialDataType" value="<%=selectedDataType%>">
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
