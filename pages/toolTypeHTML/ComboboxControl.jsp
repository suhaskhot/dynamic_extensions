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

			<td class="formRequiredLabelWithoutBorder">
				<bean:message key="eav.att.ListBoxType"/>
			</td>
			<td class="formFieldWithoutBorder">
					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_SINGLE_SELECT%>" onchange="listTypeChanged(this)" >
						<bean:message key="eav.att.ListBoxSingleTitle"/>
					</html:radio>

					<html:radio property="attributeMultiSelect" value="<%=ProcessorConstants.LIST_TYPE_MULTI_SELECT%>" onchange="listTypeChanged(this)" disabled="true">
						<bean:message key="eav.att.ListBoxMultiLineTitle"/>
					</html:radio>
			</td>
		</tr>
		<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">
								&nbsp;
					</td>

				<td class="formRequiredLabelWithoutBorder" disabled="true">
						<bean:message key="eav.att.ListBoxDisplayLines"/>
				</td>
						<% if(listType.equalsIgnoreCase(ProcessorConstants.LIST_TYPE_SINGLE_SELECT)) { %>
				<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized" value='' disabled='true' maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% }  else {
						%>
							<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"   maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
						<% } %>
		</tr>

		 <tr>
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
						<html:radio property="dataType" value="<%=dataTypeValue%>" onclick="listDataTypeChanged(this)" >
							<c:out value="${dataTypeObj.name}"/>
						</html:radio>

				</c:forEach>
			</td>
			<html:hidden property="choiceList" />
		</tr>


		<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					 			&nbsp;
		 	</td>
			<td class="formRequiredLabelWithoutBorder" ><bean:message key="eav.att.ListBoxOptionTypes"></bean:message></td>
			<td  class="formFieldWithoutBorder">
				<c:forEach items="${displayChoiceList}" var="choiceType">
					<jsp:useBean id="choiceType" type="edu.wustl.common.beans.NameValueBean" />
						<c:set var="choiceTypeValue" value="${choiceType.value}" />
						<jsp:useBean id="choiceTypeValue" type="java.lang.String" />
						<html:radio property="displayChoice" value="<%=choiceTypeValue%>">
							<c:out value="${choiceType.name}"/>
						</html:radio>

				</c:forEach>
			</td>
		</tr>
		<tr>
			<td class="formRequiredNoticeWithoutBorder" valign="top" width="2%">
								&nbsp;
			</td>
			<td class="formRequiredLabelWithoutBorder" valign="top">&nbsp;</td>
			<td valign="top">
				<input type="hidden" value="1" name="choiceListCounter" >
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
	<tr>
			<td class="formRequiredNoticeWithoutBorder" width="2%">
					&nbsp;
			</td>
			<td class="formRequiredLabelWithoutBorder" width="25%">&nbsp;</td>
			<td class="formFieldWithoutBorder" align="left">
				<html:checkbox property="attributeIdentified" value="true"><bean:message key="app.att.isIdentified" /></html:checkbox>
			</td>
	</tr>
	 </table>
	</td>
 </tr>
 </tr>
		
</table>

