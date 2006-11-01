<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : Deepti Shelar--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.util.Iterator" %>
<c:set var="textRulesList" value="${controlsForm.textValidationRulesList}"/>
<jsp:useBean id="textRulesList" type="java.util.List"/>
<c:set var="numberRulesList" value="${controlsForm.numberValidationRulesList}"/>
<jsp:useBean id="numberRulesList" type="java.util.List"/>
<div id="TextDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" align = 'left' width='100%'>

			 <tr>
				<td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			&nbsp
			 		 	</td>

			 			<td class="formRequiredLabelWithoutBorder">
			 				<bean:message key="eav.control.validation"/>
			 			</td>

			 		<td  class="formFieldWithoutBorder">
						<% Iterator iter = textRulesList.iterator();
							while (iter.hasNext())
							{
								String ruleLabel = iter.next().toString();
							
							%>
								
								<td  class="formFieldWithoutBorder">
			 					<html:checkbox property='validationRules' value='Required' ><%= ruleLabel %></html:checkbox>
							</td>
							<% } %>
							</td>
			 			
			 		</tr>
			 </table>
</div>

<div id="NumberDataType" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" align = 'left' width='100%'>

			 <tr>
			 <td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 &nbsp;			
			 		 	</td>
			 			<td class="formRequiredLabelWithoutBorder">
			 				<bean:message key="eav.control.validation"/>
			 			</td>
			 			<td  class="formFieldWithoutBorder">
						<% Iterator iter1 = numberRulesList.iterator();
							while (iter1.hasNext())
							{
								String ruleLabel = iter1.next().toString();
							
							%>
							<tr><td/><td/><td  class="formFieldWithoutBorder">
			 					<html:checkbox property='validationRules' value='Required' ><%= ruleLabel %></html:checkbox>
							</td></tr>
							<% if (ruleLabel.equalsIgnoreCase("Range")) {
								%>
						
								<tr>
								<td class="formRequiredNoticeWithoutBorder" width="2%">
									&nbsp;
								</td>
								<td class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.range.min"/>
								</td>
												<td class="formFieldWithoutBorder">
									<html:text styleClass="formParamSized"  maxlength="100" size="10"  property="attributenoOfCols" />
								</td>
									<td class="formRequiredNoticeWithoutBorder" width="2%">
										&nbsp;
								</td>
								<td class="formRequiredLabelWithoutBorder">
										<bean:message key="eav.range.max"/>
								</td>
												<td class="formFieldWithoutBorder">
									<html:text styleClass="formParamSized"  maxlength="100" size="10"  property="attributenoOfCols" />
								</td>
							</tr>
							<% } %>
							<tr>
							<% } %>
			 			</td>
			 		</tr>
			 </table>
</div>
