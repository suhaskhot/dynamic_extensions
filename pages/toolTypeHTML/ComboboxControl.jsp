<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>
<%@page import="edu.wustl.common.beans.NameValueBean"%>
<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
 <jsp:useBean id="dataTypeList" type="java.util.List"/>

  <c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
  <jsp:useBean id="displayChoiceList" type="java.util.List"/>

  <c:set var="listType" value="${controlsForm.attributeMultiSelect}"/>
<jsp:useBean id="listType" type="java.lang.String"/>

<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
  <tr>
    <td>

	<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
		<!--<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />-->
		<!--<html:hidden property="displayChoice" value="UserDefined"/>-->
		<!--  <tr>
			<td class="formRequiredLabelWithoutBorder">
				<bean:message key="eav.att.SourceForValues"/>
			</td>
			<td class="formFieldWithoutBorder">
				<html:select property="displayChoice"  onchange="changeSourceForValues(this)" >
					<html:options collection="displayChoiceList" labelProperty="name" property="value" />
				</html:select>
			</td>
			<html:hidden property="choiceList" />
		</tr> -->
		 <tr>
		 	<td class="formRequiredNoticeWithoutBorder" width="2%">
		 				*
		 	</td>

			<td class="formRequiredLabelWithoutBorder" width="25%">
				<bean:message key="eav.att.ListBoxType"/>
			</td>
			<td class="formFieldWithoutBorder">
					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_SINGLE_SELECT%>" onclick="listTypeChanged(this)" >
						<bean:message key="eav.att.ListBoxSingleTitle"/>
					</html:radio>

					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_MULTI_SELECT%>" onclick="listTypeChanged(this)" >
						<bean:message key="eav.att.ListBoxMultiLineTitle"/>
					</html:radio>
			</td>
		</tr>
		<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">
								&nbsp;
					</td>


						<% if(listType.equalsIgnoreCase(ProcessorConstants.LIST_TYPE_SINGLE_SELECT)) { %>
						<td class="formRequiredLabelWithoutBorder" disabled="true" id="lblNumberOfRows">
							<bean:message key="eav.att.ListBoxDisplayLines"/>
						</td>
						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized" value='' disabled='true' maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% }  else {
						%>
						<td class="formRequiredLabelWithoutBorder"  id="lblNumberOfRows">
							<bean:message key="eav.att.ListBoxDisplayLines"/>
						</td>
							<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"   maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% } %>
		</tr>

		<!--  <tr>
		 	<td class="formRequiredNoticeWithoutBorder" width="2%">
		 			*
		 	</td>

			<td class="formRequiredLabelWithoutBorder">
				<bean:message key="eav.att.DataInput"></bean:message>
			</td>
			<td class="formFieldWithoutBorder">
				<c:forEach items="${dataTypeList}" var="dataTypeObj">
					<jsp:useBean id="dataTypeObj" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="dataTypeValue" value="${dataTypeObj.value}" />
						<jsp:useBean id="dataTypeValue" type="java.lang.String" />
						<html:radio property="dataType" value="" onclick="listDataTypeChanged(this)" >
							<c:out value="${dataTypeObj.name}"/>
						</html:radio>

				</c:forEach>
			</td>

		</tr>
		 -->

		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					 			&nbsp;
		 	</td>
			<td class="formRequiredLabelWithoutBorder" width="25%"><bean:message key="eav.att.ListBoxOptionTypes"></bean:message></td>
			<td  class="formFieldWithoutBorder">
				<c:forEach items="${displayChoiceList}" var="choiceType">
					<jsp:useBean id="choiceType" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="choiceTypeValue" value="${choiceType.value}" />
						<jsp:useBean id="choiceTypeValue" type="java.lang.String" />
						<html:radio property="displayChoice" value="<%=choiceTypeValue%>" onchange="changeSourceForValues">
							<c:out value="${choiceType.name}"/>
						</html:radio>

				</c:forEach>
			</td>
		</tr>


	 </table>
	</td>
 </tr>
 <tr>
 	<td valign="top">
 		<div id="optionValuesSpecificationDiv">

 		</div>
 	</td>
 <tr>
 <html:hidden property="dataType" value ="<%=ProcessorConstants.DATATYPE_STRING%>"/>
</table>
<jsp:include page="/pages/toolTypeHTML/ListValuesSpecification.jsp" />

