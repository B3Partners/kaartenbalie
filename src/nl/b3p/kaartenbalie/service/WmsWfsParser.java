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
package nl.b3p.kaartenbalie.service;

import java.io.IOException;
import java.util.*;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.gis.CredentialsParser;
import nl.b3p.kaartenbalie.struts.ServerAction;
import nl.b3p.ogc.sld.SldNamedLayer;
import nl.b3p.ogc.sld.SldReader;
import nl.b3p.ogc.sld.SldUserStyle;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.LayerDomainResource;
import nl.b3p.wms.capabilities.Style;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author rachelle
 */
abstract public class WmsWfsParser extends ServerAction {
    protected static final Log log = LogFactory.getLog(WMSParser.class);
    protected static long maxResponseTime = 10000;
    public static final String OK = "OK";
    public static final String SAVE_ERRORKEY = "Error_saving";
    public static final String ERROR_DELETE_OLD_PROVIDER = "Error_deleting_old_provider";
    protected Exception exception;
    public static final String ERROR_INVALID_URL = "Not a WMS URL";
    public static final String SERVICE_STATUS_ERROR = "FOUT";
    public static final String SERVICE_STATUS_OK = "GOED";
    public static final String SERVER_CONNECTION_ERROR = SERVER_CONNECTION_ERRORKEY;
    public static final String MALFORMED_CAPABILITY_ERROR = MALFORMED_CAPABILITY_ERRORKEY;
    public static final String UNSUPPORTED_WMSVERSION_ERROR = UNSUPPORTED_WMSVERSION_ERRORKEY;
    protected ArrayList<String> parseMessages;
    
    public WmsWfsParser(){
        parseMessages    = new ArrayList<String>();
    }
    
    /** 
     * Method for saving a new service provider from input of a user.
     *
     * @param request The HTTP Request we are processing.
     * @param dynaForm The DynaValidatorForm bean for this request.
     * 
     * @return The status code
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    abstract public String saveProvider(HttpServletRequest request,DynaValidatorForm dynaForm) throws Exception;
    
    abstract public int test(DynaValidatorForm dynaForm) throws Exception;
    
    abstract public int batchUpdate(DynaValidatorForm dynaForm) throws Exception ;
    
    abstract public int batchUpdate(DynaValidatorForm dynaForm,String prefix) throws Exception;
    
    abstract public String deleteConfirm(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception;
    
    /**
     * Method for deleting a serviceprovider.
     *
     * @param dynaForm The DynaValidatorForm bean for this request. 
     * @param request The HTTP Request we are processing. 
     *
     * @return the status code
     *
     * @throws Exception
     */
    abstract public String delete(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception;
       
    protected Set<Style> getSldStylesSet(List<SldNamedLayer> allNamedLayers,Layer layer)
            throws Exception {

        Set<Style> styles = new HashSet<Style>();
        SldReader sldReader = new SldReader();
        //get only the named layers for this layer
        List<SldNamedLayer> namedLayers=sldReader.getNamedLayers(allNamedLayers, layer.getName());
        
        for (SldNamedLayer namedLayer : namedLayers) {
            List<SldUserStyle> userStyles = namedLayer.getUserStyles();//sldReader.getUserStyles(namedLayer);

            for (SldUserStyle userStyle : userStyles) {
                Style style = new Style();
                style.setLayer(layer);
                style.setName(userStyle.getName());
                style.setTitle(userStyle.getName());
                style.setSldPart(userStyle.getSldPart());
                styles.add(style);
            }            
        }
        return styles;
    }
    
    /* Tries to find a specified layer given for a certain ServiceProvider.
     *
     * @param layers the set with layers which the method has to surch through
     * @param orgLayer the layer to be found
     *
     * @return layer if found.
     */
    // <editor-fold defaultstate="" desc="checkLayer(Layer orgLayer, Set layers) method.">

    protected Layer checkLayer(Layer orgLayer, Set layers) {
        if (layers == null || layers.isEmpty()) {
            return null;
        }
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            Layer layer = (Layer) it.next();
            if (orgLayer.getName() == null && layer.getName() == null
                    && orgLayer.getTitle().equalsIgnoreCase(layer.getTitle())) {
                return layer;
            }
            if (orgLayer.getName() != null && layer.getName() != null
                    && orgLayer.getName().equalsIgnoreCase(layer.getName())) {
                return layer;
            }
            Layer foundLayer = checkLayer(orgLayer, layer.getLayers());
            if (foundLayer != null) {
                return foundLayer;
            }
        }
        return null;
    }
    
