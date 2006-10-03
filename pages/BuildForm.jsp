<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/dynamicExtensions.tld" prefix="dynamicExtensions" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<script src="jss/dynamicExtensions.js" type="text/javascript"></script>
<script src="jss/dynamicExtensionsTaglib.js" type="text/javascript"></script>

<c:set var="toolsList" value="${controlsForm.toolsList}"/>
 <jsp:useBean id="toolsList" type="java.util.List"/>

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
	  	<table align = 'center' width='100%' border="3">
	  		<tr>
	  		
	  			<td>
	  			
				    <dynamicExtensions:ToolsMenu id="BuildForm" 
							toolsList = "<%=toolsList%>" 
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