<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <META HTTP-EQUIV="Expires" CONTENT="-1" />
        <META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0, no-store" />
        
        <title>Kaartenbalie</title>
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <link href="<html:rewrite page='/styles/mnp_nieuw.css' module='' />" type="text/css" rel="stylesheet" />
        <!--[if IE 6]>
        <link href="<html:rewrite page='/styles/main-ie6.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <!--[if IE 7]>
        <link href="<html:rewrite page='/styles/main-ie7.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
    </head>
    <body>
        <div id="bodycontent">
            <!-- <div id="banner">
                BEGIN header
                <%-- tiles:insert name='top'/ --%>
                END header
            </div> -->
            <div id="balk">
                <div id="banner">
                    <!-- BEGIN header -->
                    <tiles:insert name='top'/>
                    <!-- END header -->
                </div>
                <div id="menudiv">
                    <!-- BEGIN menu -->
                    <tiles:insert name='menu'/>
                    <!-- END menu -->
                </div>
            </div>
            
            <div id="content">
                <div id="content_c">
                    <!-- BEGIN content -->		
                    <tiles:insert definition="common.actionMessages"/>
                    <tiles:insert name='content'/>
                    <!-- END content -->
                </div>
            </div>
            
            <div id="footer">
                <!-- <div id=footertext>
                    BEGIN footer
                    <tiles:insert name='footer'/>
                    END footer
                </div>-->
                <div id="initiatief"></div>
                <div id="copyright"></div>
            </div>
        </div>
    </body>
</html:html>

