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
        
        <script type="text/javascript">
        /* <![CDATA[ */
        		//var debugMode = true;
        		var debugMode = false;
       			//var viewMode = "true";
        		var viewMode = "false";
        		
            var layerId = "<c:out value="${metadataForm.map.id}"/>";
            var layerName = "<c:out value="${metadataForm.map.name}"/>";
            var metadataXML = "<c:out value="${metadataForm.map.metadata}"/>";
 
            // path to root of ISO metadata element MD_Metadata, required for add/delete menu
						var pathToRoot = "/";
            var basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;MD_Metadata xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gml=\"http://www.opengis.net/gml\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd ./ISO19139_2005-10-08/gmd/gmd.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" /&gt;";
            var baseURL = document.URL.substring(0, document.URL.lastIndexOf("<html:rewrite page='' module='' />"));
            var baseFullPath = "<html:rewrite page='/js/metadataEditor/' module='' />";
            var mainXslFullPath = "<html:rewrite page='/js/metadataEditor/mdEdit_strict.xsl' module='' />";
            var preprocessorXslFullPath = "<html:rewrite page='/js/metadataEditor/preprocessors/metadataEditPreprocessor.xsl' module='' />";

            if (false) { // switch om ISO met CEN te combineren in een metadata document
                pathToRoot ="/metadata";
                basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;metadata&gt;&lt;/metadata&gt;";
                mainXslFullPath = "<html:rewrite page='/js/metadataEditor/mdEdit_default.xsl' module='' />";
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
        
        <script type="text/javascript">
            /* <![CDATA[ */
            addLoadEvent(initWithXmlString);
            /* ]]> */
        </script>	
        
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <link href="<html:rewrite page='/styles/metadataEdit.css' module='' />" rel="stylesheet" type="text/css" />
				<!--[if lte IE 7]> <link href="<html:rewrite page='/styles/metadataEdit-ie.css' module='' />" rel="stylesheet" type="text/css" /> <![endif]-->



    </head>
    <body>
        <html:form action="/editmetadata">
            <html:hidden property="action"/>
            <html:hidden property="alt_action"/>
            <html:hidden property="from" value="info@b3partners.nl"/>
            <html:hidden property="xsl" value="/infoText.xsl"/>
            <html:hidden property="subject" value="Metadata.xml van B3P Kaartenbalie"/>
            <html:hidden property="body" value="Bijgevoegd vindt u de metadata als xml zoals u dat heeft ingevuld op onze website.\n\nMet vriendelijke groeten\n\nB3Partners BV"/>
            
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
            <div id="write-root" onclick="click();"></div>
            <%--
            <input type="hidden" name="save" value="">
            <html:button property="saveButton" value="Opslaan" disabled="true" styleId="saveButton" onclick="checkForm(this);"/> 
            <h4>Direct metadata.xml downloaden</h4>
            <input type="hidden" name="download" value="">
            <html:button property="downloadButton" value="Downloaden" disabled="true" styleId="downloadButton" onclick="checkForm(this);"/> 
            --%>
            <h4>Email met metadata.xml verzenden</h4>
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
