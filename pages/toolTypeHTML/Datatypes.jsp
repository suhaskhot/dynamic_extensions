<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants" %>
<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>
<c:set var="linesType" value="${controlsForm.linesType}"/>
<jsp:useBean id="linesType" type="java.lang.String"/>

		<div id="TextDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>

			 <tr valign="top">
			 			<td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			*
			 		 	</td>
			 			<td class="formRequiredLabelWithoutBorder">
			 								<bean:message key="eav.control.type"/>
			 						</td>
			 			<td  class="formFieldWithoutBorder">
			 			<html:radio property='linesType' value='SingleLine' onclick='radioButtonClicked(this)'>Single</html:radio>
			 			<html:radio property='linesType' value='MultiLine' onclick='radioButtonClicked(this)'>Multi Line</html:radio>
			 			</td>
			 		</tr>
			 		<tr valign="top">
			 			<td class="formRequiredNoticeWithoutBorder" width="2%">
			 				&nbsp;
			 		 	</td>
			 						<% if(linesType.equalsIgnoreCase("SingleLine")) { %>
			 						<td class="formRequiredLabelWithoutBorder" id="noOfLines" disabled="true">
			 							<bean:message key="eav.text.noOfLines"/>
			 						</td>
			 				<td class="formFieldWithoutBorder">
			 								<html:text styleClass="formDateSized" value='' disabled='true' maxlength="100" size="60"  property="attributeNoOfRows" />
			 						</td>
			 						<% }  else {
			 						%>
			 							<td class="formRequiredLabelWithoutBorder" id="noOfLines">
			 								<bean:message key="eav.text.noOfLines"/>
			 							</td>

			 							<td class="formFieldWithoutBorder">
			 								<html:text styleClass="formDateSized"  disabled='false' maxlength="100" size="60"  property="attributeNoOfRows" />
			 						</td>
			 						<% } %>
					</tr>
			       	<tr valign="top">
			       			<td class="formRequiredNoticeWithoutBorder" width="2%">
									 		&nbsp;
		 					</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.MaxCharacters"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr valign="top">
						<td class="formRequiredNoticeWithoutBorder" width="2%">
									 			&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder">
								<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>
				 </table>
			</div>

			<div id="NumberDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%'>
			       <!--<tr>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.AttributeSize"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.AttributeDigits"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDigits" />
						</td>
					</tr>
-->
					<tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
								&nbsp;
						</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.AttributeDecimalPlaces"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDecimalPlaces" />
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
							<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>

					<tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
														 			&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.AttributeMeasurementUnits"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeMeasurementUnits" />
						</td>
					</tr>
				 </table>
			</div>
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>
			<div id="DateDataType" style="display:none">
			   <table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%'>
			       <tr>
			    	   <td class="formRequiredNoticeWithoutBorder" width="2%">
				   									 			&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formFieldWithoutBorder">
							<html:text styleClass="formDateSized" styleId="attributeDefaultValue"  maxlength="100" size="60"  property="attributeDefaultValue" />

	                       <a href="javascript:show_calendar('getElementById(\'attributeDefaultValue\')',null,null,'<%=ProcessorConstants.DATE_ONLY_FORMAT%>');">
	                        <img src='images\\calendar.gif' width=24 height=22 border=0/><%=ProcessorConstants.DATE_ONLY_FORMAT%></a>
						</td>
					</tr>

					<tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
							*
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
								<bean:message key="eav.att.Format"/>
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="format" />
						</td>
					</tr>
				 </table>
			</div>
			<div id="BooleanDataType" style="display:none">
			   <table summary="" cellpadding="3" cellspacing="0" border="1" align = 'center' width='100%'>
				   <tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
							*
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="25%">
							<bean:message key="eav.att.DefaultValue"/>
						</td>

						<td class="formFieldWithoutBorder">
							<html:select property="attributeDefaultValue" >
								<html:option  value="true">true</html:option>
								<html:option  value="false">false</html:option>
							</html:select>
						</td>
					</tr>

				 </table>
			</div>