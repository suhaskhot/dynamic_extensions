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

  <c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
  <jsp:useBean id="displayChoiceList" type="java.util.List"/>


<table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
  <tr>
    <td>

	<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
		<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />

		<tr valign="top">
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					 			&nbsp;
		 	</td>
			<td class="formRequiredLabelWithoutBorder" ><bean:message key="eav.att.RadioButtonOptionTypes"></bean:message></td>
			<td  class="formFieldWithoutBorder">
				<c:forEach items="${displayChoiceList}" var="choiceType">
					<jsp:useBean id="choiceType" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="choiceTypeValue" value="${choiceType.value}" />
						<jsp:useBean id="choiceTypeValue" type="java.lang.String" />
						<html:radio property="displayChoice" value="<%=choiceTypeValue%>" onchange="changeSourceForValues()">
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
</table>
<html:hidden property="dataType" value ="<%=ProcessorConstants.DATATYPE_STRING%>"/>

<jsp:include page="/pages/toolTypeHTML/ListValuesSpecification.jsp" />

