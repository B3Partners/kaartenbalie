<%@include file="/templates/taglibs.jsp" %>
<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

<html:html>
    <head>
        <title>Kaartenbalie</title>
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <!--[if IE 6]>
            <link href="<html:rewrite page='/styles/main-ie6.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>">
        </script>
    </head>
    <body>
        <tiles:insert page="${template}" flush="true">
            <tiles:put name="top"       value="${top}"/>
            <tiles:put name="menu"      value="${menu}"/>
            <tiles:put name="content"   value="${content}"/>
            <tiles:put name="footer"    value="${footer}"/>
        </tiles:insert>
    </body>
</html:html>