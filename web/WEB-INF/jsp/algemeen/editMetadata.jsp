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
        <!-- mde -->
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEditor.js' module='' />"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $.mde.logMode = true;

                $("#metadataEditor").mde({
                    xml: Sarissa.unescape($("#md-placeholder").text()),
                    baseFullPath: "<html:rewrite page='/js/metadataEditor/' module='' />",
                    profile: "nl_md_1.2_with_fc",
                    changed: function(changed) {
                    }
                });

                $("#sendButton").click(function(event) {
                    $("#metadata").val(Sarissa.escape($("#metadataEditor").mde("save", {
                        profile: $("#saveStrict").is(":checked") ? "nl_md_1.2" : "nl_md_1.2_with_fc"
                    })));
                    $("#metadataForm").submitAsEvent("send");
                });
             });

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

            <h4><fmt:message key="algemeen.editMetadata.title" /></h4>
            <table>
                <tr>
                    <td><b title="Metadata verzenden strict volgens het Nederlandse metadata profiel op ISO 19115 voor geografie 1.2. De attributen (FeatureCatalog) worden weggelaten.">Strict</b></td>
                    <td><input type="checkbox" id="saveStrict" checked="checked" title="Metadata verzenden strict volgens het Nederlandse metadata profiel op ISO 19115 voor geografie 1.2. De attributen (FeatureCatalog) worden weggelaten."/></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.fullname"/></B></td>
                    <td><html:text property="fullname" size="22" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.company"/></B></td>
                    <td><html:text property="company" size="22" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.address"/></B></td>
                    <td><html:text property="address" size="50" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.zipcode"/></B></td>
                    <td><html:text property="zipcode" size="8" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.city"/></B></td>
                    <td><html:text property="city" size="22" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.country"/></B></td>
                    <td><html:text property="country" size="22" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.phone"/></B></td>
                    <td><html:text property="phone" size="15" /></td>
                </tr>
                <tr>
                    <td><B><fmt:message key="message.fax"/></B></td>
                    <td><html:text property="fax" size="15" /></td>
                </tr>
                <tr>
                    <td>
                        <B><fmt:message key="message.to"/></B>
                    </td>
                    <td>
                        <html:text property="to" size="50"/>
                        <html:button property="sendButton" value="Verzenden" styleId="sendButton"/>
                    </td>
                </tr>
            </table>
        </html:form>

        <div id="metadataEditor"></div>

    </body>
</html>