    protected String getUniqueStyleName(Set<Style> styles, String name) throws Exception {        
        return getUniqueStyleName(styles,name,null);
    }
    
    protected String getUniqueStyleName(Set<Style> styles, String name, Integer tries) throws Exception {    
        if (tries!=null && tries==10)
            throw new Exception("Can't create unique name for style");
                
        String newName=name;
        if (tries!=null)
            newName+=tries;
        
        Iterator<Style> it = styles.iterator();
        boolean unique=true;
        while (it.hasNext()&& unique){
            Style s= it.next();
            if (s.getName().equals(newName))
                unique=false;
        }
        if (!unique){
            if (tries==null)
                tries= new Integer("0");
            tries++;            
            return getUniqueStyleName(styles, name, tries);
        }
        return newName;
    }
    
    protected void setMetadataFromLayerSource(Layer layer, Layer oldLayer,B3PCredentials credentials) {
        String mdUrl = null;
        Set ldrs = layer.getDomainResource();
        if (ldrs != null && !ldrs.isEmpty()) {
            LayerDomainResource ldr = null;
            Iterator ldri = ldrs.iterator();
            while (ldri.hasNext()) {
                ldr = (LayerDomainResource) ldri.next();
                if (LayerDomainResource.METADATA_DOMAIN.equalsIgnoreCase(ldr.getDomain())) {
                    mdUrl = ldr.getUrl();
                    break;
                }
            }
        }

        if (mdUrl == null || mdUrl.length() == 0) {
            return; // no metadata at source
        }

        String newMetadata = null;
        try {
            newMetadata = collectMetadata(mdUrl,credentials);
        } catch (Exception ex) {
//            log.error("", ex);
        }
        String currentMetadata = null;
        if (oldLayer != null) {
            currentMetadata = oldLayer.getMetadata();
        }

        layer.setMetadata(convertMetadata(newMetadata, currentMetadata));
    }

    private String convertMetadata(String newMetadata, String currentMetadata) {
        if (currentMetadata != null && currentMetadata.length() > 0) {
            //TODO update old with new info
            return currentMetadata;
        }
        // TODO replace online resource urls to point to KB urls's
        // TODO remove xsl
        return newMetadata;
    }

    /**
     * Collects the meta data
     * 
     * @param url               The server URL
     * @param username          The username, null for none
     * @param password          The password, null for none
     * @return                  The meta data
     * @throws HttpException    
     * @throws IOException
     * @throws Exception 
     */
    protected String collectMetadata(String url,B3PCredentials credentials) throws HttpException, IOException, Exception {

        HttpClient client = CredentialsParser.CommonsHttpClientCredentials(credentials);
        GetMethod method = new GetMethod(url);
        client.getHttpConnectionManager().getParams().setConnectionTimeout((int) maxResponseTime);
        String metadata = "";

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                log.error("Error connecting to server. Status code: " + statusCode);
                throw new Exception("Error connecting to server. Status code: " + statusCode);
            }

            metadata = method.getResponseBodyAsString();

        } catch (HttpException e) {
            log.error("Fatal protocol violation: " + e.getMessage());
            throw new HttpException("Fatal protocol violation: " + e.getMessage());
        } catch (IOException e) {
            log.error("Fatal transport error: " + e.getMessage());
            throw new IOException("Fatal transport error: " + e.getMessage());
        } catch (Exception e) {
            log.error("General error: " + e.getMessage());
            throw e;
        } finally {
            method.releaseConnection();
        }

        return metadata;
    }
    
    protected Integer getInt(DynaValidatorForm dynaForm,String field) {
        return FormUtils.StringToInteger(dynaForm.getString(field));
    }   
    
    public Exception getException(){
        return this.exception;
    }
    
    public ArrayList<String> getMessages(){
        return this.parseMessages;
    }

    /**
     * Checks if the abbr exists
     * 
     * @param abbr The abbr
     * @param em    The entity manager
     * @return  True if the abbr exists
     */
    abstract public boolean abbrExists(String abbr,EntityManager em);
}
