<%@page contentType="text/javascript"%>
<%@include file="/templates/taglibs.jsp" %>


//alert("hij doet iets in deze file: browserEdit.jsp!");
init();

function init() {
    xslFullPath = "<html:rewrite page='/js/metadataEditor/MNP_Metadata_Beheerder_Edit_Intern.xsl' module='' />";
    addLoadEvent(initWithXmlString);
    
    PLUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_plus.gif' module='' />";
    MINUS_IMAGE = "<html:rewrite page='/js/metadataEditor/images/xp_minus.gif' module='' />";
    MENU_IMAGE = "<html:rewrite page='/js/metadataEditor/images/arrow.gif' module='' />";
    //alert("hij is in init!");
}

function addLoadEvent(func) {
  var oldonload = window.onload;
  if (typeof window.onload != 'function') {
    window.onload = func;
  } else {
    window.onload = function() {
      if (oldonload) {
        oldonload();
      }
      func();
    }
  }
}

function sarissaTest() {
	//sarissa:
	/*
	var xmlDoc = Sarissa.getDomDocument();
    xmlDoc.async = false;
	xmlDoc.load("/metadataEditor/MetadataTestCEN4_beheerder.xml");

    var xslDoc = Sarissa.getDomDocument();
	xslDoc.async = false;
	xslDoc.load("/metadataEditor/MNP_Metadata_Beheerder_Edit_Intern.xsl");
	*/
	
	/*var xmlSerializer = new XMLSerializer();
	
    var xmlhttpXml = new XMLHttpRequest();
    xmlhttpXml.open("GET", "/metadataEditor/MetadataTestCEN4_beheerder.xml", false);
    // if needed set header information using the setRequestHeader method
    xmlhttpXml.send('');
    //var xmlDoc = xmlhttpXml.responseXML;
    var xmlDoc = xmlhttpXml.responseXML;
    //alert(xmlhttpXml.responseText);
    alert(xmlDoc);
    //alert(xmlSerializer.serializeToString(xmlhttpXml.responseXML));
    */

	
    /*var xmlhttpXsl = new XMLHttpRequest();
    xmlhttpXsl.open("GET", "/metadataEditor/MNP_Metadata_Beheerder_Edit_Intern.xsl", false);
    // if needed set header information using the setRequestHeader method
    xmlhttpXsl.send('');
    //var xslDoc = xmlhttpXsl.responseXML;
    var xslDoc = xmlhttpXsl.responseXML;  
    //alert(xmlhttpXsl.responseText);
    alert(xslDoc);    
    //alert(xmlSerializer.serializeToString(xmlhttpXsl.responseXML));    
	
	var processor = new XSLTProcessor();
	processor.importStylesheet(xslDoc);	
	
	var fragment = processor.transformToFragment(xmlDoc, document);
	var elem = document.getElementById("writeroot");
	elem.appendChild(fragment);

	//Sarissa.updateContentFromURI("/metadataEditor/MetadataTestCEN4_beheerder.xml", document.getElementById("writeroot"), processor);	

	xmlDocInit();*/
	
	//oud, nu nog beter werkend:
	var xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.load("file://../MetadataTestCEN4_beheerder.xml");
	alert(xmlDoc.xml);
	
	var xslDoc = jsXML.createDOMDocument();
	xslDoc.async = false;
	xslDoc.load("file://../MNP_Metadata_Beheerder_Edit_Intern.xslt");	
	alert(xslDoc.xml);
	
	//var elem = document.getElementById("writeroot");
	
	// Transform
	//var result = xmlDoc.transformNode(xslDoc);
	//alert(result);
	//alert(htmlAsText);
	//alert(new XMLSerializer().serializeToString(xmlDoc.documentElement));
	XML.transform(xmlDoc, xslDoc, "writeroot");
	
	xmlDocInit();
}

function initWithXmlString() {
	alert("hij is in initWithXmlString!");
	var xmlString = xmlJs;
	
	alert("escaped:\n\n" + xmlString);
	xmlString = xmlString.unescapeHTML();
	//xslString = xslString.unescapeHTML();

	alert(xmlString);
	var xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(xmlString);

	//alert(xslFullPath);
	var xslDoc = jsXML.createDOMDocument();
	xslDoc.async = false;
	//xslDoc.loadXML(xslString);
	xslDoc.load(xslFullPath);	
	alert(xslDoc.xml);
	
	XML.transform(xmlDoc, xslDoc, "writeroot");
	
	xmlDocInit();
}

function test() {
    //niets
}

// TODO: nog uitwerken:
function checkForm() {
    if (true == false) {
	document.getElementById("metadataForm").submit();
    }
}

function putXMLinHiddenNode() {
	document.getElementById('xml').setAttribute("value", xmlDoc.xml);
	return true;
}