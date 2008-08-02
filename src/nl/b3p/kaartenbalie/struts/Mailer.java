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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.7 $ $Date: 2005-12-31 15:28:38 +0100 (za, 31 dec 2005) $
 */
public class Mailer {

    protected Log log = LogFactory.getLog(this.getClass());
    // --------------------------------------------------------- public methods
    private String mailTo = null;
    private String mailFrom = null;
    private String subject = null;
    private String mailCc = null;
    private String mailBcc = null;
    private String mailHost = null;
    private String body = null;
    private String attachment = null;
    private String attachmentName = null;
    private DataSource attachmentDataSource = null;
    private String mailer = null;
    private String footer = null;
    private Hashtable extraParams = null;
    private MessageResources messages = null;
    private Locale locale = null;
    private HttpSession session = null;
    private String xsl = null;
    private boolean returnReceipt = false;

    /** Creates new Mailer */
    public Mailer() {
    }

    /** Getter for property mailTo.
     * @return Value of property mailTo.
     */
    public java.lang.String getMailTo() {
        return mailTo;
    }

    /** Setter for property mailTo.
     * @param mailTo New value of property mailTo.
     */
    public void setMailTo(java.lang.String mailTo) {
        this.mailTo = mailTo;
    }

    /** Getter for property mailFrom.
     * @return Value of property mailFrom.
     */
    public java.lang.String getMailFrom() {
        return mailFrom;
    }

    /** Setter for property mailFrom.
     * @param mailFrom New value of property mailFrom.
     */
    public void setMailFrom(java.lang.String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /** Getter for property subject.
     * @return Value of property subject.
     */
    public java.lang.String getSubject() {
        return subject;
    }

    /** Setter for property subject.
     * @param subject New value of property subject.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    /** Getter for property mailCc.
     * @return Value of property mailCc.
     */
    public java.lang.String getMailCc() {
        return mailCc;
    }

    /** Setter for property mailCc.
     * @param mailCc New value of property mailCc.
     */
    public void setMailCc(java.lang.String mailCc) {
        this.mailCc = mailCc;
    }

    /** Getter for property mailBcc.
     * @return Value of property mailBcc.
     */
    public java.lang.String getMailBcc() {
        return mailBcc;
    }

    /** Setter for property mailBcc.
     * @param mailBcc New value of property mailBcc.
     */
    public void setMailBcc(java.lang.String mailBcc) {
        this.mailBcc = mailBcc;
    }

    /** Getter for property mailHost.
     * @return Value of property mailHost.
     */
    public java.lang.String getMailHost() {
        return mailHost;
    }

    /** Setter for property mailHost.
     * @param mailHost New value of property mailHost.
     */
    public void setMailHost(java.lang.String mailHost) {
        this.mailHost = mailHost;
    }

    /** Getter for property body.
     * @return Value of property body.
     */
    public java.lang.String getBody() {
        return body;
    }

    /** Setter for property body.
     * @param body New value of property body.
     */
    public void setBody(java.lang.String body) {
        this.body = body;
    }

    /** Getter for property attachment.
     * @return Value of property attachment.
     */
    public java.lang.String getAttachment() {
        return attachment;
    }

    /** Setter for property attachment.
     * @param attachment New value of property attachment.
     */
    public void setAttachment(java.lang.String attachment) {
        this.attachment = attachment;
    }

    /** Getter for property attachmentName.
     * @return Value of property attachmentName.
     */
    public java.lang.String getAttachmentName() {
        return attachmentName;
    }

    /** Setter for property attachmentName.
     * @param attachmentName New value of property attachmentName.
     */
    public void setAttachmentName(java.lang.String attachmentName) {
        this.attachmentName = attachmentName;
    }

    /** Getter for property mailer.
     * @return Value of property mailer.
     */
    public java.lang.String getMailer() {
        return mailer;
    }

    /** Setter for property mailer.
     * @param mailer New value of property mailer.
     */
    public void setMailer(java.lang.String mailer) {
        this.mailer = mailer;
    }

    /** Getter for property footer.
     * @return Value of property footer.
     */
    public java.lang.String getFooter() {
        return footer;
    }

    /** Setter for property footer.
     * @param mailer New value of property footer.
     */
    public void setFooter(java.lang.String footer) {
        this.footer = footer;
    }

    public void send() throws AddressException, MessagingException, IOException, Exception {
        send(null);
    }

    public ActionMessages send(ActionMessages errors) throws AddressException, MessagingException, IOException, Exception {

        HtmlEmail email = new HtmlEmail();

        String[] ds = null;
        if (mailTo != null && mailTo.trim().length() != 0) {
            ds = mailTo.split(",");
            for (int i = 0; i < ds.length; i++) {
                try {
                    email.addTo(ds[i]);
                } catch (MessagingException me) {
                    log.error("To email address not valid: ", me);
                    if (errors != null) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.message.emailproblem", ds[i]));
                    }
                }
            }
        }
        if (mailCc != null && mailCc.trim().length() != 0) {
            ds = mailCc.split(",");
            for (int i = 0; i < ds.length; i++) {
                try {
                    email.addCc(ds[i]);
                } catch (MessagingException me) {
                    log.error("Cc email address not valid: ", me);
                    if (errors != null) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.message.emailproblem", ds[i]));
                    }
                }
            }
        }
        if (mailBcc != null && mailBcc.trim().length() != 0) {
            ds = mailBcc.split(",");
            for (int i = 0; i < ds.length; i++) {
                try {
                    email.addBcc(ds[i]);
                } catch (MessagingException me) {
                    log.error("Bcc email address not valid: ", me);
                    if (errors != null) {
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.message.emailproblem", ds[i]));
                    }
                }
            }
        }

