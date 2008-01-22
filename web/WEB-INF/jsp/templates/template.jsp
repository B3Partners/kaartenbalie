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
        <link href="<html:rewrite page='/styles/selectcss' module='' />" type="text/css" rel="stylesheet" />
        <!--[if IE 6]>
        <link href="<html:rewrite page='/styles/main-ie6.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <!--[if IE 7]>
        <link href="<html:rewrite page='/styles/main-ie7.css' module='' />" rel="stylesheet" type="text/css">
        <![endif]-->
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
        <SCRIPT LANGUAGE="JavaScript" SRC="../js/tabcontainer.js"></SCRIPT>
        <script language="JavaScript" type="text/JavaScript">
            function hidePopup()
            {
                transDiv = document.getElementById('transdiv');
                divcontainer = document.getElementById('container');
                iframe = document.getElementById('popupframe');
                iframe.src = 'about:blank';
                transDiv.style.display = 'none';
            }
            
            function showPopupLocalized(width, height, title, source, left, top) {
               transDiv = document.getElementById('transdiv');
               divcontainer = document.getElementById('container');
               iframe = document.getElementById('popupframe');
               divcontainer.style.width = width + 'px';
               divcontainer.style.height = height + 'px';
               divcontainer.style.marginLeft =  left + 'px';
               divcontainer.style.marginTop =  top + 'px';
               iframe.src = source;
               transDiv.style.display = 'inline';
            }
            function showPopup(width, height, title, source) {
                left = ((width/2) * -1);
                top = ((height/2) * -1);
                showPopupLocalized(width, height, title, source, left, top)
            }
            function navigate(target, source)
            {
                hidePopup();
                if (target == null || target == '')
                {
                    location.href = source;
                } else {
                    frame = document.getElementById(target);
                    frame.src = source;
                }
            }
        </script>
    </head>
    <body>
        <div id="transdiv" onclick="hidePopup();">
            
            <div id="container">
                <iframe name="popupframe" id="popupframe" src="about:blank" frameborder="0" style="width:100%;height:100%"></iframe>
            </div>
        </div>        
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
                <a href="http://www.b3p.nl/b3partners/webPages.do?pageid=203015" style="cursor: hand;"><div id="initiatief"></div></a>
                <div id="copyright"></div>
            </div>
        </div>
        <tiles:insert definition="common.googleAnalytics"/>        
    </body>
</html:html>

