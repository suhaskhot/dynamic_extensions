<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/dynamicExtensionsTaglib.js" type="text/javascript"></script>
  <%-- Imports --%>    
     <%@
    	 page  
     	 language="java"
         contentType="text/html" 
         import="java.util.List"
     %>   
  <body>
<html:form styleId = "controlsForm" action="/ApplyFormControlsAction" >
	  	<html:errors />
	  	<table align = 'center' width='100%'>
	  		<tr>
	  			<td>
	  			
	  			<% List toolsList1 = (List)request.getAttribute("toolsList");
	  
	  			System.out.println("selectedTool"+toolsList1); %>
				    <dynamicExtensions:ToolsMenu id="BuildForm" 
							toolsList = "<%=toolsList1%>" 
							onClick="controlSelectedAction"
							height="100%" width="100%">						
			    </dynamicExtensions:ToolsMenu>
	  			</td>
	  			<td>
	  			Controls definition
	  			</td>
	  			<td>
	  			Form Controls Tree
	  			</td>
	  		</tr>
			
	  	</table>
	  	</html:form>
	  	</body>