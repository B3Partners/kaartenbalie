<%@include file="/templates/taglibs.jsp" %>
<tiles:importAttribute/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html:html>
    <head>
        <title>Metadata Editor</title>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_dhtml.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_edit.js' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/nczXMLDOMWrapper.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/XML.Transformer.js' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/Metadata_Beheerder_Edit.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/browserEdit.js' module='' />"></script>
        
        
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/js/metadataEditor/includes/generic.css' module='' />" />
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/js/metadataEditor/includes/Metadata_Beheerder_Edit.css' module='' />" />
    </head>
    
    <body onload="init()" changed="false" metaEditType="fullXML" onmousemove="mouseEvent()">
        <c:set var="form" value="${metadataForm}"/>
        <c:set var="mainid" value="${form.map.id}"/>
        
        <html:form action="/metadata">
            <html:hidden property="id" />
            <html:hidden property="name" />
            <html:hidden property="xml" /> 
            ID: <c:out value="${mainid}"/>
            ID: <c:out value="${form.map.name}"/>
            ID: <c:out value="${form.map.xml}"/>
            
            
            <p>Test 2222</p>
            
            
            <!--
            <div id="writeroot"></div>
            -->
            
        </html:form>
    </body>
</html:html>

    