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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.commons.struts.ExtendedMethodProperties;
import nl.b3p.gis.B3PCredentials;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.DirectoryParser;
import nl.b3p.kaartenbalie.service.WMSParser;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.validator.DynaValidatorForm;

public class WmsServerAction extends ServerAction {

    private static final Log log = LogFactory.getLog(WmsServerAction.class);
    protected static final String ABBR_WARN_KEY = "warning.abbr.changed";
    protected static final String MAPPING_TEST = "test";
    protected static final String MAPPING_BATCH_UPDATE = "batchUpdate";
    protected static long maxResponseTime = 10000;
    private String WMS_SERVICE_TEST_FOUT = "beheer.wms.test.fout";
    private String WMS_SERVICE_BATCH_UPDATE_FOUT = "beheer.wms.batchupdate.fout";
    protected WMSParser parser;

    public WmsServerAction() {
        parser = new WMSParser();
    }

    @Override
    protected Map getActionMethodPropertiesMap() {
        Map map = super.getActionMethodPropertiesMap();
        ExtendedMethodProperties crudProp = null;

        crudProp = new ExtendedMethodProperties(MAPPING_TEST);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.wms.test.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.wms.test.failed");
        map.put(MAPPING_TEST, crudProp);

        crudProp = new ExtendedMethodProperties(MAPPING_BATCH_UPDATE);
        crudProp.setDefaultForwardName(SUCCESS);
        crudProp.setDefaultMessageKey("beheer.wms.batchupdate.succes");
        crudProp.setAlternateForwardName(FAILURE);
        crudProp.setAlternateMessageKey("beheer.wms.batchupdate.failed");
        map.put(MAPPING_BATCH_UPDATE, crudProp);

        return map;
    }

    /*
     * Edit method which handles all editable requests.
     *
     * @param mapping The ActionMapping used to select this instance. @param
     * form The DynaValidatorForm bean for this request. @param request The HTTP
     * Request we are processing. @param response The HTTP Response we are
     * processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    @Override
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServiceProvider serviceprovider = parser.getServiceProvider(dynaForm, false);
        if (serviceprovider == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, SP_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateServiceProviderForm(serviceprovider, dynaForm, request);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>
    /*
     * Method for saving a new service provider from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance. @param
     * form The DynaValidatorForm bean for this request. @param request The HTTP
     * Request we are processing. @param response The HTTP Response we are
     * processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">

    @Override
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        /*
         * Change DataWarehousing mode to performance as this is a very
         * complicated process which will otherwise consume a lot of time.
         */
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            super.addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        String url = FormUtils.nullIfEmpty(dynaForm.getString("url"));
        if (url == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }

