/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
function XML() { }


/**
 * This XML.Transformer class encapsulates an XSL stylesheet.
 * If the stylesheet parameter is a URL, we load it.
 * Otherwise, we assume it is an appropriate DOM Document.
 */
XML.Transformer = function(stylesheet) {
    // Load the stylesheet if necessary.
    this.stylesheet = stylesheet;
    if (typeof XSLTProcessor != "undefined") {
        this.processor = new XSLTProcessor();
        //debug("voor creëren FF proc");		
        this.processor.importStylesheet(this.stylesheet);
        //debug("na creëren FF proc");		
    }
    else if (typeof ActiveXObject != "undefined") { //!!window.ActiveX
        var xslt = new ActiveXObject("Msxml2.XSLTemplate.3.0");
        //debug("voor toevoegen sheet IE");
        xslt.stylesheet = stylesheet;
        //debug("voor creëren IE proc");
        this.processor = xslt.createProcessor();
        //debug("na creëren IE proc");		
    }
    else {
        alert("No suitable XSLT Processor found for your browser. Please use either IE or Firefox.");
    }
};

// if ff: transforms to nodeset; if ie: transforms to string
XML.Transformer.prototype._transform = function(node) {
    if ("transformToFragment" in this.processor) {
        return this.processor.transformToFragment(node, document);
    }
    else if ("transform" in this.processor) {
        this.processor.input = node;
        this.processor.transform();
        return this.processor.output;
    }
    else if ("transformNode" in node) {
        return node.transformNode(this.stylesheet);
    }
    else {
        throw "XSLT is not supported in this browser";
    }
}

// always transforms to string
XML.Transformer.prototype.transformToString = function(node) {
    var fragmentOrString = this._transform(node);
    if ("transformToFragment" in this.processor) {
        var objXMLSerializer = new XMLSerializer;
        return objXMLSerializer.serializeToString(fragmentOrString);
    }
    return fragmentOrString;
}

// always appends result to the specified element/id
XML.Transformer.prototype.transformAndAppend = function(node, element) {
    // If element is specified by id, look it up.
    if (typeof element == "string") {
        element = document.getElementById(element);
    }
    if ("transformToFragment" in this.processor) {
        element.innerHTML = "";
        element.appendChild(this._transform(node));
    }
    else {
        element.innerHTML = this._transform(node);
    }
};

XML.Transformer.prototype.setParameter = function(key, value) {
    if ("setParameter" in this.processor) {
        this.processor.setParameter(null, key, value);
    }
    else if ("addParameter" in this.processor) {
        this.processor.addParameter(key, value);
    }
};

// _transform: deze niet gebruiken
XML._transform = function(xmldoc, stylesheet) {
    var transformer = new XML.Transformer(stylesheet);
    return transformer._transform(xmldoc);
}

XML.transformToString = function(xmldoc, stylesheet) {
    var transformer = new XML.Transformer(stylesheet);
    return transformer.transformToString(xmldoc);
}

XML.transformAndAppend = function(xmldoc, stylesheet, element) {
    var transformer = new XML.Transformer(stylesheet);
    transformer.transformAndAppend(xmldoc, element);
}
