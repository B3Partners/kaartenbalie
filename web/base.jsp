<%--
author <a href="info@b3partners.nl">B3Partners</a>
version $Id: base.jsp 3519 2006-05-10 15:48:02Z Frank $
--%>

<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false"%>

<%@ page errorPage="/navinfo/errorpage.jsp" %>

<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 


<html:html>
    <head>
        <title>Kaartenbalie</title>
        
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
    </head>
    <body>
        <tiles:insert page="${template}" flush="true">
            <tiles:put name="top"     value="${top}"/>
            <tiles:put name="menu"     value="${menu}"/>
            <tiles:put name="content"     value="${content}"/>
            <tiles:put name="footer"  value="${footer}"/>
        </tiles:insert>
    </body>
</html:html>