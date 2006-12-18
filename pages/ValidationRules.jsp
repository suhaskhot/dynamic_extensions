<%-- Jsp Summary                                                                                 	--%>
<%-- @author : Deepti Shelar																		--%>
<%-- ---------------------------------------------------------------------------------------------- --%>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.List" %>
<script src="<%=request.getContextPath()%>/jss/calender.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/jss/calendarComponent.js"></script>
<%@page import="edu.common.dynamicextensions.ui.util.RuleConfigurationObject" %>
<%@page import="edu.wustl.common.beans.NameValueBean" %>
<%@page import="edu.common.dynamicextensions.util.DynamicExtensionsUtility" %>

<c:set var="controlRuleMap" value="${controlsForm.controlRuleMap}"/>
<jsp:useBean id="controlRuleMap" type="java.util.Map"/>

<c:set var="controlName" value="${controlsForm.userSelectedTool}"/>
<jsp:useBean id="controlName" type="java.lang.String"/>

<%
	boolean isFirst = true;
	Iterator iter = controlRuleMap.keySet().iterator();
	while (iter.hasNext())
	{
		String dataType = (String)iter.next();
		List listofRules = (List)controlRuleMap.get(dataType);
		String divName = dataType+"Div";			
%>
		<div id="<%= divName%>" style="display:none">
			<table summary="" cellpadding="3" cellspacing="0" align = 'left' width='100%'>
			<% 	
				Iterator rulesIter = listofRules.iterator();
				while(rulesIter.hasNext())
				{
					RuleConfigurationObject ruleObject = (RuleConfigurationObject)rulesIter.next();
					String ruleLabel =ruleObject.getDisplayLabel();
					String ruleName = ruleObject.getRuleName();
					List params = ruleObject.getRuleParametersList();
			%>
					<tr align="top">
				        <td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp</td>
			 		 	<td class="formRequiredLabelWithoutBorder" width="25%">
			 		 	<% 
							if (isFirst)
							{ 
						%>
		 						<bean:message key="eav.control.validation"/>
						<% 
							}
						%>&nbsp;
						</td>
			 			<td class="formFieldWithoutBorder">
	 						<html:multibox  styleId = 'tempValidationRules' property='tempValidationRules' value="<%= ruleName%>" onclick="ruleSelected(this)">
								<bean:message key="<%= ruleLabel%>"/>
							</html:multibox>
							<bean:message key="<%= ruleLabel%>"/>
						</td>
					</tr>

					<% 
						if(params != null) 
						{
							Iterator paramsIter = params.iterator(); 
							while(paramsIter.hasNext())
							{
								NameValueBean paramObject = (NameValueBean)paramsIter.next();
								String paramLabel = paramObject.getValue();
								String paramName = paramObject.getName();
					%>
							 	<tr>
							        <td class="formRequiredNoticeWithoutBorder" width="2%">&nbsp;</td>									
									<td class="formRequiredNoticeWithoutBorder" width="25%">&nbsp;</td>
									<td class="formRequiredLabelWithoutBorder" align="center">
										&nbsp;&nbsp;&nbsp;&nbsp;<%= paramLabel%>&nbsp;
										<html:text styleId ="<%= paramName%>" styleClass="formParamSized"  maxlength="100" size="60"  property="<%= paramName%>" />
									<% 
										if(controlName.equalsIgnoreCase("DateControl"))
										{ 
											String divId = "slcalcod"+ paramName;
									%>
										<A onclick="showCalendar('<%=paramName%>',<%=DynamicExtensionsUtility.getCurrentYear()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentDay()%>,'MM-dd-yyyy','controlsForm','<%= paramName%>',event,1900,2020);" href="javascript://">
											<IMG alt="This is a Calendar" src="images/calendar.gif" border=0>
										</A>
										<div id=<%=divId%> style="Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px">
											<SCRIPT>printCalendar('<%= paramName%>',<%=DynamicExtensionsUtility.getCurrentDay()%>,<%=DynamicExtensionsUtility.getCurrentMonth()%>,<%=DynamicExtensionsUtility.getCurrentYear()%>);</SCRIPT>
										</div><label class="formFieldWithoutBorder">[MM-DD-YYYY]&nbsp;</label>
									<%
										} //end if(controlName.equalsIgnoreCase("DateControl"))
									%>
		 							</td>
								</tr>
						<%
							} // end while(paramsIter.hasNext())
						%>
					<%
						} // end if(params != null)
					%>
	 		<%
	 				isFirst = false;
	 			} // while(rulesIter.hasNext())
	 		%>
			</table>
		</div>
<%
	} // while(iter.hasNext())
%>