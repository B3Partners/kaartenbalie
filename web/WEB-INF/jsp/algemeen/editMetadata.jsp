<%--
B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
for authentication/authorization, pricing and usage reporting.

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Kaartenbalie.

B3P Kaartenbalie is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

B3P Kaartenbalie is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
--%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<html:html xhtml="true">
    <head>
        <meta http-equiv="pragma" content="no-cache" />

        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/defaults.js' module='' />"></script>
        <script type="text/javascript">
            /* <![CDATA[ */
            //var debugMode = true;
            var debugMode = false;
            //var viewMode = "true";
            var viewMode = "<c:out value="${metadataForm.map.viewMode}"/>";
            //var strictMode = "true";
            var strictMode = "<c:out value="${metadataForm.map.strictMode}"/>";
        		
            var layerId = "<c:out value="${metadataForm.map.id}"/>";
            var layerName = "<c:out value="${metadataForm.map.name}"/>";
            var metadataXML = "<c:out value="${metadataForm.map.metadata}" escapeXml='false'/>";
 
            var baseURL = document.URL.substring(0, document.URL.lastIndexOf("<html:rewrite page='' module='' />"));
            var baseFullPath = "<html:rewrite page='/js/metadataEditor/' module='' />";
            var preprocessorXslFullPath = baseFullPath + "preprocessors/metadataEditPreprocessor.xsl";

            // path to root of ISO metadata element MD_Metadata, required for add/delete menu
            var pathToRoot;
            var basicMetadataXML;
            var mainXslFullPath;
            if (strictMode != "true") {
                pathToRoot = Defaults.Loose.pathToRoot;
                basicMetadataXML = Defaults.Loose.basicMetadataXML;
                mainXslFullPath = baseFullPath + Defaults.Loose.mainXsl;
            } else {
                pathToRoot = Defaults.Strict.pathToRoot;
                basicMetadataXML = Defaults.Strict.basicMetadataXML;
                mainXslFullPath = baseFullPath + Defaults.Strict.mainXsl;
            }
            /* ]]> */
        </script>	

        <title>Metadata Editor - <c:out value="${metadataForm.map.name}"/></title>

        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/StringBuffer.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/crossBrowser.js' module='' />"></script>		
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEdit.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEditBrowser.js' module='' />"></script>

        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_dhtml.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_edit.js' module='' />"></script>

        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/nczXMLDOMWrapper.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/XML.Transformer.js' module='' />"></script>

        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/scriptaculous-js-1.7.0/lib/prototype.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/wiki2html.js' module='' />"></script>

        <script type="text/javascript">
            /* <![CDATA[ */
            addLoadEvent(initWithXmlString);
            /* ]]> */
        </script>	

        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css" />
        <link href="<html:rewrite page='/styles/metadataEdit.css' module='' />" rel="stylesheet" type="text/css" />
        <!--[if lte IE 7]> 
            <link href="<html:rewrite page='/styles/metadataEdit-ie.css' module='' />" rel="stylesheet" type="text/css" />
        <![endif]-->

    </head>
    <body>
        <html:form action="/editmetadata">
            <html:hidden property="action"/>
            <html:hidden property="alt_action"/>
            <html:hidden property="from" value="info@b3partners.nl"/>
            <html:hidden property="xsl" value="/infoText.xsl"/>
            <c:set var="subject"><fmt:message key="algemeen.editMetadata.subject" /></c:set>
            <c:set var="body"><fmt:message key="algemeen.editMetadata.body" /></c:set>
            <html:hidden property="subject" value="${subject}" />
            <html:hidden property="body" value="${body}" />

            <html:hidden property="id" />
            <html:hidden property="name" />
            <html:hidden property="metadata" styleId="metadata"/>
            <div class="messages"> 
                <html:messages id="message" message="true" >
                    <div id="error">
                        <c:out value="${message}" escapeXml="false"/>
                    </div>
                </html:messages> 
            </div> 
            <div id="write-root"></div>
            <%--
            <input type="hidden" name="save" value="">
            <html:button property="saveButton" value="Opslaan" disabled="true" styleId="saveButton" onclick="checkForm(this);"/> 
            <h4>Direct metadata.xml downloaden</h4>
            <input type="hidden" name="download" value="">
            <html:button property="downloadButton" value="Downloaden" disabled="true" styleId="downloadButton" onclick="checkForm(this);"/> 
            --%>
            <h4><fmt:message key="algemeen.editMetadata.title" /></h4>
            <table>
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
                        <input type="hidden" name="send" value="">
                        <html:button property="sendButton" value="Verzenden" styleId="sendButton" onclick="checkForm(this);"/> 
                    </td>
                </tr>
            </table>
        </html:form>
        <div class="hidden">
            <!-- plus/minus images used for expanding/collapsing sections, FF only -->
            <img id="plus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />"></img>
            <img id="minus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />"></img>
        </div>
    </body>
    <!-- another head: prevents IE cache bug/feature -->
    <head>
        <meta http-equiv="pragma" content="no-cache" />
    </head>
</html:html>
