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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<html>
    <head>
        <!--title>B3Partners Metadata Editor</title-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta http-equiv="pragma" content="no-cache" />

        <link href="<html:rewrite page='/js/metadataEditor/styles/jquery-ui-latest.custom.css' module='' />" rel="stylesheet" type="text/css" />
        <link href="<html:rewrite page='/js/metadataEditor/styles/standalone.css' module='' />" rel="stylesheet" type="text/css" />
        <link href="<html:rewrite page='/js/metadataEditor/styles/metadataEdit.css' module='' />" rel="stylesheet" type="text/css" />
        <!--[if IE]> <link href="<html:rewrite page='/js/metadataEditor/styles/metadataEdit-ie.css' module='' />" rel="stylesheet" type="text/css" /> <![endif]-->
        <!--[if lte IE 7]> <link href="<html:rewrite page='/js/metadataEditor/styles/metadataEdit-ie7.css' module='' />" rel="stylesheet" type="text/css" /> <![endif]-->
        <style type="text/css">
            #error {
                border: 1px solid red;
                background: #fff5f5 url(<html:rewrite page='/images/siteImages/warning.jpg' module='' />) no-repeat;
                background-position: 10px 11px;
                padding: 12px;
                padding-left: 30px;
                margin: 10px;
                margin-left: 0px;
            }
            #acknowledge {
                border: 1px solid #357EBF;
                background: #E0E1FF url(<html:rewrite page='/images/icons/information.png' module='' />) no-repeat;
                background-position: 10px 11px;
                padding: 12px;
                padding-left: 30px;
                margin: 10px;
                margin-left: 0px;
            }
        </style>

        <!-- jquery -->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/jquery/jquery-latest.js' module='' />"></script>
        <!--script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/jquery/jquery-latest.min.js' module='' />"></script-->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/jquery-ui/jquery-ui-latest.custom.js' module='' />"></script>
        <!--script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/jquery-ui/jquery-ui-latest.custom.min.js' module='' />"></script-->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/jquery.ui.datepicker-nl/jquery.ui.datepicker-nl.js' module='' />"></script>

        <!-- mde dependencies -->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/sarissa/sarissa.js' module='' />"></script>
        <!--script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/sarissa/sarissa-compressed.js' module='' />"></script-->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/sarissa/sarissa_ieemu_xpath.js' module='' />"></script>
        <!--script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/sarissa/sarissa_ieemu_xpath-compressed.js' module='' />"></script-->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/Math.uuid.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/wiki2html.js' module='' />"></script>

        <!-- organisations database. should be filled by customer data. -->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/picklists/organisations.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/picklists/gemet-nl.js' module='' />"></script>
        <!-- mde -->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEditor.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/commonOptions.js' module='' />"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $("#metadataEditor").mde({
                    xml: $("#md-placeholder").text(),
                    fcMode: true,
                    logMode: true,
                    richTextMode: true,
                    organisations: organisations,
                    change: function(changed) {
                        $("#saveButton").attr("disabled", !changed);
                    }
                });

                $("#saveButton").click(function(event) {
                    putMetadataInDiv();
                    $("#metadataForm").submitAsEvent("save");
                });

                $("#downloadButton").click(function(event) {
                    putMetadataInDiv();
                    $("#metadataForm").submitAsEvent("download");
                });

            });
            
            function putMetadataInDiv() {
                var xpath = $("#saveStrict").is(":checked") ? "/metadata/gmd:MD_Metadata" : "/metadata";
                var savedMD = $("#metadataEditor").mde("save", {
                    xpath: xpath,
                    reloadAfterSave: false // this option gives problems with Google Chrome
                });
                $("#metadata").val(Sarissa.escape(savedMD));
            };

            (function($) {
                $.fn.submitAsEvent = function(eventName) {
                    return this.each(function() {
                        var $this = $(this);
                        var input = $("<input>").attr({
                            type: "hidden",
                            name: eventName
                        }).val("t");
                        $this.append(input);
                        $this.submit();
                        input.remove();
                    });
                }
            })(jQuery);
        </script>

    </head>
    <body>
        <div id="md-placeholder" style="display: none"><c:out value="${metadataForm.map.metadata}" escapeXml="true"/></div>

        <tiles:insert definition="common.actionMessages"/>

        <html:form styleId="metadataForm" action="/editmetadata" enctype="multipart/form-data">
            <html:hidden property="action"/>
            <html:hidden property="alt_action"/>
            <html:hidden property="from" value="info@b3partners.nl"/>
            <html:hidden property="xsl" value="/infoText.xsl"/>
            <c:set var="subject"><fmt:message key="beheer.metadataeditor.subject" /></c:set>
            <c:set var="body"><fmt:message key="beheer.metadataeditor.body" /></c:set>
            <html:hidden property="subject" value="${subject}" />
            <html:hidden property="body" value="${body}" />

            <!-- auto-filled by Struts(?) -->
            <html:hidden property="id" />
            <html:hidden property="name" />

            <html:hidden property="metadata" styleId="metadata"/>

            <html:button property="saveButton" value="Opslaan" disabled="true" styleId="saveButton"/>

            <h4><fmt:message key="beheer.metadataeditor.downloaden" /></h4>
            <div id="saveStrictBlock" style="margin-bottom: 10px">
                <input type="checkbox" id="saveStrict" checked="checked" />
                <label for="saveStrict">Download strict volgens het Nederlandse metadata profiel op ISO 19115 voor geografie 1.2</label>
            </div>
            <html:button property="downloadButton" value="Downloaden" disabled="false" styleId="downloadButton"/>
        </html:form>

        <div id="metadataEditor"></div>

    </body>
</html>