<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
		<div id="StringDataType" style="display:none">
			    <table>
			       	<tr>
						<td class="formRequiredLabel">
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
			    <table>
			       <tr>
						<td class="formRequiredLabel">
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
			<div id="DateDataType" style="display:none">
			    <table>
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
								<bean:message key="eav.att.Format"/>
						</td>
			
						<td class="formField">
								<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="format" />
						</td>
					</tr>
				 </table>
			</div>