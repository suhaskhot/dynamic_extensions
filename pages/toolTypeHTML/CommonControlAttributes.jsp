<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>



<c:set var="dataTypeList" value="${controlsForm.dataTypeList}"/>
<jsp:useBean id="dataTypeList" type="java.util.List"/>

<table summary="" cellpadding="3" cellspacing="0"  align = 'center' width='100%' >
			<tr valign="top">
				<td  class="formRequiredNoticeWithoutBorder" width="2%">
						*
				</td>

				<td class="formRequiredLabelWithoutBorder" width="15%">
						<bean:message key="eav.att.Label"/>
				</td>

				<td class="formFieldWithoutBorder">
						<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="caption" />
				</td>
			</tr>
			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
						&nbsp;
				</td>

				<td class="formRequiredLabelWithoutBorder" width="15%">
					<bean:message key="eav.form.conceptCode"/>
				</td>

				<td class="formFieldWithoutBorder">
						<html:text styleClass="formDateSized"  maxlength="100" size="60"  property="attributeConceptCode" />
				</td>

			</tr>


			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
							</td>

				<td class="formRequiredLabelWithoutBorder" width="15%">
						<bean:message key="eav.att.Description"/>
				</td>

				<td class="formFieldWithoutBorder">
						<html:textarea styleClass="formFieldSmallSized" rows = "3" cols="28"  property="description"  />
				</td>
			</tr>

			<tr valign="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
						&nbsp;
				</td>
				<td class="formRequiredLabelWithoutBorder" width="15%">&nbsp;</td>
				<td class="formFieldWithoutBorder" align="left">
					<html:checkbox property="attributeIdentified" value="true"><bean:message key="app.att.isIdentified" /></html:checkbox>
				</td>
			</tr>
	</tr>
	</table>