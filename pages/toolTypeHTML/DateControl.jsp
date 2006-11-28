<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants" %>
<%@page import="edu.common.dynamicextensions.util.DynamicExtensionsUtility" %>
<%@page import="java.util.Date" %>

<SCRIPT>var imgsrc="images/";</SCRIPT>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<c:set var="dateValueType" value="${controlsForm.dateValueType}"/>
<jsp:useBean id="dateValueType" type="java.lang.String"/>



<html:hidden styleId = 'dataType' property="dataType" value="<%=ProcessorConstants.DATATYPE_DATE%>"/>

<table  summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
  <tr>
  	<td>
  		<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%'>
		   <tr>
				<td class="formRequiredNoticeWithoutBorder" width="2%">
					*
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%">
						<bean:message key="eav.att.Format"/>
				</td>

				<td class="formFieldWithoutBorder">
					<html:radio styleId = 'format' property="format" value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY%>" >
						<bean:message key="eav.att.DateFormatDateOnlyTitle"/>
					</html:radio>

					<html:radio styleId = 'format' property="format" value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME%>" disabled="true">
						<bean:message key="eav.att.DateFormatDateAndTimeTitle"/>
					</html:radio>
				</td>
			</tr>
			<tr>
			   <td class="formRequiredNoticeWithoutBorder" width="2%">
						&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%">
					<bean:message key="eav.att.DefaultValue"/>
				</td>
				<td class="formFieldWithoutBorder">
					<input type="hidden" id = 'initialDateValueType' name="initialDateValueType" value="<%=dateValueType%>">
					<html:radio styleId= 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_NONE%>" onclick="changeDateType(this)">
						<bean:message key="eav.att.DateValueNone"/>
					</html:radio>

					<html:radio styleId = 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_TODAY%>" onclick="changeDateType(this)">
						<bean:message key="eav.att.DateValueToday"/>
					</html:radio>
					<html:radio styleId = 'dateValueType' property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_SELECT%>"  onclick="changeDateType(this)">
						<bean:message key="eav.att.DateValueSelect"/>
					</html:radio>
				</td>
			</tr>
			<tr id="rowForDateDefaultValue">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%">
					&nbsp;
				</td>
				<td class="formFieldWithoutBorder" >
					<html:text styleId = 'attributeDefaultValue' property='attributeDefaultValue'  styleClass="formFieldVerySmallSized" maxlength="100" size="60"></html:text>
					<A onclick="showCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentDay()%>,'MM-dd-yyyy','controlsForm','attributeDefaultValue',event,1900,2020);" href="javascript://"><IMG alt="This is a Calendar" src="images/calendar.gif" border=0></A>
					<DIV id=slcalcodattributeDefaultValue style="Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px">
						<SCRIPT>printCalendar('attributeDefaultValue',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
					</DIV>
					[MM-DD-YYYY]&nbsp;
				</td>
			</tr>
	</table>

  	</td>
  </tr>


</tr>


</table>
