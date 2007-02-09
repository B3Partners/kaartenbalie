/*
 * @(#)FlamingoMap.java
 * @author R. Braam
 * @version 1.00, 6 februari 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.core.server;

/**
 * FlamingoMap definition:
 *
 */

public class FlamingoMap {
    
    /** Creates a new instance of FlamingoMap */
    public FlamingoMap() {
    }
    
    private static final String BBOX= "BBOX=";
    private static final String TRANSPARENT= "TRANSPARENT=";
    private static final String FORMAT= "FORMAT=";
    private static final String INFO_FORMAT= "INFO_FORMAT=";
    private static final String LAYERS= "LAYERS=";
    private static final String QUERY_LAYERS= "QUERY_LAYERS=";
    private static final String WIDTH= "WIDTH=";
    private static final String HEIGHT= "HEIGHT=";
    private static final String FEATURE_COUNT= "FEATURE_COUNT=";
    private static final String X= "X=";
    private static final String Y= "Y=";
    
    
    /*id van de kaart*/
    private Integer id;
    /*naam*/
    private String naam;
    /*inlog vereist?*/
    private Boolean inlog;
    /*mag worden gepubliceerd?*/
    private Boolean publicatie;
    /*Url naar metadata*/
    private String metadataLink;
     /*Url naar WMS*/
    private String WMSLink;
     /*Url naar WFS*/
    private String WFSLink;

    

    
    
    /*'Special getters'*/
    public String getFMCWMSLink(){
        String returnValue= new String(WMSLink);
        //http://b3p-roy/cgi-bin/mapserv.exe?map=C:/mapserver/mapIjmond/ijmond.map&REQUEST=map&WMTVER=1.0&WIDTH=469&HEIGHT=354&LAYERS=ijmond-map&TRANSPARENT=TRUE&FORMAT=PNG&BBOX=95782.84,488579.38,120509.92,507243.3&SRS=EPSG:28992
        returnValue=removeParameter(BBOX,returnValue);
        returnValue=removeParameter(TRANSPARENT,returnValue);
        returnValue=removeParameter(FORMAT,returnValue);
        returnValue=removeParameter(INFO_FORMAT,returnValue);
        returnValue=removeParameter(LAYERS,returnValue);
        returnValue=removeParameter(QUERY_LAYERS,returnValue);
        returnValue=removeParameter(WIDTH,returnValue);
        returnValue=removeParameter(HEIGHT,returnValue);
        returnValue=removeParameter(FEATURE_COUNT,returnValue);
        returnValue=removeParameter(X,returnValue);
        returnValue=removeParameter(Y,returnValue);
        return returnValue;
    }
    public String getLayers(){
        String returnValue= getParameter(LAYERS,WMSLink);
        return returnValue;        
    }
    
    
    /*Getters and setters*/
    //<editor-fold defaultstate="collapsed">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public Boolean getInlog() {
        return inlog;
    }

    public void setInlog(Boolean inlog) {
        this.inlog = inlog;
    }

    public Boolean getPublicatie() {
        return publicatie;
    }

    public void setPublicatie(Boolean publicatie) {
        this.publicatie = publicatie;
    }

    public String getMetadataLink() {
        return metadataLink;
    }

    public void setMetadataLink(String metadataLink) {
        this.metadataLink = metadataLink;
    }

    public String getWMSLink() {
        return WMSLink;
    }

    public void setWMSLink(String WMSLink) {
        this.WMSLink = WMSLink;
    }

    public String getWFSLink() {
        return WFSLink;
    }

    public void setWFSLink(String WFSLink) {
        this.WFSLink = WFSLink;
    }

    
    
    //</editor-fold>

    private String removeParameter(String param, String returnValue) {
        int index=returnValue.toLowerCase().lastIndexOf(param.toLowerCase());
        //als de string niet gevonden is dan niks doen
        if (index<0){
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

    private String getParameter(String param, String s) {
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

    

    
    
}
