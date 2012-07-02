/*
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
 * 
 * @author  Rachelle Scheijen
 */
package nl.b3p.kaartenbalie.service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.struts.KaartenbalieCrudAction;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GroupParser extends KaartenbalieCrudAction {

    protected static final Log log = LogFactory.getLog(GroupParser.class);
    private EntityManager em;

    public GroupParser(){
        try {
            em = getEntityManager();
        }
        catch(Exception e){}
    }

    /**
     * Returns all the organizations
     *
     * @return The organizations
     */
    public List<Organization> getGroups() {
        List<Organization> groups = em.createQuery("from Organization").getResultList();

        return groups;
    }

    /**
     * Returns all the organizations as a XML file. Warning : file is in
     * temporary directory!!
     *
     * @return The organizations as XML file
     */
    public File getGroupsAsXML() {
        String tempDir = System.getProperty("java.io.tmpdir");
        String sep = System.getProperty("file.separator");
        String targetLocation = tempDir+sep+"groups.xml";
        
        return getGroupsAsXML(targetLocation);
    }
    
    /**
     * Returns all the organizations as a XML file.
     *
     * @param  targetLocation  The target location on the disk
     * @return The organizations as XML file
     */
    public File getGroupsAsXML(String targetLocation) {
        try {
            List<Organization> groups = this.getGroups();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Organizations");
            doc.appendChild(rootElement);

            Element organization;
            Element id;
            Element name;
            Element code;
            for (Organization org : groups) {
                organization = doc.createElement("organization");
                rootElement.appendChild(organization);

                id = doc.createElement("id");
                id.appendChild(doc.createTextNode(org.getId().toString()));
                organization.appendChild(id);

                name = doc.createElement("name");
                name.appendChild(doc.createTextNode(org.getName()));
                organization.appendChild(name);

                code = doc.createElement("code");
                code.appendChild(doc.createTextNode(org.getCode()));
                organization.appendChild(code);
            }

            // write the content into xml file
            

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File file = new File(targetLocation);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            return file;
        } catch (Exception ex) {
            log.error("Error creating XML-document", ex);
            return null;
        }
    }
    
    public static void addRightsForAllLayers(String[] orgSelected, WfsServiceProvider sp,EntityManager em) throws Exception {
        if (orgSelected == null || sp == null) {
            return;
        }

        for (int i=0; i < orgSelected.length; i++) {
            Organization org = (Organization) em.find(Organization.class, new Integer(orgSelected[i]));

            addAllLayersToGroup(org, sp,em);
        }
    }
    
    public static void addRightsForAllLayers(String[] orgSelected, ServiceProvider sp,EntityManager em) throws Exception {
        if (orgSelected == null || sp == null) {
            return;
        }

        for (int i = 0; i < orgSelected.length; i++) {
            Organization org = (Organization) em.find(Organization.class, new Integer(orgSelected[i]));

            addAllLayersToGroup(org, sp,em);
        }
    }
    
    public static void addAllLayersToGroup(Organization org, WfsServiceProvider sp,EntityManager em) throws Exception {
        log.info("Updating WFS rights for :" + org.getName());

        Set wfsLayers = new HashSet();

        Set<WfsLayer> orgWfsLayerSet = org.getWfsLayers();
        for (WfsLayer l : orgWfsLayerSet) {
            WfsServiceProvider layerSp = l.getWfsServiceProvider();

            if (!layerSp.getAbbr().equals(sp.getAbbr())) {
                wfsLayers.add(l);

                log.info("Org wfs layer :" + l.getName());
            }
        }

        Set<WfsLayer> selectedLayers = sp.getWfsLayers();
        for (WfsLayer l : selectedLayers) {
            wfsLayers.add(l);

            log.info("Wfs layer :" + l.getName());
        }

        org.setWfsLayers(wfsLayers);

        em.merge(org);
        em.flush();
    }

    public static void addAllLayersToGroup(Organization org, ServiceProvider sp,EntityManager em) throws Exception {
        Set wmsLayers = new HashSet();

        Set<Layer> orgWmsLayerSet = org.getLayers();
        for (Layer l : orgWmsLayerSet) {
            ServiceProvider layerSp = l.getServiceProvider();

            if (!layerSp.getAbbr().equals(sp.getAbbr())) {
                wmsLayers.add(l);
            }
        }

        Set<Layer> selectedLayers = sp.getAllLayers();
        for (Layer l : selectedLayers) {
            wmsLayers.add(l);
        }

        org.setLayers(wmsLayers);

        em.merge(org);
        em.flush();
    }
}