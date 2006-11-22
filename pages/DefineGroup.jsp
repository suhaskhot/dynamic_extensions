<%-- @author : Preeti Munot --%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@page import="java.util.List" %>

<html>
<head>
	<title>Dynamic Extensions</title>
	<link rel="stylesheet" type="text/css" href="css/styleSheet.css" />
	<script src="jss/script.js" type="text/javascript"></script>
	<script src="jss/dynamicExtensions.js" type="text/javascript"></script>

	<script>
		function changeGroupSource(groupSrc)
{
	alert('in group src');
	alert(groupSrc);
	if(groupSrc!=null)
	{
		var divForGrpDetails = document.getElementById('groupDetailsDiv');
		var divForGrpSrc = document.getElementById(groupSrc.value+"Div");
		if((divForGrpSrc!=null)&&(divForGrpDetails!=null))
		{
			divForGrpDetails.innerHTML = divForGrpSrc.innerHTML;
		}
	}
}

function initDefineGroupForm()
{
	alert('in init define group form');
	changeGroupSource(document.getElementById('createGroupAs'));
}
	</script>
	
</head>
  <body onload="initDefineGroupForm();">

<!-- Menu tabs -->
<c:set var="groupsList" value="${groupForm.groupList}"/>
<jsp:useBean id="groupsList" type="java.util.List"/>

<html:form styleId = "groupForm"  action="/LoadGroupDefinitionAction">
<input type="hidden" name="groupOperation" value=""/>
        <table valign="top"  align='right' width='90%' height="100%" border='0' cellspacing="0" cellpadding="0" >
		         <!-- Main Page heading -->
		         <tr valign="top" style = "border-bottom:0px">
		         	<td style = "border-right:0px;border-bottom:0px" width = '30px'>&nbsp;</td>
		         	<td style = "border-left:0px;border-bottom:0px" class="formFieldSized1" ><bean:message key="app.title.MainPageTitle" /></td>
		         </tr>
				  <tr >
					<td style = "border-top:0px;border-right:0px" width = '10px'>&nbsp;</td>
					<td style = "border-left:0px;border-top:0px;border-bottom:0px" valign="top" >
						 <table valign="top" align='left' width='90%' height = '90%' cellspacing="0" cellpadding="3" class = "tbBordersAllbordersBlack" >
							<tr valign="top" >
							   <td height="20" class="tabMenuItemSelected" >
								 <bean:message key="app.title.DefineGroupTabTitle" />
							   </td>

							   <td height="20" class="tabMenuItem"  onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" onclick="showDefineFormJSP()">
								 <bean:message key="app.title.DefineFormTabTitle" />
							   </td>

							   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()" >
								 <bean:message key="app.title.BuildFormTabTitle" />
							   </td>

							   <td height="20" class="tabMenuItem" onmouseover="changeMenuStyle(this,'tabMenuItemOver'),showCursor()" onmouseout="changeMenuStyle(this,'tabMenuItem'),hideCursor()"  >
								 <bean:message key="app.title.PreviewTabTitle" />
							   </td>
							   <td width="50%" class="tabMenuSeparator" colspan="3">&nbsp;</td>
							</tr>
							<tr height="100%" valign="top" >
								<td height = '100%' colspan="7">
									<table valign = "top" cellspacing="0" cellpadding="4"  align="left" width="100%" height = '100%' class = "tbBordersAllbordersBlack"  >

										<tr valign = "top">
											 <td class="formMessage" colspan="3">
												 <bean:message key="app.requiredMessage"/>
											 </td>
										</tr>
										<tr valign = "top">
											 <td class="formMessage" colspan="3">
												 <html:errors/>
											 </td>
										</tr>
										<tr valign = "top">
											 <td class="formMessage" colspan="3">
												&nbsp;
											 </td>
										</tr>
										<tr valign = "top" class="rowWithBottomPadding">
											<td  class="formRequiredNoticeWithoutBorder" width="2%">*</td>
											<td class="formRequiredLabelWithoutBorder" width="12%">
												<bean:message key="eav.att.GroupType"/> :
											</td>
											<td class="formFieldWithoutBorder" align="left">
												<html:radio styleId = 'createGroupAs' property="createGroupAs" value="NewGroup" onclick="changeGroupSource(this)">
													<bean:message key="eav.att.NewGroup"/>
												</html:radio>

												<html:radio disabled="true" property="createGroupAs" value="ExistingGroup" onclick="changeGroupSource(this)">
													<bean:message key="eav.att.ExistingGroup"/>
												</html:radio>
											</td>
										</tr>
										 <tr  valign = "top" >
											 <td colspan="3" valign = "top" class="cellWithNoLeftBottomPadding">
											 	<div id="groupDetailsDiv" > </div>
											 </td>
										</tr>
										<tr >
											 <td height = '100%'  class="formFieldSized3" colspan="3">
												 &nbsp;
											 </td>
										</tr>
									</table>
								</td>
							</tr>
							<tr height = '100%'  valign = "top" class= "formLabelBorderless">
								<td height = '100%' colspan="2" align="left">
									<html:button styleClass="actionButton" property="saveButton" onclick="saveGroup()">
										<bean:message  key="buttons.save" />
	 								</html:button>

									<html:button styleClass="actionButton" property="cancelButton" onclick="showHomePageFromCreateGroup()">
										<bean:message  key="buttons.cancel" />
	 								</html:button>
								</td>
								<td height = '100%' colspan="5" align="right">
									<html:button styleClass="actionButton" property="nextButton" onclick="showDefineFormJSP()" >
										<bean:message  key="buttons.next" />
	 								</html:button>
								</td>
							</tr>
						</table>
					</td>
				</tr>
		</table>
<div id="NewGroupDiv" style="display:none">
	<table  valign = "top" cellspacing="0" cellpadding="3" width="100%" height = '100%'>

		<tr valign = "top"  class="rowWithBottomPadding">
			<td  class="formRequiredNoticeWithoutBorder" width="2%">*</td>
			<td class="formRequiredLabelWithoutBorder" width="12%">
			<label for="groupName">
				<bean:message key="eav.att.GroupTitle"/> :
			</label>
			</td>
			<td class="formFieldWithoutBorder">
				<html:text styleClass="formDateSized" styleId="groupName"  property="groupName" />
			</td>
		</tr>

		<tr valign = "top"  class="rowWithBottomPadding">
			<td class="formRequiredNoticeWithoutBorder" width="2%" >&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="12%">
			<label for="groupDescription">
				<bean:message key="eav.form.description"/> :
			</label>
			</td>
			<td class="formFieldWithoutBorder">
				<html:textarea styleClass="formFieldSmallSized"  rows = "3" cols="28" styleId="groupDescription"  property="groupDescription" />
			</td>
		 </tr>
		 </table>
</div>
<div id="ExistingGroupDiv" style="display:none">
	<table valign = "top" cellspacing="0" cellpadding="4"  align="left" width="100%" height = '100%' >
		<tr valign = "top"  class="rowWithBottomPadding">
			<td class="formRequiredNoticeWithoutBorder" width="2%" >&nbsp;</td>
			<td class="formRequiredLabelWithoutBorder" width="12%">
				&nbsp;
			</td>
			<td class="formFieldWithoutBorder"  class="rowWithBottomPadding">
				<html:select styleClass="formDateSized"  property="groupName" >
					<c:forEach items="${groupsList}" var="groupname">
						<jsp:useBean id="groupname" type="java.lang.String" />
							<html:option  value='<%=groupname%>' ></html:option>
					</c:forEach>
				</html:select>
			</td>
		 </tr>
	 </table>
</div>
</html:form>
</body>
</html>

