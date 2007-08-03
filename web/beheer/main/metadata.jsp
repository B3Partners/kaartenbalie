<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<%@include file="/templates/taglibs.jsp" %>
<tiles:importAttribute/>

<c:set var="form" value="${metadataForm}"/>
<c:set var="xml" value="${form.map.xml}"/>


<html:html xhtml="true">
    <head>
	
	<title>Metadata Editor</title>

	<script type="text/javascript">
	/* <![CDATA[ */
	    xmlJs = "<c:out value="${xml}" escapeXml="true"/>";
	/* ]]> */
	</script>	
	
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_dhtml.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_edit.js' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/nczXMLDOMWrapper.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/XML.Transformer.js.jsp' module='' />"></script>
        
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/crossBrowser.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEditBrowser.js' module='' />"></script>
        <script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEdit.js.jsp' module='' />"></script>
		
	<%-- hier hebben we unescapeHTML uit nodig --%>
	<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/scriptaculous-js-1.7.0/lib/prototype.js' module='' />"></script>
	
        <link rel="stylesheet" type="text/css" href="<html:rewrite page='/js/metadataEditor/includes/metadataEdit.css.jsp' module='' />" />
	
    </head>
    
    <body>
        <html:form action="/metadata" onsubmit="checkForm(); return false;">
            <html:hidden property="id" />
            <html:hidden property="name" />
            <%--
		<html:hidden property="xml" /><!-- dynamisch met javascript doen bij submit (theform.appendChild)-->
	    --%>
            
	    <html:button property="save" value="Save" onclick="checkForm();"/> 
			

	    
        </html:form>
	
        <div id="writeroot"></div>
	
	<div class="hidden">
		<!-- location of the edit and add templates stylesheets (used by Add Section code) -->
		<span id="editStylesheetFile"><html:rewrite page='/js/metadataEditor/MNP_Metadata_Beheerder_Edit_Intern.xsl' module='' /></span>
		<span id="addStylesheetFile"><html:rewrite page='/js/metadataEditor/Preprocessors/Add_MNP_Metadata_Beheerder_Edit_Templates.xsl' module='' /></span>
		
		<!-- plus/minus images used for expanding/collapsing sections -->
		<img id="plus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />"></img>
		<img id="minus_img" class="plus-minus" src="<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />"></img>
	</div>
	
	
    </body>
</html:html>

    