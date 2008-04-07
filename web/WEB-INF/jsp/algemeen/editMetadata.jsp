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
            
            var layerId = "<c:out value="${metadataForm.map.id}"/>";
            var layerName = "<c:out value="${metadataForm.map.name}"/>";
            var metadataXML = "<c:out value="${metadataForm.map.metadata}"/>";
            
            var baseURL = document.URL.substring(0, document.URL.lastIndexOf("<html:rewrite page='' module='' />"));			
                var basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;MD_Metadata xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gml=\"http://www.opengis.net/gml\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd ./ISO19139_2005-10-08/gmd/gmd.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" /&gt;";
                
                var mainXslFullPath = "<html:rewrite page='/js/metadataEditor/metadataEditISO19139.xsl' module='' />";
                var preprocessorXslFullPath = "<html:rewrite page='/js/metadataEditor/preprocessors/metadataEditPreprocessor.xsl' module='' />";			
                var preprocessorTemplatesXslFullPath = "<html:rewrite page='/js/metadataEditor/preprocessors/metadataEditPreprocessorTemplates.xsl' module='' />";						
                var addElementsXslFullPath = "<html:rewrite page='/js/metadataEditor/includes/addElements.xsl' module='' />";						
                var baseFullPath = "<html:rewrite page='/js/metadataEditor/' module='' />";
                
                var PLUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />";
                var MINUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />";
                var MENU_IMAGE = "<html:rewrite page='/js/metadataEditor/images/arrow.gif' module='' />";
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
        
        
        <link href="<html:rewrite page='/styles/main.css' module='' />" rel="stylesheet" type="text/css">
        <link href="<html:rewrite page='/styles/metadataEdit.css' module='' />" rel="stylesheet" type="text/css" />
        
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
            <!-- plus/minus images used for expanding/collapsing sections -->
            <img id="plus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />"></img>
            <img id="minus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />"></img>
        </div>
    </body>
    <!-- another head: prevents IE cache bug/feature -->
    <head>
        <meta http-equiv="pragma" content="no-cache" />
    </head>
</html:html>
