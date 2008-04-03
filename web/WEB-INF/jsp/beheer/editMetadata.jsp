<?xml version="1.0" encoding="UTF-16"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<%@include file="/WEB-INF/jsp/taglibs.jsp" %>
<tiles:importAttribute/>

<html:html xhtml="true">
	<head>
		<meta http-equiv="pragma" content="no-cache" />

		<script type="text/javascript">
		/* <![CDATA[ */
			var debugMode = true;
			//var debugMode = false;
			
			var layerId = "<c:out value="${metadataForm.map.id}"/>";
			var layerName = "<c:out value="${metadataForm.map.name}"/>";
			var metadataXML = "<c:out value="${metadataForm.map.metadata}"/>";
			
			var baseURL = document.URL.substring(0, document.URL.lastIndexOf("<html:rewrite page='' module='' />"));			
			var basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-16\"?&gt;&lt;MD_Metadata xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\" xmlns:gml=\"http://www.opengis.net/gml\" xsi:schemaLocation=\"http://www.isotc211.org/2005/gmd ./ISO19139_2005-10-08/gmd/gmd.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" /&gt;";
			
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

		<link rel="stylesheet" type="text/css" href="<html:rewrite page='/styles/metadataEdit.css' module='' />" />

	</head>

	<body>
		<html:form action="/editmetadata" onsubmit="checkForm(this); return false;">
			<html:hidden property="id" />
			<html:hidden property="name" />
			<html:hidden property="metadata" styleId="metadata"/>
			<html:hidden property="save" value="t"/>
						
			<html:submit property="save" value="Opslaan" disabled="true" styleId="saveButton" onclick="checkForm(this);"/> 
			
			<div id="write-root" onclick="click();"></div>

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
