<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="edu.common.dynamicextensions.processor.ProcessorConstants"%>

<script src="jss/overlib_mini.js" type="text/javascript"></script>
<script src="jss/calender.js" type="text/javascript"></script>

<c:set var="linesType" value="${controlsForm.linesType}"/>
<jsp:useBean id="linesType" type="java.lang.String"/>

<c:set var="measurementUnitsList" value="${controlsForm.measurementUnitsList}"/>
<jsp:useBean id="measurementUnitsList" type="java.util.List"/>

<input type="hidden" id = 'linesTypeHidden'  name="linesTypeHidden" value='<%=linesType%>' />
		<div id="TextDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" align = 'center' width='100%'>

			 	<tr valign="top">
			 			<td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			*
			 		 	</td>
			 			<td class="formRequiredLabelWithoutBorder" width="30%">
			 				<bean:message key="eav.control.type"/> :
			 			</td>
			 			<td  class="formFieldWithoutBorder">
			 			<html:radio property='linesType' value='<%=ProcessorConstants.LINE_TYPE_SINGLELINE%>' onclick='textBoxTypeChange(this)'>
				 			<bean:message key="eav.att.TextBoxSingleLineTitle"/>
			 			</html:radio>
			 			<html:radio property='linesType' value='<%=ProcessorConstants.LINE_TYPE_MULTILINE%>' onclick='textBoxTypeChange(this)'>
				 			<bean:message key="eav.att.TextBoxMultiLineTitle"/>
			 			</html:radio>
			 			</td>
			 		</tr>
			 		<tr valign="top" id="rowForNumberOfLines" style="display:none;">
			 			<td class="formRequiredNoticeWithoutBorder" width="2%">
			 				&nbsp;
			 		 	</td>
						<td class="formRequiredLabelWithoutBorder" id="noOfLines" >
							<bean:message key="eav.text.noOfLines"/> :
						</td>
						<td class="formFieldWithoutBorder">
								<html:text styleClass="formFieldSized5"  maxlength="100" size="60"  property="attributeNoOfRows" />
						</td>
					</tr>
			       	<tr valign="top">
			       			<td class="formRequiredNoticeWithoutBorder" width="2%">
									 		&nbsp;
		 					</td>
						<td class="formRequiredLabelWithoutBorder" width="30%">
								<bean:message key="eav.att.MaxCharacters"/> :
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formFieldSized5"  maxlength="100" size="60"  property="attributeSize" />
						</td>
					</tr>

					<tr valign="top">
						<td class="formRequiredNoticeWithoutBorder" width="2%">
									 			&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="30%">
								<bean:message key="eav.att.DefaultValue"/> :
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>
				 </table>
			</div>

			<div id="NumberDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0"  align = 'center'  width='100%'>

					<tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
								&nbsp;
						</td>
						<td class="formRequiredLabelWithoutBorder" width="30%">
								<bean:message key="eav.att.AttributeDecimalPlaces"/> :
						</td>

						<td class="formFieldWithoutBorder">
								<html:text styleClass="formFieldSized5"  maxlength="100" size="60"  property="attributeDecimalPlaces" />
						</td>
					</tr>


					<tr>
						<td class="formRequiredNoticeWithoutBorder" width="2%">
							&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="30%">
							<bean:message key="eav.att.DefaultValue"/> :
						</td>

						<td class="formFieldWithoutBorder">
							<html:text styleClass="formFieldSized5"  maxlength="100" size="60"  property="attributeDefaultValue" />
						</td>
					</tr>

					<tr >
						<td class="formRequiredNoticeWithoutBorder" width="2%">
														 			&nbsp;
		 				</td>
						<td class="formRequiredLabelWithoutBorder" width="30%">
								<bean:message key="eav.att.AttributeMeasurementUnits"/> :
						</td>

						<td class="formFieldWithoutBorder" >
							<html:select styleClass="formDateSized"  property="attributeMeasurementUnits" onchange="measurementUnitsChanged(this)">
								<c:forEach items="${measurementUnitsList}" var="measurementUnit">
									<jsp:useBean id="measurementUnit" type="java.lang.String" />
										<html:option  value='<%=measurementUnit%>' >
										</html:option>
								</c:forEach>
							</html:select>
							<html:text styleClass="formFieldSized5"  property="measurementUnitOther"  ></html:text>
						</td>
					</tr>
				 </table>
			</div>
