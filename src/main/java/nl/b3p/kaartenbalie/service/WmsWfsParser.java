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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.services.B3PCredentials;
import nl.b3p.commons.services.HttpClientConfigured;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.struts.ServerAction;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.LayerDomainResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author rachelle
 */
abstract public class WmsWfsParser extends ServerAction {
    protected static final Log log = LogFactory.getLog(WMSParser.class);
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
    public static final String UPLOADFILE_SIZE_ERRORKEY = "Het bestand is niet opgeslagen, omdat deze te groot is.";
    public static final String UPLOADFILE_FORMAT_ERRORKEY = "Het bestand is niet opgeslagen, omdat deze niet van het type .jpg .png of .sld is.";
    public static final String UPLOADFILE_EXISTS_ERRORKEY = "Het bestand is niet opgeslagen, omdat het reeds bestaat.";
    protected ArrayList<String> parseMessages;
    protected GroupParser groupParser;
    
    public WmsWfsParser(){
        parseMessages    = new ArrayList<String>();
        groupParser     = new GroupParser();
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
    
    /*
     * Method to upload a file for a serviceprovider. A folder is made (if it doesn't exists)
     * with the abbreviation of the serviceprovider as name.
     */
    protected String uploadFile(FormFile thisFile, Boolean overwrite, String abbreviation) throws Exception {
        String[] allowed_files = MyEMFDatabase.getAllowedUploadFiles();
        List<String> allowed = new ArrayList<String>();
        allowed.addAll(Arrays.asList(allowed_files));
        
        String sep = File.separator;
        
        int fileSize = (int) thisFile.getFileSize();
        if (fileSize > (1024 * 1024)) {
            return UPLOADFILE_SIZE_ERRORKEY;
        }
        String fileName = thisFile.getFileName();
        int point = fileName.indexOf(".");
        String extention = fileName.substring(point);
        if (fileName == null || !allowed.contains(extention.toLowerCase())) {
            return UPLOADFILE_FORMAT_ERRORKEY;
        }
        String uploaddir = MyEMFDatabase.getUpload()+sep+abbreviation;
        File dir = new File(uploaddir);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        
        File targetFile = new File(uploaddir, fileName);
        boolean exists = targetFile.exists();
        boolean doOverwrite = false;
        if (overwrite != null && overwrite.booleanValue()) {
            doOverwrite = true;
        }
        if (exists && !doOverwrite) {
            return UPLOADFILE_EXISTS_ERRORKEY;
        }
        
        InputStream stream = null;
        try {
            //retrieve the file data
            stream = thisFile.getInputStream();
            OutputStream bos = null;
            try {
                bos = new FileOutputStream(targetFile);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        
        return OK;
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
     * @param credentials
     * @return                  The meta data
     * @throws IOException
     * @throws Exception 
     */
    protected String collectMetadata(String url, B3PCredentials credentials) throws IOException, Exception {

        HttpClientConfigured hcc = new HttpClientConfigured(credentials);
        HttpGet request = new HttpGet(url);

        HttpResponse response = null;
        try {
            response = hcc.execute(request);
            HttpEntity entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                log.error("Error connecting to server. Status code: " + statusCode);
                throw new Exception("Error connecting to server. Status code: " + statusCode);
            }

            String metadata = EntityUtils.toString(entity);
            return metadata;
            
        } finally {
            hcc.close(response);
            hcc.close();
        }

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
    
    /**
     * Sets the service with the given url as allowed
     * 
     * @param url   The url
     * @param em    The entityManager
     */
    abstract public void addAllowedService(String url,EntityManager em)  throws Exception ;
    
    /**
     * 
     * Removes the service with the given url as allowed
     * 
     * @param url   The url
     * @param em    The entityManager
     */
    abstract public void deleteAllowedService(String url,EntityManager em) throws Exception ;
    
    /**
     * Clears the allowed services list
     * 
     * @param em    The entityManager
     */
    abstract public void deleteAllAllowedServices(EntityManager em);
}
