<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

<html:html>
    <head>
        <title>Kaartenbalie</title>
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <link href="<html:rewrite page='/styles/selectcss' module='' />" type="text/css" rel="stylesheet" />
        <!--[if IE 6]>
        <link href="<html:rewrite page='/styles/main-ie6.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <!--[if IE 7]>
        <link href="<html:rewrite page='/styles/main-ie7.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>">
        </script>
    </head>
    <body>
        <div id="bodycontent">
            <div id="banner">
                <!-- BEGIN header -->
                <tiles:insert name='top'/>
                <!-- END header -->
            </div>
            <div id="balk">
                <div>
                    <a href="<html:rewrite page='/login.do' module='' />">Login</a>
                </div>
            </div>
            <div id="nav">
                <!-- BEGIN menu -->
                <tiles:insert name='menu'/>
                <!-- END menu -->
            </div>
            <div id="content">
                <!-- BEGIN content -->		
                <tiles:insert definition="common.actionMessages"/>
                <tiles:insert name='content'/>
                <!-- END content -->
            </div>
            <div id="footer">
                <div id=footertext>
                    <!-- BEGIN footer-->
                    <tiles:insert name='footer'/>
                    <!-- END footer-->
                </div>
            </div>
        </div>
    </body>
</html:html>

