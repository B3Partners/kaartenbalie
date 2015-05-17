<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2007-2011 B3Partners BV.

This program is distributed under the terms
of the GNU General Public License.

You should have received a copy of the GNU General Public License
along with this software. If not, see http://www.gnu.org/licenses/gpl.html

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
--%>
<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="nl"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="nl"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="nl"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="nl"> <!--<![endif]-->
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><fmt:message key="kaartenbalie.title" /></title>
        <link href="<html:rewrite page='/styles/gisviewer_base.css' module='' />" rel="stylesheet" type="text/css">
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/table.js' module=''/>"></script>
        <SCRIPT LANGUAGE="JavaScript" SRC="../js/tabcontainer.js"></SCRIPT>
    </head>
    <body class="cleantemplate">
        <tiles:insert definition="common.actionMessages"/>
        <tiles:insert name='content'/>
    </body>
</html>
