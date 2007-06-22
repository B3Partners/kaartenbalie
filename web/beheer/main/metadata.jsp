<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@include file="/templates/taglibs.jsp" %>
<tiles:importAttribute/>

<html:html xhtml="true">
    <head>
        <title>Metadata Editor</title>
	<base href="<html:rewrite page='/js/metadataEditor'/>" />
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_dhtml.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_edit.js' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/nczXMLDOMWrapper.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/XML.Transformer.js' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/crossBrowser.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/Metadata_Beheerder_Edit.js' module='' />"></script>	
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/browserEdit.jsp' module='' />"></script>
		
		<%-- hier hebben we unescapeHTML uit nodig --%>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/scriptaculous-js-1.7.0/lib/prototype.js' module='' />"></script>
        
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/js/metadataEditor/includes/Metadata_Beheerder_Edit.jsp' module='' />" />
    </head>
    
    <body>
		<c:set var="form" value="${metadataForm}"/>
        <c:set var="xml" value="${form.map.xml}"/>
        <c:set var="xsl" value="${form.map.xsl}"/>	
	
        
        <html:form action="/metadata" onsubmit="checkForm(); return false;">
            <html:hidden property="id" />
            <html:hidden property="name" />
            <%--
			<html:hidden property="xml" /><!-- dynamisch met javascript doen bij submit (theform.appendChild)-->
			<html:hidden property="xsl" />
			--%>
            
	    <html:button property="save" value="Save" onclick="checkForm();"/> 
			
            <div id="writeroot"></div>
            
	    <script type="text/javascript">initWithXmlString("<c:out value="${xml}" escapeXml="true"/>");</script>			            
        </html:form>

    </body>
</html:html>

    