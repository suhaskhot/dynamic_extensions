<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="displayChoiceList" value="${controlsForm.displayChoiceList}"/>
<jsp:useBean id="displayChoiceList" type="java.util.List"/>


<div id="UserDefinedValues" style="display:none">
		<input type="hidden" value="1" name="choiceListCounter" >
		<table>
			<tr>
				<td class="formRequiredLabel"><input type="text" name="choiceValue"></td>
				<td class="formField"> <input type="button" name="addChoiceValue" value="Add Value" onclick="addChoiceToList();"></td>
			</tr>
			<tr>
				<td colspan='2'>
					<table id="choiceListTable" border="1" width="100%" >
						<tr>
							<th width="20%"><input type="button" name="btnDelete" value="Delete" onclick="deleteElementsFromChoiceList()" ></th>
							<th ><bean:message key="eav.att.values"/></th>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<div id="CADSRValues" style="display:none">
			<table >
				<tr>
					<td class="formRequiredLabel"> Public Domain Id</td>
					<td class="formField"><input type="text" name="publicDomainId"></td>
				</tr>
		</table>
	</div>