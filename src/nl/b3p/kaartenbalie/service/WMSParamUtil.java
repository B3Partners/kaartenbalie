/*
 * WMSParamUtil.java
 *
 * Created on 1 maart 2007, 14:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.service;

/**
 *
 * @author Roy
 */
public class WMSParamUtil {
    public static final String BBOX= "BBOX=";
    public static final String TRANSPARENT= "TRANSPARENT=";
    public static final String FORMAT= "FORMAT=";
    public static final String INFO_FORMAT= "INFO_FORMAT=";
    public static final String LAYERS= "LAYERS=";
    public static final String QUERY_LAYERS= "QUERY_LAYERS=";
    public static final String WIDTH= "WIDTH=";
    public static final String HEIGHT= "HEIGHT=";
    public static final String FEATURE_COUNT= "FEATURE_COUNT=";
    public static final String X= "X=";
    public static final String Y= "Y=";
    public static final String VERSION="VERSION=";
    public static final String REQUEST="REQUEST=";
    public static final String STYLES="STYLES=";
    public static final String SRS = "SRS=";
    public static final String BGCOLOR= "BGCOLOR=";
    public static final String EXCEPTIONS= "EXCEPTIONS=";
    public static final String TIME= "TIME=";
    public static final String ELEVATION= "ELEVATION=";
    public static final String SLD= "SLD=";
    public static final String WFS= "WFS=";
    public static final String SERVICE= "SERVICE=";
    public static final String WRAPDATELINE= "WRAPDATELINE=";
    /** Creates a new instance of WMSParamUtil */
    public WMSParamUtil() {
    }
    public static String removeParameter(String param, String returnValue) {
        int index=returnValue.toLowerCase().lastIndexOf(param.toLowerCase());
        //als de string niet gevonden is dan niks doen
        if (index<=0){
            return returnValue;
        }
        int firstAmp= returnValue.indexOf("&",index);
        //Als er geen volgende & gevonden is dan staat de parameter aan het einde van de string,
        //dus het gedeelte incl. & ervoor weghalen
        if (firstAmp<0){
            returnValue=returnValue.substring(0,index-1);
        }
        //staat er tussen (niet als laatste)
        else{
            returnValue=returnValue.substring(0,index-1)+returnValue.substring(firstAmp,returnValue.length());
        }
        return returnValue;
    }

    public static String getParameter(String param, String s) {
        int index=s.toLowerCase().lastIndexOf(param.toLowerCase());
        //als de param niet gevonden is null returnen.
        if (index < 0){
            return null;
        }
        int firstAmp= s.indexOf("&",index);
        //laatste parameter
        if (firstAmp<0){
            return s.substring(index+param.length(),s.length());
        }else{
            return s.substring(index+param.length(),firstAmp);
        }
        
    }
    public static String removeAllWMSParameters(String request){
        request=removeParameter(BBOX,request);
        request=removeParameter(TRANSPARENT,request);
        request=removeParameter(FORMAT,request);
        request=removeParameter(INFO_FORMAT,request);
        request=removeParameter(LAYERS,request);
        request=removeParameter(QUERY_LAYERS,request);
        request=removeParameter(WIDTH,request);
        request=removeParameter(HEIGHT,request);
        request=removeParameter(FEATURE_COUNT,request);
        request=removeParameter(X,request);
        request=removeParameter(Y,request);
        request=removeParameter(VERSION,request);
        request=removeParameter(REQUEST,request);
        request=removeParameter(STYLES,request);
        request=removeParameter(SRS,request);
        request=removeParameter(BGCOLOR,request);
        request=removeParameter(EXCEPTIONS,request);
        request=removeParameter(TIME,request);
        request=removeParameter(ELEVATION,request);
        request=removeParameter(SLD,request);
        request=removeParameter(WFS,request);
        request=removeParameter(SERVICE,request);
        request=removeParameter(WRAPDATELINE,request);
        return request;
    }
}
