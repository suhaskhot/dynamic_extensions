<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>



<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

	<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Label"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="caption" />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Name"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="name" />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.DataInput"/>
			</td>

			<td class="formField">
					<html:select property="dataType"  onchange="changeDataType(this)" >
						<html:options collection="dataTypeList" labelProperty="name" property="value" />
					</html:select>
			</td>
		</tr>
		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Description"/>
			</td>

			<td class="formField">
					<html:textarea styleClass="formFieldSized"  rows = "5" cols="40"  property="description"  />
			</td>
		</tr>

		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.CSSClassName"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="cssClass" />
			</td>
		</tr>


		<tr>
			<td class="formRequiredLabel">
					<bean:message key="eav.att.Tooltip"/>
			</td>

			<td class="formField">
					<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="tooltip" />
			</td>
		</tr>