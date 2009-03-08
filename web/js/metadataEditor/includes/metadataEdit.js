/*
B3P Metadata Editor is a ISO 19139 compliant metadata editor, 
that is preconfigured to use the Dutch profile for geography

Copyright 2006, 2007, 2008 B3Partners BV

This file is part of B3P Metadata Editor.

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
*/
//init();

function init() {
    addLoadEvent(initWithXmlString);
}

function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    }
    else {
        window.onload = function() {
            if (oldonload) {
                oldonload();
            }
            func();
        }
    }
}

function initWithXmlString() {
    debug("baseURL: " + baseURL);
    if (layerId != undefined) {
    	debug("layerId: " + layerId);
    }
    if (layerName != undefined) {
    	debug("layerName: " + layerName);
    }
    
    // if no metadata is present we start the editor with all elements empty
    if (metadataXML == "undefined" || metadataXML == null || trim(metadataXML) == "") {
        metadataXML = basicMetadataXML;
    }
    
    metadataXML = metadataXML.unescapeHTML();
    //debug(metadataXML);
    transformXml();
}

function transformXml() {
    var rawXmlDoc = jsXML.createDOMDocument();
    rawXmlDoc.async = false;
    rawXmlDoc.loadXML(metadataXML);
    
    //debug("Raw:");
    //debug(rawXmlDoc.xml);
    
    //var freeThreadedIfPossible = true;
    var ppXslDoc = jsXML.createDOMDocument(true);
    ppXslDoc.async = false;
    ppXslDoc.load(preprocessorXslFullPath);
    //debug("preprocessorXslFullPath: " + preprocessorXslFullPath);
    
    //debug("Preprocessor:");
    //debug(ppXslDoc.xml);
    
    //var rawPreprocessedXML = XML.transformToString(rawXmlDoc, ppXslDoc);
    // Global var. Also used by create section.
    preprocessor = new XML.Transformer(ppXslDoc);
    var rawPreprocessedXML = preprocessor.transformToString(rawXmlDoc);
    
    // Global var. Backend xml-document.
    xmlDoc = jsXML.createDOMDocument();
    xmlDoc.async = false;
    xmlDoc.loadXML(rawPreprocessedXML);
    
    debug("xmlDoc:");
    debugXmlDoc(xmlDoc);
    
    //var freeThreadedIfPossible = true;
    var xslDoc = jsXML.createDOMDocument(true);
    xslDoc.async = false;
    xslDoc.load(mainXslFullPath);
    
    //debug("Xsl:");
    //debugXmlDoc(xslDoc);
    
    // Global var. Also used by create section.
    xmlTransformer = new XML.Transformer(xslDoc);
    xmlTransformer.setParameter("basePath", baseFullPath);
   	xmlTransformer.setParameter("readonly", viewMode);
    xmlTransformer.transformAndAppend(xmlDoc, "write-root");
    
    insertTitle();
}

function insertTitle() {
		var titlePath = "/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString";
	  if (pathToRoot != "undefined" && pathToRoot != null && trim(pathToRoot) != "") {
	  	if (titlePath.indexOf(pathToRoot)!=0)
			titlePath = trim(pathToRoot) + titlePath;
    }

    var titleXMLNode = findNode(titlePath);
    if (titleXMLNode == null) {
        debug("title not found");
        return;
    }
    debug("titleXMLNode.nodeType: " + titleXMLNode.nodeType);
    debug("titleXMLNode.childNodes.length: " + titleXMLNode.childNodes.length);
    var titleString = "";
    for (var i = 0; i < titleXMLNode.childNodes.length; i++) {
        var node = titleXMLNode.childNodes[i];
        debug("node.nodeType: " + node.nodeType);
        if (node.nodeType == Node.TEXT_NODE) {
            debug("node.nodeValue: " + node.nodeValue);
            titleString += node.nodeValue;
        }
    }
    debug("titleString: " + titleString);
    
    var titleXHTMLElement = document.createElement("h5");
    titleXHTMLElement.innerHTML = "";
    titleXHTMLElement.appendChild(document.createTextNode(titleString));
    debug("titleXHTMLElement.innerHTML: " + titleXHTMLElement.innerHTML);
    
    var writeRootElement = document.getElementById("write-root");
    debug("writeRootElement.nodeType: " + writeRootElement.nodeType);
    debug("writeRootElement.firstChild.nodeType: " + writeRootElement.firstChild.nodeType);	
    writeRootElement.insertBefore(titleXHTMLElement, writeRootElement.firstChild);
}


