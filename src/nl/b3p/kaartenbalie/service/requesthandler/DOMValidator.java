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
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class DOMValidator extends DefaultHandler {

    private boolean documentIsValid = true;
    private boolean documentHadWarnings = false;

    public void parseAndValidate(ByteArrayInputStream uri) throws SAXParseException, ParserConfigurationException, SAXException, IOException {
        Document doc = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(this);
        doc = db.parse(uri);
        doc = null;
    }

    /** Warning. */
    public void warning(SAXParseException ex) throws SAXParseException {
        throw ex;
    }

    /** Error. */
    public void error(SAXParseException ex) throws SAXParseException {
        throw ex;
    }

    /** Fatal error. */
    public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
    }
}
