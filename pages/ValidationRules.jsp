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
							String divName = dataType+"Div";			
							boolean isFirst = true;
							%>

<div id="<%= divName%>" style="display:none">
			 <table border="2" summary="" cellpadding="3" cellspacing="0" align = 'left' width='100%'>
			 <hr>
			
						
							
						<% Iterator rulesIter = listofRules.iterator();
								while(rulesIter.hasNext())
								{
										RuleConfigurationObject ruleObject = (RuleConfigurationObject)rulesIter.next();
										String ruleLabel =ruleObject.getDisplayLabel();
										String ruleName = ruleObject.getRuleName();
										List params = ruleObject.getRuleParametersList();
						%>
					
					 <tr align="top">
				         <td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			&nbsp
			 		 	</td>
			 			<td class="formRequiredLabelWithoutBorder" width="25%">
						  <% if (isFirst)  { %>
			 				<bean:message key="eav.control.validation"/>
						<% } %> &nbsp;</td>
			 			
						<td  class="formFieldWithoutBorder">
			 						<html:checkbox property='validationRules' value="<%= ruleName%>"><bean:message key="<%=ruleLabel %>"/></html:checkbox>
						</td>
					</tr>
								
									
								<% if (params != null) {
									 Iterator paramsIter = params.iterator(); 
									
									while(paramsIter.hasNext())
									{
											NameValueBean paramObject = (NameValueBean)paramsIter.next();
											String paramLabel =paramObject.getValue();
											String paramName = paramObject.getName();
										%>
								 <tr>
								        <td class="formRequiredNoticeWithoutBorder" width="2%">
			 					 			&nbsp
								</td>
								<td class="formRequiredNoticeWithoutBorder" width="25%">
													&nbsp
								</td> 
	
												<td class="formRequiredLabelWithoutBorder"  align="center">&nbsp;&nbsp;
&nbsp;	&nbsp;											
												<%= paramLabel%>&nbsp
												<html:text styleClass="formParamSized"  maxlength="100" size="60"  property="<%= paramName%>" />
			 							</td>
							</tr>
								
							<% } %>
						
						<% } %>
					
			 		<% isFirst = false;} %>
				


		 </table>
</div>
<% } %>