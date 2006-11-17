<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants" %>

<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />

<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>
<script src="jss/calendarComponent.js"></script>
<script>var imgsrc="images/";</script>
<link href="css/calanderComponent.css" type=text/css rel=stylesheet>

<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<html:hidden property="dataType" value="<%=ProcessorConstants.DATATYPE_DATE%>"/>

<table  summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>
	<tr>
  		<td>
  			<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%'>
		   		<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">*</td>
					
					<td class="formRequiredLabelWithoutBorder" width="25%">
						<bean:message key="eav.att.Format"/>
					</td>
	
					<td class="formFieldWithoutBorder">
						<html:radio property="format" value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEONLY%>" >
							<bean:message key="eav.att.DateFormatDateOnlyTitle"/>
						</html:radio>
	
						<html:radio property="format" value="<%=ProcessorConstants.DATE_FORMAT_OPTION_DATEANDTIME%>" disabled="true">
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
						<html:radio property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_NONE%>" onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueNone"/>
						</html:radio>
	
						<html:radio property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_TODAY%>" onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueToday"/>
						</html:radio>
						<html:radio property="dateValueType" value="<%=ProcessorConstants.DATE_VALUE_SELECT%>"  onclick="changeDateType(this)">
							<bean:message key="eav.att.DateValueSelect"/>
						</html:radio>
					</td>
				</tr>
				<tr>
					<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
					</td>
					<td class="formRequiredLabelWithoutBorder" width="25%">
						&nbsp;
					</td>
					<td class="formFieldWithoutBorder" >
						<html:text property='attributeDefaultValue'  styleClass="formFieldVerySmallSized" maxlength="100" size="60"></html:text>
						<A onclick="showCalendar('attributeDefaultValue',2006,10,26,'MM-dd-yyyy','controlsForm','attributeDefaultValue',event,1900,2020);" href="javascript://"><IMG alt="This is a Calendar" src="images/calendar.gif" border=0></A>
						<DIV id=slcalcodattributeDefaultValue style="Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px">
							<SCRIPT>printCalendar('attributeDefaultValue',26,10,2006);</SCRIPT>
						</DIV>
						[MM-DD-YYYY]&nbsp;
	
					</td>
				</tr>
			</table>
		</td>
  	</tr>

	<tr>
		<td>
			<div id="substitutionDivRules"></div>
		</td>
	</tr>
</table>
<jsp:include page="/pages/toolTypeHTML/Datatypes.jsp" />
<jsp:include page="/pages/ValidationRules.jsp" />