// parameters: 
//				- path: is a XPath-path represented as a string; must start at the root
//				- newValue: value to saved at the node in the path
//				- newText: local text value to saved at the node in the path
function saveChangesInXMLDom(newValue, newText, path) {
    //debug("saveChangesInXMLDom");
    //debug("root tag: " + xmlDoc.nodeName);
    
    targetNode = findNode(path);
    if (targetNode != null) {
        while (targetNode.hasChildNodes()) {
            targetNode.removeChild(targetNode.firstChild);
        }
        
        // check if value is from picklist
        var isList = false;
        for( var x = 0; x < targetNode.attributes.length; x++ ) {
            if( targetNode.attributes[x].nodeName == 'codeListValue' ) {
                isList=true;
            }
        }
        
        var textNode;
        if (isList) {
            debug("saveChangesInXMLDom isList true: newText=" + newText + ", newValue=" + newValue);
            textNode = xmlDoc.createTextNode(newText);
            // Only add if attribute codeListValue exists
            targetNode.setAttribute('codeListValue',newValue)
        } 
        else {
            debug("saveChangesInXMLDom isList false: newText=" + newText + ", newValue=" + newValue);
            textNode = xmlDoc.createTextNode(newValue);
        }
        targetNode.appendChild(textNode);
        
        //debug("Saved changes in xml dom succesfully.");
    }
    else {
        alert("Save path in XML document not found. Changes will not be saved.");
    }
}

// Note: an xml document must have been assigned to the variable "xmlDoc" before calling this function
// Parameters: 
//	- path: is a XPath-path represented as a string; must start at the root of the xmlDoc
function findNode(path) {
		debug("findNode path: " + path);
    var pathArray = path.split("/");
    
    var targetNode = null;
    for (var i = 0; i < pathArray.length; i++) {
        targetNode = findChildNode(targetNode, pathArray[i]);
    }
    
    return targetNode;
}

// Note: an xml document must have been assigned to the variable "xmlDoc" before calling this function
function findChildNode(searchParent, targetRawChildTag) {
    if (targetRawChildTag == null || targetRawChildTag == "") {
        //debug("empty tagName");
        return null;
    }
    
    if (searchParent == null) {
        searchParent = xmlDoc;
    }
    
    //debug("searchParent: " + searchParent.nodeName);
    //debug("targetRawChildTag: " + targetRawChildTag);
    
    var splitQname = xpathQnameToArray(targetRawChildTag);
    if (splitQname == null)
        return null;
    
    //debug("prefix: " + splitQname[0]);
    //debug("name: " + splitQname[1]);
    //debug("index: " + splitQname[2]);	
    
    var searchChildren = searchParent.childNodes;
    if (searchChildren == null || searchChildren.length == 0) {
        debug("Childtree empty.");
        return null;
    }
    
    var searchChildNodeName, searchChildNode;
    var searchChildSplit;
    var correctChildCount = 0;
    for (var i = 0; i < searchChildren.length; i++) {
        searchChildNode = searchChildren[i];
        
        
        searchChildSplit = searchChildNode.nodeName.split(/[:]+/);
        //debug("searchChildSplit: " + searchChildSplit);
        if (searchChildSplit.length == 2) {
            searchChildNodeName = searchChildSplit[1];
        }
        else { // searchChildSplit.length == 1
            searchChildNodeName = searchChildSplit[0];
        }
        
        debug("searchChildNodeName: " + searchChildNodeName);		
        //debug("searchChildSplit.length: " + searchChildSplit.length);
        debug("searchChildNode.nodeType: " + searchChildNode.nodeType);
        //debug("searchChildNode.nodeName: " + searchChildNode.nodeName);
        //debug("targetChildTag: " + splitQname[1]);		
        if (searchChildNode.nodeType == Node.ELEMENT_NODE && searchChildNodeName == splitQname[1]) {
            // Xpath begint met tellen bij 1, dus eerst deze variable ophogen.
            correctChildCount++;
            if (correctChildCount == splitQname[2]) {
                //debug("correct child: " + searchChildNodeName);
                return searchChildNode;
            }
        }
    }
    
    debug("goede child niet gevonden bij parent: " + parent.nodeName);
    return null;
}


