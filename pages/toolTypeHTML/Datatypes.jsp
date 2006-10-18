<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants" %>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

		<div id="StringDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" border="1" align = 'center' width='100%'>
			       	<tr>
						<td class="formRequiredLabel" width="25%">
								<bean:message key="eav.att.MaxCharacters"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" /> 
						</td>
					</tr>
				 </table>
			</div>

			<div id="NumberDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" border="1" align = 'center' width='100%'>
			       <tr>
						<td class="formRequiredLabel" width="25%">
								<bean:message key="eav.att.AttributeSize"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeDigits"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDigits" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeDecimalPlaces"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDecimalPlaces" />
						</td>
					</tr>


					<tr>
						<td class="formRequiredLabel">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
							<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.AttributeMeasurementUnits"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeMeasurementUnits" />
						</td>
					</tr>
				 </table>
			</div>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
			<div id="DateDataType" style="display:none">
			   <table summary="" cellpadding="3" cellspacing="0" border="1" align = 'center' width='100%'>
			       <tr>
						<td class="formRequiredLabel" width="25%">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
							<html:text styleClass="formDateSized" styleId="attributeDefaultValue"  maxlength="100" size="60"  property="attributeDefaultValue" />

	                       <a href="javascript:show_calendar('getElementById(\'attributeDefaultValue\')',null,null,'<%=ProcessorConstants.DATE_FORMAT%>');">
	                        <img src='images\\calendar.gif' width=24 height=22 border=0/><%=ProcessorConstants.DATE_FORMAT%></a>
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabel">
								<bean:message key="eav.att.Format"/>
						</td>

						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="format" />
						</td>
					</tr>
				 </table>
			</div>
			<div id="BooleanDataType" style="display:none">
			   <table summary="" cellpadding="3" cellspacing="0" border="1" align = 'center' width='100%'>
				   <tr>
						<td class="formRequiredLabel" width="25%">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formField">
							<html:select property="attributeDefaultValue" >
								<html:option  value="true">true</html:option>
								<html:option  value="false">false</html:option>
							</html:select>
						</td>
					</tr>

				 </table>
			</div>