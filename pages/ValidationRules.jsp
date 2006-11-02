<%-- Jsp Summary                                                                                 	--%>
<%-- ---------------------------------------------------------------------------------------------- --%>
<%-- @author : Deepti Shelar--%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.List" %>
<%@page import="edu.common.dynamicextensions.ui.util.RuleConfigurationObject" %>
<%@page import="edu.wustl.common.beans.NameValueBean" %>
<c:set var="controlRuleMap" value="${controlsForm.controlRuleMap}"/>
<jsp:useBean id="controlRuleMap" type="java.util.Map"/>
<%
						Iterator iter = controlRuleMap.keySet().iterator();
							while (iter.hasNext())
							{
							String dataType = (String)iter.next();
							List listofRules = (List)controlRuleMap.get(dataType);
							String divName = dataType+"Div";							%>

<div id="<%= divName%>" style="display:none">
			 <table summary="" cellpadding="3" cellspacing="0" align = 'left' width='100%'>
			 <tr align="top">
				<td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			&nbsp
			 		 	</td>
			 			<td class="formRequiredLabelWithoutBorder" >
			 				<bean:message key="eav.control.validation"/>
			 			</td>
						<td  class="formFieldWithoutBorder">
							<table>
						<% Iterator rulesIter = listofRules.iterator();
								while(rulesIter.hasNext())
								{
										RuleConfigurationObject ruleObject = (RuleConfigurationObject)rulesIter.next();
										String ruleLabel =ruleObject.getDisplayLabel();
										String ruleName = ruleObject.getRuleName();
										List params = ruleObject.getRuleParametersList();
						%>
					
						<tr>
			 			
								<td  class="formFieldWithoutBorder">
			 						<html:checkbox property='validationRules' value="<%= ruleName%>"><%= ruleLabel %></html:checkbox>
								</td>
								</tr>
								
										<tr>
								<% if (params != null) {
									 Iterator paramsIter = params.iterator(); %>
									 <tr>
									<td class="formRequiredLabelWithoutBorder" >
												<bean:message key="eav.range.warning"/>
			 							</td>
										</tr>
										<%
									while(paramsIter.hasNext())
									{
											NameValueBean paramObject = (NameValueBean)paramsIter.next();
											String paramLabel =paramObject.getValue();
											String paramName = paramObject.getName();
										%>
									<tr>	
										<td class="formRequiredLabelWithoutBorder" >
												<%= paramLabel%>
			 							</td>
										<td class="formFieldWithoutBorder">
											<html:text styleClass="formParamSized"  maxlength="100" size="60"  property="<%= paramName%>" />
										</td>
									</tr>
							<% } %>
						<% } %>
						</tr>
			 		<% } %>
				 </table>
				</td>
			 </tr>
		 </table>
</div>
<% } %>