// E.g.: 
// prefix:tagName[3]
// to
// array:
// [0] == prefix or empty if not present
// [1] == local-name without prefix
// [2] == indexNr or 1 if not present
function xpathQnameToArray(qname) {
    var array = [];
    
    // defaults
    array[0] = "";
    array[1] = "";
    array[2] = 1;
    
    // split qname on '[' and ']'. For example "prefix:tagName[3]"
    var targetChildAndIndexUnfiltered = qname.split(/[\[\]]+/);
    var targetChildAndIndex = removeEmptyStringValuesFromArray(targetChildAndIndexUnfiltered);
    
    var targetRawPrefixAndChildTag;
    var targetChildIndex;
    
    if (targetChildAndIndex.length == 2) { // wel indexNr
        targetRawPrefixAndChildTag = targetChildAndIndex[0];
        array[2] = targetChildAndIndex[1];
    }
    else if (targetChildAndIndex.length == 1) { // geen indexNr. Standaard is 1
        targetRawPrefixAndChildTag = targetChildAndIndex[0];
        array[2] = 1;
    }
    else {
        debug("Incorrect tag name.");
        return null;
    }
    
    // split rawPrefixAndChildTag on ':'. For example "prefix:tagName"
    var targetPrefixAndChildTagUnfiltered = targetRawPrefixAndChildTag.split(/[:]+/);
    var targetPrefixAndChildTag = removeEmptyStringValuesFromArray(targetPrefixAndChildTagUnfiltered);
    
    var targetChildTag;
    var prefix;
    
    if (targetPrefixAndChildTag.length == 2) { // wel prefix
        array[0] = targetPrefixAndChildTag[0];
        array[1] = targetPrefixAndChildTag[1];
    }
    else if (targetPrefixAndChildTag.length == 1) { // geen prefix
        array[1] = targetPrefixAndChildTag[0];
    }
    else {
        debug("Incorrect tag name.");
        return null;
    }
    
    return array;
}

function removeEmptyStringValuesFromArray(array) {
    // filter out empty groups
    var newArray = [];
    var newIndex = 0;
    for (var i = 0; i < array.length; i++) {
        if (array[i] != "") {
            newArray[newIndex] = array[i];
            newIndex++;
        }
    }
    
    for (var i = 0; i < newArray.length; i++) {
        //debug(i + ": " + newArray[i]);
    }
    
    return newArray;
}

function checkForm(source) {
    var form = document.forms[0];
    
    addDateStampToXMLDom();
    
    var metadataHiddenInput = document.getElementById("metadata");
    if (metadataHiddenInput) {
        debug("metadataHiddenInput exists");
        metadataHiddenInput.value = xmlDoc.xml.escapeHTML();
    } 
    else {
        debug("metadataHiddenInput not exists");
        var xmlHiddenInput = document.createElement("input");
        xmlHiddenInput.setAttribute("type", "hidden");
        xmlHiddenInput.setAttribute("value", xmlDoc.xml.escapeHTML());	
        xmlHiddenInput.setAttribute("name", "metadata");
        form.appendChild(xmlHiddenInput);
    }
    
    var sourceName = source.getAttribute("name");
    if (sourceName != null && sourceName == "saveButton") {
        form.save.value = "t";
    }
    if (sourceName != null && sourceName == "sendButton") {
        form.send.value = "t";
    }
    if (sourceName != null && sourceName == "downloadButton") {
        form.download.value = "t";
    }
    
    
    form.submit();
    //self.close();
}

function addDateStampToXMLDom() {
    
		var dataPath = "/gmd:MD_Metadata/gmd:dateStamp/gco:Date";
    if (pathToRoot != "undefined" && pathToRoot != null && trim(pathToRoot) != "") {
			dataPath = trim(pathToRoot) + dataPath;
    }
    var dateNode = findNode(dataPath);
    if (dateNode == null) {
        debug("dateNode not found!");
        return;
    }
    debug("dateNode found!");
    while (dateNode.hasChildNodes()) {
        debug("deleting dateNode text: " + dateNode.firstChild.text);
        dateNode.removeChild(dateNode.firstChild);
    }
    
    var currentTime = new Date();
    var month = "0" + (currentTime.getMonth() + 1);
    if (month.length>2)
        month = month.substring(1);
    var day = "0" + currentTime.getDate();
    if (day.length>2)
        day = day.substring(1);
    var year = "" + currentTime.getFullYear();
    var currentDate = year + "-" + month + "-" + day;
    debug("current date: " + currentDate);
    
    var  textNode = xmlDoc.createTextNode(currentDate);
    dateNode.appendChild(textNode);
}