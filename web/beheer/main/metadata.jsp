<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">


<%@include file="/templates/taglibs.jsp" %>
<tiles:importAttribute/>

<html:html xhtml="true">
	<head>
		<meta http-equiv="pragma" content="no-cache" />

		<script type="text/javascript">
		/* <![CDATA[ */
			var debugMode = true;
			
			var layerId = "<c:out value="${metadataForm.map.id}"/>";
			var layerName = "<c:out value="${metadataForm.map.name}"/>";
			var metadataXML = "<c:out value="${metadataForm.map.metadata}"/>";
			
			var basicMetadataXML = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;&lt;MD_Metadata xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gmd=\"http://www.isotc211.org/2005/gmd\" xmlns:gco=\"http://www.isotc211.org/2005/gco\"/&gt;";
			
			var xslFullPath = "<html:rewrite page='/js/metadataEditor/metadataEditISO19139.xsl' module='' />";
			var preprocessorFullPath = "<html:rewrite page='/js/metadataEditor/preprocessors/metadataEditPreprocessor.xsl' module='' />";			
			var basePath = "<html:rewrite page='/js/metadataEditor/' module='' />";
			
			var PLUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />";
			var MINUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />";
			var MENU_IMAGE = "<html:rewrite page='/js/metadataEditor/images/arrow.gif' module='' />";
		/* ]]> */
		</script>	

		<title>Metadata Editor - <c:out value="${metadataForm.map.name}"/></title>

		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/crossBrowser.js' module='' />"></script>
		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEdit.js' module='' />"></script>
		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/metadataEditBrowser.js' module='' />"></script>

		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_dhtml.js' module='' />"></script>
		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/generic_edit.js' module='' />"></script>

		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/nczXMLDOMWrapper.js' module='' />"></script>
		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/XML.Transformer.js' module='' />"></script>
		
		<script type="text/javascript" src="<html:rewrite page='/js/metadataEditor/includes/scriptaculous-js-1.7.0/lib/prototype.js' module='' />"></script>		

		<link rel="stylesheet" type="text/css" href="<html:rewrite page='/js/metadataEditor/includes/metadataEdit.css' module='' />" />

	</head>

	<body>
		<html:form action="/metadata" onsubmit="checkForm(this); return false;">
			<html:hidden property="id" />
			<html:hidden property="name" />
			<html:hidden property="save" value="val"/>			

			<html:submit property="save" value="Save" disabled="true" styleId="saveButton" onclick="checkForm(this);"/> 

			<div id="writeroot"></div>

		</html:form>

		<div class="hidden">
			<%--
			<!-- location of the edit and add templates stylesheets (used by Add Section code) -->
			<span id="editStylesheetFile"><html:rewrite page='/js/metadataEditor/MNP_Metadata_Beheerder_Edit_Intern.xsl' module='' /></span>
			<span id="addStylesheetFile"><html:rewrite page='/js/metadataEditor/Preprocessors/Add_MNP_Metadata_Beheerder_Edit_Templates.xsl' module='' /></span>
			--%>

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