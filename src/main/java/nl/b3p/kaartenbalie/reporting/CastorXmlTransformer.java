/*
 * $Id: CastorXmlTransformer.java 769 2005-06-30 05:52:01Z Chris $
 */
package nl.b3p.kaartenbalie.reporting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;

import org.apache.avalon.framework.logger.Log4JLogger;
import org.apache.fop.messaging.MessageHandler;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.apache.fop.apps.Driver;
import org.xml.sax.InputSource;

/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 769 $ $Date: 2009-01-07 07:52:01 +0200 (Thu, 30 Jun 2005) $
 */
public class CastorXmlTransformer {

    protected static Log log = LogFactory.getLog(CastorXmlTransformer.class);
    protected static Logger fopLogger = Logger.getLogger("fop");

    private static final String ENCODING = "ISO-8859-1";
    /*
     * rapport type constanten
     */
    public static final String HTML = "html";
    public static final String XSL = "xsl";
    public static final String PDF = "pdf";
    public static final String XML = "xml";
    private static Map contentTypes = new HashMap();
    static {
        getContentTypes().put(HTML, "text/html");
        getContentTypes().put(XSL, "text/xml");
        getContentTypes().put(PDF, "application/pdf");
        getContentTypes().put(XML, "text/xml");
    }

    /**
     * @return the contentTypes
     */
    public static Map getContentTypes() {
        return contentTypes;
    }

    private String xslField = null;
    private String xsdField = null;
    private String xslHttpPath = null;
    private String xslFilePath = null;

    public CastorXmlTransformer() {
        this(null, null);
    }
    
    public CastorXmlTransformer(
            String xslField,
            String xslFilePath) {
        this(xslField, null, null, xslFilePath);
    }

    public CastorXmlTransformer(
            String xslField,
            String xsdField,
            String xslHttpPath,
            String xslFilePath) {

        this.xslField = xslField;
        this.xsdField = xsdField;
        this.xslHttpPath = xslHttpPath;
        this.xslFilePath = xslFilePath;
        return;
    }

    public String createXml(Object rapport) throws Exception {
        StringWriter xmlString = new StringWriter();
        try {
            Marshaller marshal = new Marshaller(xmlString);
            marshal.setEncoding(ENCODING);
            marshal.marshal(rapport);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return xmlString.toString();
    }

    public String createHtml(Object rapport) throws Exception {
        String xslFullPath = findXslFilePath();
        if (xslFullPath == null) {
            return null;
        }

        StringWriter xmlString = new StringWriter();
        SAXTransformerFactory tFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        log.debug("transformer used: " + tFactory.getClass().getName());
        try {
            TransformerHandler tHandler = tFactory.newTransformerHandler(new StreamSource(xslFullPath));
            tHandler.setResult(new StreamResult(xmlString));
            Marshaller marshal = new Marshaller(tHandler);
            marshal.marshal(rapport);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return xmlString.toString();
    }

    public String createXsl(Object rapport) throws Exception {
        String xslFullPath = findXslHttpPath();

        StringWriter xmlString = new StringWriter();
        Marshaller marshal = new Marshaller(xmlString);
        if (xslFullPath != null) {
            String xslURL = xslFullPath + xslField;
            marshal.addProcessingInstruction("xml-stylesheet", "type='text/xsl' href='" + xslURL + "'");
            log.debug(" Adding xsl url to xml for local transformation: " + xslURL);
        }
        if (xsdField != null && xsdField.length() > 0) {
            String xsdURL = xslHttpPath + xsdField;
            marshal.setNoNamespaceSchemaLocation(xsdURL);
        }
        marshal.setEncoding(ENCODING);
        marshal.marshal(rapport);
        return xmlString.toString();
    }

    public ByteArrayOutputStream createPdf(Object rapport) throws Exception {
        String xslFullPath = findXslFilePath();
        if (xslFullPath == null) {
            return null;
        }

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        File xslFile = new java.io.File(xslFullPath);
        File xmlPath = new java.io.File(xslFile.getParent());
        Source xslSource = new SAXSource(new InputSource(new FileInputStream(xslFile)));
        /* Zorg ervoor dat in de XSL met relatieve URL's bestanden kunnen worden
         * geinclude
         */
        xslSource.setSystemId(xmlPath.toURI().toString());

        /* Zorg ervoor dat fop relatieve URLs vanaf hier resolved... */
        org.apache.fop.configuration.Configuration.put("baseDir", xmlPath.getCanonicalPath());

        objectToPDF(rapport, xslSource, byteArray, true);
        return byteArray;
    }

    /**
     * Transformeert een Object dat door Castor kan worden gemarshalled naar
     * een PDF met behulp van apache-fop.
     *
     * @param marshallable Het door Castor te marshallen object
     * @param xslFopSource Source van het XSL-FO document
     * @param out Output voor de gegenereerde PDF
     * @param validateMarshal Of Castor het marshallable object moet valideren of
     *          het voldoet aan het schema.
     */
    public static void objectToPDF(Object marshallable, Source xslFopSource, OutputStream out, boolean validateMarshal)
            throws TransformerConfigurationException, IOException, MarshalException, ValidationException {

        /* Deze methode maakt geen gebruik van een tijdelijk XML bestand maar
         * maakt gebruik van een SAXTransformerFactory die direct de SAX events
         * van Castor kan afhandelen en kan transformeren met de xslFopSource.
         * Het resultaat van de transformatie wordt met SAX events direct
         * doorgestuurd naar de ContentHandler van de Apache FOP Driver.
         */

        /* TODO check of optimalizatie van Marshalling zoals beschreven op
         * http://castor.org/xml-faq.html#How-can-I-speed-up-marshalling/unmarshalling-performance?
         * zin heeft OF dat Apache FOP sowieso de meeste tijd inneemt.
         */

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        if (!transformerFactory.getFeature(SAXTransformerFactory.FEATURE)) {
            throw new UnsupportedOperationException("SAXTransformerFactory required");
        }

        SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) transformerFactory;
        //log.debug("SAXTransformerFactory class: " + saxTransformerFactory.getClass());
        TransformerHandler transformer = saxTransformerFactory.newTransformerHandler(xslFopSource);

        Driver driver = new Driver();

        Log4JLogger logger = new Log4JLogger(fopLogger);
        driver.setLogger(logger);
        MessageHandler.setScreenLogger(logger);
        driver.setRenderer(Driver.RENDER_PDF);
        driver.setOutputStream(out);

        Result res = new SAXResult(driver.getContentHandler());
        transformer.setResult(res);

        Marshaller marshaller = new Marshaller(transformer);
        marshaller.setValidation(validateMarshal);
        marshaller.marshal(marshallable);
    }

    protected String findXslFilePath() throws Exception {
        //Checking xsl as file
        boolean xslFileAvailable = false;
        try {
            java.io.File file = new java.io.File(xslFilePath + xslField);
            if (file.exists()) {
                xslFileAvailable = true;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (!xslFileAvailable) {
            throw new Exception("xsl file is ongeldig: " + xslFilePath + xslField);
        }
        return xslFilePath + xslField;
    }

    protected String findXslHttpPath() throws Exception {
        //Checking xsl as file
        boolean xslHttpAvailable = false;
        java.io.File file = new java.io.File(xslHttpPath + xslField);
        if (file.exists()) {
            xslHttpAvailable = true;
        }
        if (!xslHttpAvailable) {
            throw new Exception("xsl file is ongeldig: " + xslHttpPath + xslField);
        }
        return xslHttpPath + xslField;
    }
}