        email.setFrom(mailFrom);
        email.setSubject(subject);
        email.setHostName(mailHost);

        if (isReturnReceipt()) {
            email.addHeader("Disposition-Notification-To", mailFrom);
        }

        if (attachmentName == null) {
            attachmentName = "attachment";
        }
        if (attachment != null) {
            URL attachUrl = null;
            try {
                attachUrl = new URL(attachment);
                email.attach(attachUrl, attachmentName, attachmentName);
            } catch (MalformedURLException mfue) {
            }
        }
        if (attachmentDataSource != null) {
            email.attach(attachmentDataSource, attachmentName, attachmentName);
        }

        email.setMsg(createHTML());

        // send the email
        email.send();

        return errors;
    }

    protected List convertDelim2ArrayList(String delim) {
        if (delim == null) {
            return null;
        }
        String[] ds = delim.split(",");
        ArrayList al = new ArrayList();
        for (int i = 0; i < ds.length; i++) {
            al.add(ds[i]);
        }
        return al;
    }

    public Document createFormDOM() throws DOMException, ParserConfigurationException {

        Document doc = XMLUtils.newDocument();

        // Insert the root element node
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);

        Element element = doc.createElement("to");
        Text text = doc.createTextNode(mailTo + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("from");
        text = doc.createTextNode(mailFrom + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("subject");
        text = doc.createTextNode(subject + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("body");
        text = doc.createTextNode(body + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("cc");
        text = doc.createTextNode(mailCc + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("bcc");
        text = doc.createTextNode(mailBcc + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("attachment");
        text = doc.createTextNode(attachment + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        element = doc.createElement("footer");
        text = doc.createTextNode(footer + "");
        element.appendChild(text);
        rootElement.appendChild(element);

        if (extraParams != null) {
            Enumeration cenum = extraParams.keys();
            while (cenum.hasMoreElements()) {
                String theParameter = (String) cenum.nextElement();
                if (theParameter == null) {
                    continue;
                }
                element = doc.createElement(theParameter);
                text = doc.createTextNode((String) extraParams.get(theParameter));
                element.appendChild(text);
                rootElement.appendChild(element);
            }
        }

        return doc;

    }

    protected String createHTML() throws FileNotFoundException, Exception,
            TransformerConfigurationException, TransformerException, ParserConfigurationException {

        Document xml = createFormDOM();
        if (xml == null) {
            throw new Exception("No XML created!");
        }

        TransformerFactory tf = TransformerFactory.newInstance();

        File xslFile = new File(MyEMFDatabase.localPath(xsl));
        File xmlPath = new File(xslFile.getParent());
        Source xslSource = new SAXSource(new InputSource(new FileInputStream(xslFile)));
        /* Zorg ervoor dat in de XSL met relatieve URL's bestanden kunnen worden
         * geinclude
         */
        xslSource.setSystemId(xmlPath.toURI().toString());

        Transformer transformer = tf.newTransformer(xslSource);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        transformer.transform(new DOMSource(xml), new StreamResult(output));

        return output.toString();
    }

    protected String createTxt() {
        // De gevraagde info wordt aan de body toegevoegd
        StringBuffer ib = new StringBuffer();
        boolean doAdd = false;

        if (extraParams.get("fullname") != null && !extraParams.get("fullname").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.fullname"));
            ib.append(" ");
            ib.append((String) extraParams.get("fullname"));
            doAdd = true;
        }
        if (extraParams.get("company") != null && !extraParams.get("company").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.company"));
            ib.append(" ");
            ib.append((String) extraParams.get("company"));
            doAdd = true;
        }
        if (extraParams.get("address") != null && !extraParams.get("address").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.address"));
            ib.append(" ");
            ib.append((String) extraParams.get("address"));
            doAdd = true;
        }
        if (extraParams.get("zipcode") != null && !extraParams.get("zipcode").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.zipcode"));
            ib.append(" ");
            ib.append((String) extraParams.get("zipcode"));
            doAdd = true;
        }
        if (extraParams.get("city") != null && !extraParams.get("city").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.city"));
            ib.append(" ");
            ib.append((String) extraParams.get("city"));
            doAdd = true;
        }
        if (extraParams.get("country") != null && !extraParams.get("country").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.country"));
            ib.append(" ");
            ib.append((String) extraParams.get("country"));
            doAdd = true;
        }
        if (extraParams.get("phone") != null && !extraParams.get("phone").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.phone"));
            ib.append(" ");
            ib.append((String) extraParams.get("phone"));
            doAdd = true;
        }
        if (extraParams.get("fax") != null && !extraParams.get("fax").equals("")) {
            ib.append("\n");
            ib.append(getMessages().getMessage(locale, "message.fax"));
            ib.append(" ");
            ib.append((String) extraParams.get("fax"));
            doAdd = true;
        }

        StringBuffer sb = new StringBuffer();
        if (doAdd) {
            sb.append(getMessages().getMessage(locale, "message.requestor"));
            sb.append("\n");
            sb.append(ib.toString());
            sb.append("\n\n\n");
        }

        ib = new StringBuffer();
        doAdd = false;

        Enumeration cenum = extraParams.keys();
        while (cenum.hasMoreElements()) {
            String theParameter = (String) cenum.nextElement();
            if (theParameter == null) {
                continue;
            }
            String theValue = (String) extraParams.get(theParameter);
            if (theParameter.equals("fullname") ||
                    theParameter.equals("company") ||
                    theParameter.equals("address") ||
                    theParameter.equals("zipcode") ||
                    theParameter.equals("receipt") ||
                    theParameter.equals("city") ||
                    theParameter.equals("country") ||
                    theParameter.equals("phone") ||
                    theParameter.equals("fax")) {
                continue;
            }
            ib.append("\n");
            ib.append(theParameter);
            if (!theValue.equals("on")) {
                ib.append(" ");
                ib.append(theValue);
            }
            doAdd = true;
        }
        if (doAdd) {
            sb.append(getMessages().getMessage(locale, "message.inquiry"));
            sb.append("\n");
            sb.append(ib.toString());
            sb.append("\n\n\n");
        }

        if (body != null && !body.equals("")) {
            sb.append(getMessages().getMessage(locale, "message.body"));
            sb.append("\n");
            sb.append(body);
        }

        if (footer != null) {
            sb.append("\n\n\n");
            sb.append(footer);
        }

        return sb.toString();
    }

    public Hashtable getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Hashtable extraParams) {
        this.extraParams = extraParams;
    }

    public MessageResources getMessages() {
        return messages;
    }

    public void setMessages(MessageResources messages) {
        this.messages = messages;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getXsl() {
        return xsl;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    public boolean isReturnReceipt() {
        return returnReceipt;
    }

    public void setReturnReceipt(boolean returnReceipt) {
        this.returnReceipt = returnReceipt;
    }

    public DataSource getAttachmentDataSource() {
        return attachmentDataSource;
    }

    public void setAttachmentDataSource(DataSource attachmentDataSource) {
        this.attachmentDataSource = attachmentDataSource;
    }
}
