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
<SCRIPT>var imgsrc="images/";</SCRIPT>
<LINK href="css/calanderComponent.css" type=text/css rel=stylesheet>
<html:hidden property="dataType" value="<%=ProcessorConstants.DATATYPE_STRING%>"/>

<table  summary="" align = 'center' width='100%'>

  <tr>
  	<td>
  		<table summary=""  align = 'center' width='100%'>
		   <tr>
				<td class="formRequiredNoticeWithoutBorder" width="2%">
					*
				</td>
				<td class="formRequiredLabelWithoutBorder" width="25%">
						<bean:message key="eav.att.DefaultValue"/>
				</td>

				<td class="formFieldWithoutBorder">
					<html:radio property="attributeDefaultValue" value="checked" >
						<bean:message key="eav.att.CheckedAttributeTitle"/>
					</html:radio>

					<html:radio property="attributeDefaultValue" value="unchecked">
						<bean:message key="eav.att.UnCheckedAttributeTitle"/>
					</html:radio>
				</td>
			</tr>

	</table>

  	</td>
  </tr>
</table>
