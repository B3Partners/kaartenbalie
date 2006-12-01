
<%--
author <a href="info@b3partners.nl">B3Partners</a>
version $Id: viewerbase.jsp 3227 2006-04-12 13:31:11Z Chris $
--%>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false"%>

<%@ page errorPage="/navinfo/errorpage.jsp" %>

<tiles:importAttribute/>

<%--<c:set var="form" value="${viewerForm}"/>
<c:set var="action" value="${form.map.action}"/>
<c:set var="mode" value="${form.map.mode}"/>
<c:set var="minx" value="${form.map.minx}"/>
<c:set var="maxx" value="${form.map.maxx}"/>
<c:set var="miny" value="${form.map.miny}"/>
<c:set var="maxy" value="${form.map.maxy}"/>
<c:set var="perceelnr" value="${form.map.Perceelnr}"/>--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 



<html:html>
    <head>
        <title><fmt:message key="index.title"/></title>
        <title><fmt:message key="index.title"/></title>
        <meta content="document" name="resource-type">
        <meta content="landmark information group" name="author">
        <meta content="landmark information | environmental reports &amp; digital mapping" name="title">
        <meta content="britain's leading supplier of environmental and property risk assessment reports. aerial photos, ordnance survey digital maps and services for environmental, engineering, planning and legal professionals." name="description" >
        <meta content="environmental policy, management, analysis, property risk assessment, report, ordnance survey maps, os, online mapping, internet, aerial photos, photographs, engineering, planning, legal, uk, britain" name="keywords">
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <link href="<html:rewrite page='/styles/editRapport.css' module='' />" rel="stylesheet" type="text/css">
        

        

    </head>

    <body>
        <tiles:insert page="${template}" flush="true">
            <tiles:put name="menu" value="${menu}"/>
            <tiles:put name="info" value="${info}"/>
            <tiles:put name="thema" value="${thema}"/>
            <tiles:put name="content" value="${content}"/>
      </tiles:insert>
    </body>
</html:html>