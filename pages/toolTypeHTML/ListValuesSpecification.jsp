<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
<jsp:useBean id="displayChoiceList" type="java.util.List"/>


<div id="UserDefinedValues" style="display:none">
		<input type="hidden" value="1" name="choiceListCounter" >
	<table summary="" cellpadding="0" cellspacing="0" border="0" align = 'center' width='100%'>
			<tr>
				<td class="formRequiredLabel"><input type="text" maxlength="5" size="10" name="choiceValue"></td>
					
				<td class="formField"> <input type="button" name="addChoiceValue" value="Add Value" onclick="addChoiceToList(true);"></td>
			</tr>
			<tr class="formRequiredLabel">
		
				<td class="formRequiredLabel">
					<table id="choiceListTable" summary="" cellpadding="0" cellspacing="0" border="1" align = 'center' width='100%'> 
						<tr>
							<th width="10%"><input type="button" name="btnDelete" value="Delete" onclick="deleteElementsFromChoiceList()" ></th>
							<th width="10%"><bean:message key="eav.att.values"/></th>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<div id="CADSRValues" style="display:none">
			<table summary="" cellpadding="1" cellspacing="0" border="1" align = 'center' width='100%'>
				<tr>
					<td class="formRequiredLabel"> Public Domain Id</td>
					<td class="formField"><input type="text" name="publicDomainId"></td>
				</tr>
		</table>
	</div>