<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="edu.wustl.dynamicextensions.formdesigner.resource.facade.ContainerFacade" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/prototype.js"></script>
<link rel="stylesheet" type="text/css" href="../../stylesheets/de/styleSheet.css" />
<link rel="stylesheet" type="text/css" href="../../stylesheets/de/de_style.css" />
<link rel="stylesheet" type="text/css" href="../../stylesheets/de/ext-all.css" />
<link href="../../stylesheets/de/calanderComponent.css" type=text/css rel=stylesheet />
<link rel="stylesheet" type="text/css" href="../../stylesheets/de/xtheme-gray.css" />
<script src="../../javascripts/de/script.js" type="text/javascript"></script>
<script src="../../javascripts/de/jquery-1.3.2.js" type="text/javascript"></script>
<script>jQuery.noConflict();</script>
<script src="../../javascripts/de/form_plugin.js" type="text/javascript"></script>
<script src="../../javascripts/de/dynamicExtensions.js" type="text/javascript"></script>
<script src="../../javascripts/de/overlib_mini.js" type="text/javascript"></script>
<script src="../../javascripts/de/ajax.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/scriptaculous.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/scr.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/ext-base.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/ext-all.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/combos.js"></script>
<script language="JavaScript" type="text/javascript" src="../../javascripts/de/ajaxupload.js"></script>
<link rel="stylesheet" type="text/css" href="../../dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.css" />
<link rel="stylesheet" type="text/css" href="../../dhtmlxSuite_v35/dhtmlxCalendar/codebase/skins/dhtmlxcalendar_dhx_terrace.css" />
<script type="text/javascript" src="../../dhtmlxSuite_v35/dhtmlxCalendar/codebase/dhtmlxcalendar.js"></script>
<script>
	var imgsrc="../..images/de/";
</script>
<title>CSD Preview</title>
</head>
<body style = "font-size : 20px;">
	<%
	ContainerFacade containerFacade = (ContainerFacade) request.getSession().getAttribute(
			"sessionContainer");
	%>
	<%=containerFacade.getHTML(request).replaceAll("images/de/", "../../images/de/") %>
</body>
</html>