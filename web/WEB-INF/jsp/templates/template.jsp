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
        <META HTTP-EQUIV="Expires" CONTENT="-1" />
        <META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0, no-store" />
        
        <title><tiles:insert name='title'/> - <fmt:message key="kaartenbalie.title" /></title>
        <link href="<html:rewrite page='/styles/gisviewer_base.css' module='' />" rel="stylesheet" type="text/css">
        <script language="JavaScript" type="text/JavaScript" src="<html:rewrite page='/js/validation.jsp' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/tabcontainer.js' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/jquery-1.5.1.min.js' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/jquery.dataTables.min.js' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/commonfunctions.js' module=''/>"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/jquery.bgiframe.min.js' module=''/>"></script>
        <script language="JavaScript" type="text/JavaScript">
            function hidePopup()
            {
                var transDiv = document.getElementById('transdiv');
                var divcontainer = document.getElementById('container');
                var iframe = document.getElementById('popupframe');
                iframe.src = 'about:blank';
                transDiv.style.display = 'none';
            }
            
            function showPopupLocalized(width, height, title, source, left, top) {
                var transDiv = document.getElementById('transdiv');
                var divcontainer = document.getElementById('container');
                var iframe = document.getElementById('popupframe');
                divcontainer.style.width = width + 'px';
                divcontainer.style.height = height + 'px';
                divcontainer.style.marginLeft =  left + 'px';
                divcontainer.style.marginTop =  top + 'px';
                iframe.src = source;
                transDiv.style.display = 'inline';
            }
            function showPopup(width, height, title, source) {
                var left = ((width/2) * -1);
                var top = ((height/2) * -1);
                showPopupLocalized(width, height, title, source, left, top);
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
        <div id="wrapper">
            <div id="header"><div id="header_content"><tiles:insert attribute="menu" /></div></div>
            <div id="content_normal">
                <div id="content">
                    <tiles:insert definition="common.actionMessages"/>
                    <tiles:insert name='content'/>
                </div>
            </div>
        </div>
        <div id="footer">
            <div id="footer_content">
                <div id="footer_tekst_links" class="footer_tekst">This program is distributed under the terms of the <a class="gpl_link" href="http://www.gnu.org/licenses/gpl.html">GNU General Public License</a></div>
                <div id="footer_tekst_rechts" class="footer_tekst">B3P GIS Suite 4.2</div>
            </div>
        </div>
        <tiles:insert definition="common.googleAnalytics"/>
        <!--[if lte IE 7]>
        <script type="text/javascript">
            $("#header_content").find(".menu").children('li').each(function() {
                $(this).width($(this).find("a").outerWidth());
            });
            $("#header_content").find(".menu").css('visibility', 'visible');
            $("#header").css({"position": "relative", "z-index": 300});
            $("#content_normal").css({"z-index": 200});
        </script>
        <![endif]-->
    </body>
</html>

