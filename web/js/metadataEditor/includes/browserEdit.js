
function init() {
	
	
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

function initWithXmlString(xmlString) {
	// TODO:
	//zet speciale tekens uit xmlstring om met RegEx Object (&lt; &gt; etc...)
	
	alert(xmlString);
	var xmlDoc = jsXML.createDOMDocument();
	xmlDoc.async = false;
	xmlDoc.loadXML(xmlString);
	alert(xmlDoc.xml);
	
	var xslDoc = jsXML.createDOMDocument();
	xslDoc.async = false;
	xslDoc.loadXML(xmlString);	
	alert(xslDoc.xml);
	
	XML.transform(xmlDoc, xslDoc, "writeroot");
	
	xmlDocInit();
}

function putXMLinHiddenNode() {
	document.getElementById('xml').setAttribute("value", xmlDoc.xml);
	return true;
}

function createTable()
{
	//alert(xmlDoc.xml);
	var x = xmlDoc.getElementsByTagName('Esri');
	//alert(x[0]);
	var newEl = document.createElement('table');
	newEl.setAttribute('cellPadding',5);
	var tmp = document.createElement('tbody');
	newEl.appendChild(tmp);
	var row = document.createElement('tr');
	for (j=0;j<x[0].childNodes.length;j++)
	{
		if (x[0].childNodes[j].nodeType != 1) continue;
		var container = document.createElement('th');
		var theData = document.createTextNode(x[0].childNodes[j].nodeName);
		container.appendChild(theData);
		row.appendChild(container);
	}
	tmp.appendChild(row);
	for (i=0;i<x.length;i++)
	{
		var row = document.createElement('tr');
		for (j=0;j<x[i].childNodes.length;j++)
		{
			if (x[i].childNodes[j].nodeType != 1) continue;
			var container = document.createElement('td');
			var theData = document.createTextNode(x[i].childNodes[j].firstChild.nodeValue);
			container.appendChild(theData);
			row.appendChild(container);
		}
		tmp.appendChild(row);
	}
	document.getElementById('writeroot').appendChild(newEl);
}