        /*
         * First we need to check if the given url is realy an url.
         */
        try {
            URL tempurl = new URL(url.trim());
        } catch (MalformedURLException mue) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_URL_ERRORKEY);
            return getAlternateForward(mapping, request);
        }

        /*
         * Check username and password setting
         */
        String username = dynaForm.getString("username");
        String password = dynaForm.getString("password");
        
        if ((!username.equals("") && password.equals("")) || (username.equals("") && !password.equals(""))) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_CREDENTIALS_ERRORKEY);
            return getAlternateForward(mapping, request);
        }

        String abbreviation = FormUtils.nullIfEmpty(dynaForm.getString("abbr"));
        if (!isAlphaNumeric(abbreviation)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_ALPHANUMERIC_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        if (abbreviation.equalsIgnoreCase(KBConfiguration.SERVICEPROVIDER_BASE_ABBR)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ABBR_RESERVED_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        /*
         * Save provider data
         */
        String code = parser.saveProvider(request, dynaForm);

        if (code.equals(WMSParser.ERROR_INVALID_URL)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, parser.getException().getMessage());
            return getAlternateForward(mapping, request);
        } else if (code.equals(NON_UNIQUE_ABBREVIATION_ERROR_KEY)) {
            /*
             * ID not unique
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NON_UNIQUE_ABBREVIATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        } else if (code.equals(SERVER_CONNECTION_ERRORKEY)) {
            /*
             * Error saving server
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, WMSParser.SERVER_CONNECTION_ERROR);
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.MALFORMED_CAPABILITY_ERROR)) {
            /*
             * data mallformed
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, MALFORMED_CAPABILITY_ERRORKEY);
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.SAVE_ERRORKEY)) {
            /*
             * General save error
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, parser.getException().getMessage());
            return getAlternateForward(mapping, request);
        } else if (code.equals(UNSUPPORTED_WMSVERSION_ERRORKEY)) {
            /*
             * WMS version unsupported
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, UNSUPPORTED_WMSVERSION_ERRORKEY);
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.ERROR_DELETE_OLD_PROVIDER)) {
            /*
             * Error deleting old provider
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, parser.getException().getMessage());
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.UPLOADFILE_SIZE_ERRORKEY)) {
            /*
             * Error file to big
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, WMSParser.UPLOADFILE_SIZE_ERRORKEY);
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.UPLOADFILE_FORMAT_ERRORKEY)) {
            /*
             * Error wrong file format
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, WMSParser.UPLOADFILE_FORMAT_ERRORKEY);
            return getAlternateForward(mapping, request);
        } else if (code.equals(WMSParser.UPLOADFILE_EXISTS_ERRORKEY)) {
            /*
             * Error file already exists
             */
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, WMSParser.UPLOADFILE_EXISTS_ERRORKEY);
            return getAlternateForward(mapping, request);
        } else {
            /*
             * All oke
             */
            dynaForm.set("id", null);

            prepareMethod(dynaForm, request, LIST, EDIT);
            addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

            return getDefaultForward(mapping, request);
        }
    }
    // </editor-fold>

    public static ServiceProvider saveServiceProvider(String url, B3PCredentials credentials, String givenName, String abbreviation, EntityManager em) throws Exception {
        return WMSParser.saveServiceProvider(url, credentials, givenName, abbreviation, em);
    }

    public static void addRightsForAllLayers(String[] orgSelected, ServiceProvider sp, EntityManager em) throws Exception {
        WMSParser.addRightsForAllLayers(orgSelected, sp, em);
    }

    public static void addAllLayersToGroup(Organization org, ServiceProvider sp, EntityManager em) throws Exception {
        WMSParser.addAllLayersToGroup(org, sp, em);
    }

    public ActionForward test(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int fout = parser.test(dynaForm);

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);

        if (fout > 0) {
            ActionMessages messages = getMessages(request);
            ActionMessage message = new ActionMessage(WMS_SERVICE_TEST_FOUT, fout);

            messages.add(ActionMessages.GLOBAL_MESSAGE, message);
            saveMessages(request, messages);
        }

        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    public ActionForward batchUpdate(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int fout = parser.batchUpdate(dynaForm);

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);

        if (fout > 0) {
            ActionMessages messages2 = getMessages(request);
            ActionMessage message = new ActionMessage(WMS_SERVICE_BATCH_UPDATE_FOUT, fout);

            messages2.add(ActionMessages.GLOBAL_MESSAGE, message);
            saveMessages(request, messages2);
        }

        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    @Override
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = parser.deleteConfirm(dynaForm, request);

        if (code.equals(NOTFOUND_ERROR_KEY)) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        prepareMethod(dynaForm, request, DELETE, EDIT);

        ArrayList<String> parseMessages = parser.getMessages();
        for (String message : parseMessages) {
            addAlternateMessage(mapping, request, null, message);
        }

        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /*
     * Method for deleting a serviceprovider selected by a user.
     *
     * @param mapping The ActionMapping used to select this instance. @param
     * form The DynaValidatorForm bean for this request. @param request The HTTP
     * Request we are processing. @param response The HTTP Response we are
     * processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    @Override
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code     = parser.delete(dynaForm, request);
        
        if( code.equals(TOKEN_ERROR_KEY) ){
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        if ( code.equals(NOTFOUND_ERROR_KEY) ) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /*
     * Creates a list of all the service providers in the database.
     *
     * @param form The DynaValidatorForm bean for this request. @param request
     * The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    @Override
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {
        super.createLists(form, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        // Only shows WMS servers for now
        List serviceproviderlist = em.createQuery("from ServiceProvider order by given_name").getResultList();
        request.setAttribute("serviceproviderlist", serviceproviderlist);

        List organizationlist = em.createQuery("from Organization order by name").getResultList();
        request.setAttribute("organizationlist", organizationlist);

        //createTreeview(form, request);
    }
// </editor-fold>
/*
     * public void createTreeview(DynaValidatorForm form, HttpServletRequest
     * request) throws Exception { //File dir = new File("/var/mapfiles/");
     * JSONObject root = new JSONObject(); DirectoryParser directoryParser = new
     * DirectoryParser(); File dir = new File(MyEMFDatabase.getMapfiles());
     * String[] allowed_files = {".map"};
     *
     * if (dir.isDirectory()) { root.put("id", "root"); root.put("children",
     * directoryParser.stepIntoDirectory(dir, allowed_files)); root.put("title",
     * "bestanden"); }
     *
     * request.setAttribute("mapfiles", root.toString(4)); }
     */
    //-------------------------------------------------------------------------------------------------------
    // PROTECTED METHODS -- Will be used in the demo by ServerActioDemo
    //-------------------------------------------------------------------------------------------------------
    /*
     * Method which returns the service provider with a specified id or a new
     * object if no id is given.
     *
     * @param form The DynaValidatorForm bean for this request. @param request
     * The HTTP Request we are processing. @param createNew A boolean which
     * indicates if a new object has to be created. @param id An Integer
     * indicating which organization id has to be searched for.
     *
     * @return a service provider object.
     */
    // <editor-fold defaultstate="" desc="getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">

    protected ServiceProvider getServiceProvider(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        ServiceProvider serviceProvider = null;
        Integer id = getID(dynaForm);
        if (null == id && createNew) {
            serviceProvider = new ServiceProvider();
        } else if (null != id) {
            serviceProvider = (ServiceProvider) em.find(ServiceProvider.class, new Integer(id.intValue()));
        }
        return serviceProvider;
    }
    // </editor-fold>

    // </editor-fold>
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    /*
     * Method which will fill the JSP form with the data of a given service
     * provider.
     *
     * @param serviceProvider ServiceProvider object from which the information
     * has to be printed. @param form The DynaValidatorForm bean for this
     * request. @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateOrganizationForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) method.">
    private void populateServiceProviderForm(ServiceProvider serviceProvider, DynaValidatorForm dynaForm, HttpServletRequest request) {
        dynaForm.set("id", serviceProvider.getId().toString());
        dynaForm.set("givenName", serviceProvider.getGivenName());
        dynaForm.set("url", serviceProvider.getUrl());
        dynaForm.set("username", serviceProvider.getUserName());
        dynaForm.set("password", serviceProvider.getPassword());
        dynaForm.set("updatedDate", serviceProvider.getUpdatedDate().toString());
        dynaForm.set("abbr", serviceProvider.getAbbr());
        dynaForm.set("sldUrl", serviceProvider.getSldUrl());

        dynaForm.set("ignoreResource", serviceProvider.getIgnoreResource());
        
        /* TODO: Bij opslaan orgSelected bewaren zodat de vinkjes
         * bij bewerken alvast gezet kunnen worden */
        //String[] orgSelected = new String[] {"1","3"};
        //dynaForm.set("orgSelected", orgSelected);        
        
        // Haal geuploaden files van deze service op
        String uploaddir = MyEMFDatabase.getUpload()+"\\"+serviceProvider.getAbbr();
        List uploadFiles = new ArrayList();
        List<String> fileNames = new ArrayList<String>();
        File dir = new File(uploaddir);
        if (dir.isDirectory()) {
            DirectoryParser directoryParser = new DirectoryParser();
            String[] allowed_files = MyEMFDatabase.getAllowedUploadFiles();
            uploadFiles = directoryParser.parse2List(dir, allowed_files);
            
            if(uploadFiles != null && !uploadFiles.isEmpty()){
                for(Iterator it = uploadFiles.iterator(); it.hasNext();){
                    String file = it.next().toString();
                    int i = file.lastIndexOf("\\");
                    fileNames.add(file.substring(i+1));
                }
                request.setAttribute("fileNames", fileNames);
            }
        }
    }

    protected String checkWmsUrl(String url) throws Exception {
        OGCRequest ogcrequest = new OGCRequest(url);
        if (ogcrequest.containsParameter(OGCConstants.WMS_REQUEST)
                && !OGCConstants.WMS_REQUEST_GetCapabilities.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_REQUEST))) {
            log.error(KBConfiguration.UNSUPPORTED_REQUEST);
            throw new Exception(KBConfiguration.UNSUPPORTED_REQUEST);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_REQUEST, OGCConstants.WMS_REQUEST_GetCapabilities);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_SERVICE)
                && !OGCConstants.WMS_SERVICE_WMS.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_SERVICE))) {
            log.error(KBConfiguration.UNSUPPORTED_SERVICE);
            throw new Exception(KBConfiguration.UNSUPPORTED_SERVICE);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_SERVICE, OGCConstants.WMS_SERVICE_WMS);
        }
        if (ogcrequest.containsParameter(OGCConstants.WMS_VERSION)
                && !OGCConstants.WMS_VERSION_111.equalsIgnoreCase(ogcrequest.getParameter(OGCConstants.WMS_VERSION))) {
            log.error(KBConfiguration.UNSUPPORTED_VERSION);
            throw new Exception(KBConfiguration.UNSUPPORTED_VERSION);
        } else {
            ogcrequest.addOrReplaceParameter(OGCConstants.WMS_VERSION, OGCConstants.WMS_VERSION_111);
        }
        return ogcrequest.getUrl();
    }
}
