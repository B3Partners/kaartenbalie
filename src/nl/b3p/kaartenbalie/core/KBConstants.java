/*
 * @(#)KBConstants.java
 * @author Chris
 * @version 1.00
 * Created on 26 december 2006, 12:16
 *
 */

package nl.b3p.kaartenbalie.core;

import java.util.Arrays;
import java.util.List;

public interface KBConstants {
    // <editor-fold defaultstate="collapsed" desc="Predefined static final strings ">
    public static final String KB_USER = "user" ;
    public static final String KB_PERSONAL_URL = "personalURL" ;
    
    public static final String WMS_SERVICE = "SERVICE" ;
    public static final String WMS_SERVICE_WMS = "WMS" ;
    
    public static final String WMS_REQUEST = "REQUEST" ;
    public static final String WMS_REQUEST_GetCapabilities = "GetCapabilities" ;
    public static final String WMS_REQUEST_GetMap = "GetMap" ;
    public static final String WMS_REQUEST_GetFeatureInfo = "GetFeatureInfo" ;
    public static final String WMS_REQUEST_GetLegendGraphic = "GetLegendGraphic" ;
    public static final String WMS_REQUEST_GetStyles = "GetStyles" ;
    public static final String WMS_REQUEST_PutStyles = "PutStyles" ;
    public static final String WMS_REQUEST_DescribeLayer = "DescribeLayer" ;
    
    public static final String WMS_VERSION = "VERSION" ;
    public static final String WMS_VERSION_111 = "1.1.1" ;
    public static final String WMS_VERSION_110 = "1.1.0" ;
    
    public static final String WMS_PARAM_LAYERS = "LAYERS" ;
    public static final String WMS_PARAM_STYLES = "STYLES" ;
    public static final String WMS_PARAM_SRS = "SRS" ;
    public static final String WMS_PARAM_BBOX = "BBOX" ;
    public static final String WMS_PARAM_WIDTH = "WIDTH" ;
    public static final String WMS_PARAM_HEIGHT = "HEIGHT" ;
    public static final String WMS_PARAM_FORMAT = "FORMAT" ;
    public static final String WMS_PARAM_QUERY_LAYERS = "QUERY_LAYERS" ;
    public static final String WMS_PARAM_X = "X" ;
    public static final String WMS_PARAM_Y = "Y" ;
    public static final String WMS_PARAM_STYLE = "STYLE" ;
    public static final String WMS_PARAM_LAYER = "LAYER" ;
    public static final String WMS_PARAM_FEATURETYPE = "FEATURETYPE" ;
    public static final String WMS_PARAM_RULE = "RULE" ;
    public static final String WMS_PARAM_SCALE = "SCALE" ;
    public static final String WMS_PARAM_SLD = "SLD" ;
    public static final String WMS_PARAM_SLD_BODY = "SLD_BODY" ;
    public static final String WMS_PARAM_INFO_FORMAT = "INFO_FORMAT";
    public static final String WMS_PARAM_FEATURECOUNT = "FEATURECOUNT";
    public static final String WMS_PARAM_EXCEPTION_FORMAT = "EXCEPTIONS";
    
    public static final String WMS_PARAM_TRANSPARENT = "TRANSPARENT";
    public static final String WMS_PARAM_TRANSPARENT_TRUE = "TRUE";
    
    public static final String WMS_PARAM_EXCEPTION_TEXT = "text/plain";
    public static final String WMS_PARAM_EXCEPTION_HTML = "text/html";
    public static final String WMS_PARAM_EXCEPTION_XML = "text/xml";
    public static final String WMS_PARAM_EXCEPTION_JPEG = "image/jpeg";
    public static final String WMS_PARAM_EXCEPTION_PNG = "image/png";
    
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
    
    public static final boolean WMS_GETFEATUREINFO_RETURN_EXCEPTION = true;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List with essential parameters per wms service.">
    /**
     * List with essential parameters per wms service.
     */
    public static final List PARAMS_GetCapabilities = Arrays.asList(new String[] {
        WMS_SERVICE,
        WMS_VERSION
    });
    
    public static final List PARAMS_GetMap = Arrays.asList(new String[] {
        WMS_REQUEST,
        WMS_VERSION,
        WMS_PARAM_LAYERS,
        WMS_PARAM_STYLES,
        WMS_PARAM_SRS,
        WMS_PARAM_BBOX,
        WMS_PARAM_WIDTH,
        WMS_PARAM_HEIGHT,
        WMS_PARAM_FORMAT
    });
    
    public static final List PARAMS_GetFeatureInfo = Arrays.asList(new String[] {
        WMS_VERSION,
        WMS_PARAM_QUERY_LAYERS,
        WMS_PARAM_X,
        WMS_PARAM_Y,
        WMS_REQUEST,
        WMS_VERSION,
        WMS_PARAM_LAYERS,
        WMS_PARAM_SRS,
        WMS_PARAM_BBOX,
        WMS_PARAM_WIDTH,
        WMS_PARAM_HEIGHT,
        WMS_PARAM_FORMAT
    });
    
    public static final List PARAMS_GetLegendGraphic = Arrays.asList(new String[] {
        WMS_SERVICE,
        WMS_VERSION,
        WMS_PARAM_STYLE
    });
    
    public static final List PARAMS_GetStyles = Arrays.asList(new String[] {
        WMS_SERVICE,
        WMS_VERSION
    });
    
    public static final List PARAMS_PutStyles = Arrays.asList(new String[] {
        WMS_SERVICE,
        WMS_VERSION
    });
    
    public static final List PARAMS_DescribeLayer = Arrays.asList(new String[] {
        WMS_SERVICE,
        WMS_VERSION
    });
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List with implemented requests.">
    /**
     * List with implemented requests.
     */
    public static final List SUPPORTED_REQUESTS = Arrays.asList(new String[] {
        WMS_REQUEST_GetCapabilities,
        WMS_REQUEST_GetMap,
        WMS_REQUEST_GetFeatureInfo,
        WMS_REQUEST_GetLegendGraphic
                // ,WMS_REQUEST_GetStyles
                // ,WMS_REQUEST_PutStyles
                // ,WMS_REQUEST_DescribeLayer
    });
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List with implemented services.">
    /**
     * List with implemented services.
     */
    public static final List SUPPORTED_SERVICES = Arrays.asList(new String[] {
        WMS_SERVICE_WMS
    });
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List with implemented versions.">
    /**
     * List with implemented versions.
     */
    public static final List SUPPORTED_VERSIONS = Arrays.asList(new String[] {
        WMS_VERSION_111, WMS_VERSION_110
    });
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="List with implemented exceptions.">
    /**
     * List with implemented exceptions.
     */
    public static final List SUPPORTED_EXCEPTIONS = Arrays.asList(new String[] {
        WMS_PARAM_EXCEPTION_TEXT, 
        WMS_PARAM_EXCEPTION_HTML, 
        WMS_PARAM_EXCEPTION_XML, 
        WMS_PARAM_EXCEPTION_JPEG, 
        WMS_PARAM_EXCEPTION_PNG
    });
    // </editor-fold>
}