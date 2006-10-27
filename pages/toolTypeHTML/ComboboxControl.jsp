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
		<jsp:include page="/pages/toolTypeHTML/CommonControlAttributes.jsp" />
		<html:hidden property="displayChoice" value="UserDefined"/>
		<!--  <tr>
			<td class="formRequiredLabel">
				<bean:message key="eav.att.SourceForValues"/>
			</td>
			<td class="formField">
				<html:select property="displayChoice"  onchange="changeSourceForValues(this)" >
					<html:options collection="displayChoiceList" labelProperty="name" property="value" />
				</html:select>
			</td>
			<html:hidden property="choiceList" />
		</tr> -->
		 <tr>
		 	<td class="formRequiredNotice" width="2%">
		 				*
		 	</td>

			<td class="formRequiredLabel">
				<bean:message key="eav.att.ListBoxType"/>
			</td>
			<td class="formField">
					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_SINGLE_SELECT%>" onchange="listTypeChanged(this)" >
						<bean:message key="eav.att.ListBoxSingleTitle"/>
					</html:radio>

					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_MULTI_SELECT%>" onchange="listTypeChanged(this)" disabled="true">
						<bean:message key="eav.att.ListBoxMultiLineTitle"/>
					</html:radio>
			</td>
		</tr>
		<tr>
					<td class="formRequiredNotice" width="2%">
								&nbsp;
					</td>

				<td class="formRequiredLabel" disabled="true">
						<bean:message key="eav.att.ListBoxDisplayLines"/>
				</td>
						<% if(listType.equalsIgnoreCase(ProcessorConstants.LIST_TYPE_SINGLE_SELECT)) { %>
				<td class="formField">
								<html:text styleClass="formDateSized" value='' disabled='true' maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% }  else {
						%>
							<td class="formField">
								<html:text styleClass="formDateSized"   maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% } %>
		</tr>

		 <tr>
		 	<td class="formRequiredNotice" width="2%">
		 			*
		 	</td>

			<td class="formRequiredLabel">
				<bean:message key="eav.att.DataInput"></bean:message>
			</td>
			<td class="formField">
				<c:forEach items="${dataTypeList}" var="dataTypeObj">
					<jsp:useBean id="dataTypeObj" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="dataTypeValue" value="${dataTypeObj.value}" />
						<jsp:useBean id="dataTypeValue" type="java.lang.String" />
						<html:radio property="dataType" value="<%=dataTypeValue%>" onclick="listDataTypeChanged(this)" >
							<c:out value="${dataTypeObj.name}"/>
						</html:radio>

				</c:forEach>
			</td>
			<html:hidden property="choiceList" />
		</tr>


		<tr>
			<td class="formRequiredNotice" width="2%">
					 			&nbsp;
		 	</td>
			<td class="formRequiredLabel"><bean:message key="eav.att.ListBoxOptionTypes"></bean:message></td>
			<td>
				<input type="hidden" value="1" name="choiceListCounter" >
					<table summary="" cellpadding="0" cellspacing="0" border="0" align = 'center' width='100%'>
						<tr>
							<td class="formRequiredLabel" width="100%" align="right" >
								<input type="text" class="formDateSized" name="choiceValue" maxlength="100" size="60" >
								<input type="button" name="addChoiceValue" value="Add" onclick="addChoiceToList(true);">
							</td>
						</tr>
						<tr class="formRequiredLabel">
							<td class="formRequiredLabel" >
								<table id="choiceListTable" summary="" cellpadding="0" cellspacing="0" border="1" width="100%">
									<tr>

										<th width="10%" class="formLabel">
											<bean:message key="eav.att.DefaultValue"/>
										</th>
										<th class="formLabel">
											<bean:message key="eav.att.values"/>
										</th>
										<th width="15%" class="formLabel">
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
	</td>
 </tr>
</table>

