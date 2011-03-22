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
package nl.b3p.kaartenbalie.struts;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.LayerTreeSupport;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

public class MetadataAction extends KaartenbalieCrudAction {

    private final static Log log = LogFactory.getLog(MetadataAction.class);
    protected static final String SEND = "send";
    protected static final String DOWNLOAD = "download";
    protected static final String GET = "get";
    protected static String METADATA_LINE_SEPARATOR = "<pseudohtml:br/>";
    // We map the prefixes to URIs
    protected static final NamespaceContext ctx = new NamespaceContext() {

        public String getNamespaceURI(String prefix) {
            String uri;
            if (prefix.equals("gmd")) {
                uri = "http://www.isotc211.org/2005/gmd";
            } else if (prefix.equals("xsi")) {
                uri = "http://www.w3.org/2001/XMLSchema-instance";
            } else if (prefix.equals("gml")) {
                uri = "http://www.opengis.net/gml";
            } else if (prefix.equals("gts")) {
                uri = "http://www.isotc211.org/2005/gts";
            } else if (prefix.equals("gco")) {
                uri = "http://www.isotc211.org/2005/gco";
            } else {
                uri = null;
            }
            return uri;
        }

        // Dummy implementation - not used!
        public Iterator getPrefixes(String val) {
            return null;
        }

        // Dummy implemenation - not used!
        public String getPrefix(String uri) {
            return null;
        }
    };

    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();

        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(SEND);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.send.ok");
        crudProp.setAlternateForwardName(SUCCESS);
        crudProp.setAlternateMessageKey("beheer.send.problem");
        map.put(SEND, crudProp);

        crudProp = new ExtendedMethodProperties(DOWNLOAD);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.download.ok");
        crudProp.setAlternateForwardName(SUCCESS);
        crudProp.setAlternateMessageKey("beheer.download.problem");
        map.put(DOWNLOAD, crudProp);

        crudProp = new ExtendedMethodProperties(GET);
        map.put(GET, crudProp);

        return map;
    }

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Principal user = request.getUserPrincipal();
        if (user != null) {
            showLayerTree(request);
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Principal user = request.getUserPrincipal();
        if (user != null) {
            String layerUniqueName = FormUtils.nullIfEmpty(dynaForm.getString("id"));
            if (layerUniqueName != null) {
                Layer layer = getLayerByUniqueName(layerUniqueName);
                populateMetadataEditorForm(layer, dynaForm, request);
            }
        }
        return mapping.findForward(SUCCESS);
    }

    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Principal user = request.getUserPrincipal();
        if (user != null) {
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager();
            String layerUniqueName = FormUtils.nullIfEmpty(dynaForm.getString("id"));
            Layer layer = null;
            if (layerUniqueName != null) {
                layer = getLayerByUniqueName(layerUniqueName);
            }
            if (layer == null) {
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }

            String metadata = null;
            try {
                metadata = getMetadata(dynaForm, layer);
            } catch (Exception e) {
                log.error("error parsing metadata xml: ", e);
                prepareMethod(dynaForm, request, LIST, EDIT);
                addAlternateMessage(mapping, request, null, e.getMessage());
                return edit(mapping, dynaForm, request, response);
            }

            layer.setMetadata(metadata);

            em.merge(layer);
            // flush used because database sometimes doesn't update (merge) quickly enough
            em.flush();

            populateMetadataEditorForm(layer, dynaForm, request);
        }
        return getDefaultForward(mapping, request);
    }

    public ActionForward get(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        User user = (User) request.getUserPrincipal();
        User dbUser = null;
        try {
            dbUser = (User) em.createQuery("from User u where "
                    + "u.id = :userid").setParameter("userid", user.getId()).getSingleResult();
        } catch (NoResultException nre) {
            log.error("No serviceprovider for user found.");
            throw new Exception("No serviceprovider for user found.");
        }


        String metadata = null;
        if (dbUser != null) {
            Set userLayers = dbUser.getLayers();
            String layerUniqueName = (String) dynaForm.get("id");
            if (layerUniqueName != null && layerUniqueName.length() > 0) {
                Layer layer = null;
                try {
                    layer = getLayerByUniqueName(layerUniqueName);
                } catch (Exception exception) {
                    log.error("Can not get layer " + layerUniqueName
                            + " for metadata, cause: " + exception.getLocalizedMessage());
                }
                if (layer != null && LayerTreeSupport.hasVisibility(layer, userLayers)) {
                    metadata = layer.getMetadata();
                }
            }
        }
        String xsl = FormUtils.nullIfEmpty(dynaForm.getString("xsl"));
        if (xsl == null || metadata == null) {
            return xmlDownload(metadata, mapping, dynaForm, request, response);
        }
        String xslPath = MyEMFDatabase.localPath(xsl);
        return htmlDownload(metadata, xslPath, mapping, dynaForm, request, response);
    }

    public ActionForward download(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String metadata = null;
        try {
            metadata = getMetadata(dynaForm);
        } catch (Exception e) {
            log.error("error parsing metadata xml: ", e);
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, null, e.getMessage());
            return edit(mapping, dynaForm, request, response);
        }
        return xmlDownload(metadata, mapping, dynaForm, request, response);
    }

    public ActionForward xmlDownload(String metadata, ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (metadata == null) {
            String layerUniqueName = (String) dynaForm.get("id");
            log.error("Metadata not available for " + layerUniqueName);
            metadata = "<error>Metadata not available for layer '" + layerUniqueName + "'</error>";
        }
        response.setContentType("text/xml");
        response.setContentLength(metadata.length());

        String fileName = "metadata.xml";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
//        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\";");

        try {
            Writer rw = response.getWriter();
            rw.write(metadata);
        } catch (IOException ex) {
            log.error("error parsing metadata xml: ", ex);
        }
        return null;
    }

    public ActionForward htmlDownload(String metadata, String stylesheet, ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (metadata == null) {
            return xmlDownload(metadata, mapping, dynaForm, request, response);
        }

        response.setContentType("text/html");
        response.setContentLength(metadata.length());

        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(stylesheet));

            ByteArrayInputStream bais = new ByteArrayInputStream(metadata.getBytes());
            transformer.transform(new StreamSource(bais),
                    new StreamResult(response.getWriter()));
        } catch (Exception e) {
            log.error("error parsing metadata xml: ", e);
        }
        return null;
    }

    public ActionForward send(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = (String) dynaForm.get("id");
        String name = (String) dynaForm.get("name");
        String metadata = getMetadata(dynaForm);

        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            populateMetadataEditorForm(id, name, metadata, dynaForm, request);

            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            
            return getAlternateForward(mapping, request);
        } else {
            Mailer mailer = new Mailer();
            populateMailerObject(mailer, dynaForm, request);

            String type = "text/xml; charset=utf-8";
            ByteArrayDataSource bads = new ByteArrayDataSource(metadata.getBytes("utf-8"), type);
            mailer.setAttachmentDataSource(bads);
            mailer.setAttachmentName("metadata.xml");

            ActionMessages messages = getMessages(request);
            messages = mailer.send(messages);
            saveMessages(request, messages);

            populateMetadataEditorForm(id, name, metadata, dynaForm, request);

            if (messages.isEmpty()) {
                addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
            } else {
                addAlternateMessage(mapping, request, null, "zie hiervoor");
            }
            return mapping.findForward(SUCCESS);
        }
    }

    protected void populateMailerObject(Mailer infoMailer, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        Locale locale = request.getLocale();
        HttpSession session = request.getSession();

        infoMailer.setMessages(messages);
        infoMailer.setLocale(locale);
        infoMailer.setSession(session);

        String xsl = dynaForm.getString("xsl");
        if (xsl != null && xsl.length() > 0) {
            infoMailer.setXsl(xsl);
        }

        String mailh = dynaForm.getString("mailhost");
        if ((mailh == null) || (mailh.length() == 0)) {
            addMessage(request, "error.message.nomailhost");
        }
        infoMailer.setMailHost(mailh);
        infoMailer.setMailer("B3PKaartenbalie");

        if (request.getParameter("receipt") != null && !request.getParameter("receipt").equals("")) {
            log.info("Return receipt actief.");
            infoMailer.setReturnReceipt(true);
        }

        String to = dynaForm.getString("to");
        if ((to == null) || (to.length() == 0)) {
            addMessage(request, "error.message.noto");
        }
        infoMailer.setMailTo(to);

        String from = dynaForm.getString("from");
        if ((from == null) || (from.length() == 0)) {
            addMessage(request, "error.message.nofrom");
        }
        infoMailer.setMailFrom(from);

        String tempFrom = infoMailer.getMailFrom();
        if (request.getParameter("reverse") != null && !request.getParameter("reverse").equals("")) {
            log.info("email reverse actief.");
            tempFrom = infoMailer.getMailTo();
            infoMailer.setMailTo(infoMailer.getMailFrom());
            infoMailer.setMailFrom(tempFrom);
        }

        String cc = dynaForm.getString("cc");
        if (cc != null && !cc.equals("") && tempFrom != null) {
            cc = cc + ", " + tempFrom;
        }
        if (cc == null || cc.equals("")) {
            cc = tempFrom;
        }
        infoMailer.setMailCc(cc);

        String bcc = dynaForm.getString("bcc");
        if (bcc != null && bcc.length() != 0) {
            infoMailer.setMailBcc(bcc);
        }

        infoMailer.setSubject(dynaForm.getString("subject"));
        infoMailer.setBody(dynaForm.getString("body"));

        Hashtable params = new Hashtable();
        Enumeration cenum = request.getParameterNames();
        while (cenum.hasMoreElements()) {
            String theParameter = (String) cenum.nextElement();
            if (theParameter.equals("to")
                    || theParameter.equals("cc")
                    || theParameter.equals("bcc")
                    || theParameter.equals("from")
                    || theParameter.equals("subject")
                    || theParameter.equals("body")) {
                continue;
            }
            params.put(theParameter, request.getParameter(theParameter));
        }
        infoMailer.setExtraParams(params);

        infoMailer.setFooter(messages.getMessage(locale, "message.moreinfo"));

        return;
    }

    /* FF heeft problemen met xml dat IE produceert. Alleen lege tags met een
     * additionele namespace worden verkeerd geparsed
     * <code>
     *  <x:y x:z="" xmlns:x="een namespace"></x:y>
     * </code>
     * wordt door FF gepasrsed als:
     * <code>
     *  <y x:z="" xmlns:x="een namespace"></x:y>
     * </code>
     * De afsluittag is dus niet goed.
     * Reparsen van IE output lijkt zaak te verbeteren, maar een eenmale fout
     * xml (in de ogen van FF, want is wel valid) wordt hiermee niet hersteld.
     */
    private String reparseXML(String metadata, Layer l) throws Exception {
        StringReader sr = new StringReader(metadata);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(sr));

        String uuid = null;
        Node uuidNode = null;
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(ctx);
//        uuid = xpath.evaluate("//gmd:fileIdentifier/gco:CharacterString", doc);
        NodeList nl = (NodeList) xpath.evaluate("//gmd:fileIdentifier/gco:CharacterString",
                doc, XPathConstants.NODESET);
        if (nl.getLength() > 0) {
            uuidNode = nl.item(0);
            uuid = getTextContents(uuidNode);
        }
        if (uuid == null || uuid.trim().length() == 0) {
            uuid = UUID.randomUUID().toString();
            uuidNode.setTextContent(uuid);
        }

        if (l != null) {
            String title = null;
            Node titleNode = null;
            nl = (NodeList) xpath.evaluate("//gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString",
                    doc, XPathConstants.NODESET);
            if (nl.getLength() > 0) {
                titleNode = nl.item(0);
                title = getTextContents(titleNode);
            }
            if (title == null || title.trim().length() == 0) {
                title = l.getTitle();
                titleNode.setTextContent(title);
            }

            //TODO andere waarden uit capabilities overbrengen
        }

        StringWriter sw = new StringWriter();
        OutputFormat format = new OutputFormat(doc);
        format.setIndenting(true);
        XMLSerializer serializer = new XMLSerializer(sw, format);
        serializer.serialize(doc);

        return sw.toString();
    }

    private String getTextContents(Node node) {
        if (node == null) {
            return null;
        }

        NodeList childNodes;
        StringBuffer contents = new StringBuffer();

        childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                contents.append(childNodes.item(i).getNodeValue());
            }
        }
        return contents.toString();
    }

    private String getMetadata(DynaValidatorForm dynaForm) throws Exception {
        return getMetadata(dynaForm, null);
    }

    private String getMetadata(DynaValidatorForm dynaForm, Layer l) throws Exception {
        String metadata = StringEscapeUtils.unescapeXml((String) dynaForm.get("metadata"));
        if (metadata == null || metadata.length() == 0) {
            throw new Exception("No metadata found!");
        }
        String newMetadata = reparseXML(metadata, l);
        if (newMetadata == null || newMetadata.length() == 0) {
            throw new Exception("No metadata found!");
        }
        return newMetadata;
    }

    private void populateMetadataEditorForm(Layer layer, DynaValidatorForm dynaForm, HttpServletRequest request) {
        if (layer != null) {
            populateMetadataEditorForm(layer.getUniqueName(), layer.getTitle(), layer.getMetadata(), dynaForm, request);
        }
    }

    private void populateMetadataEditorForm(String id, String name, String metadata, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", id);
        dynaForm.set("name", name);
        dynaForm.set("metadata", metadata);
    }
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------

    private void showLayerTree(HttpServletRequest request) throws Exception {
        try {
            log.debug("Getting entity manager ......");
            EntityManager em = getEntityManager();
            User sesuser = (User) request.getUserPrincipal();
            if (sesuser == null) {
                log.debug("No user principal found.");
                return;
            }
            User user = (User) em.find(User.class, sesuser.getId());
            if (user == null) {
                log.debug("No database user matching principal found.");
                return;
            }

            Set userLayers = user.getLayers();
            JSONObject root = this.createTree("Kaartlagen", userLayers);
            request.setAttribute("layerList", root);
        } catch (Throwable e) {
            log.warn("Error creating EntityManager: ", e);
        }
    }
}
