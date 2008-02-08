/*
 * CustomLayer.java
 *
 * Created on January 25, 2008, 10:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.KBConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;

/**
 *
 * @author Chris Kramer
 */
public abstract class ConfigLayer extends Layer implements KBConstants {
    
    private static Map configLayers;
    
    /*
     * Default constructor is protected... It's abstract anyways..
     */
    protected ConfigLayer() {
    }
    
    /*
     * This should be the default constructor...
     */
    protected ConfigLayer(String name, String title) {
        this.setName(name);
        this.setTitle(title);
        SrsBoundingBox srsbb1 = new SrsBoundingBox();
        srsbb1.setLayer(this);
        srsbb1.setMinx("0");
        srsbb1.setMiny("0");
        srsbb1.setMaxx("1");
        srsbb1.setMaxy("1");
        addSrsbb(srsbb1);
        SrsBoundingBox srsbb2 = new SrsBoundingBox();
        srsbb2.setLayer(this);
        srsbb2.setSrs("EPSG:28992");
        addSrsbb(srsbb2);
        setMetaData("http://www.b3partners.nl"); //??? 7.1.4.5.14
        setQueryable("0");
        setCascaded("0");
        setOpaque("0");
    }
    
    /*
     * This initializes the configLayers hashMap...
     */
    static {
        configLayers = new HashMap();
        configLayers.put(AllowTransactionsLayer.NAME, AllowTransactionsLayer.class);
        configLayers.put(Allow100CTALayer.NAME, Allow100CTALayer.class);
    }
    
    /*
     * use this to get the right instance of a class..
     */
    public static ConfigLayer forName(String configLayerName) throws Exception{
        
        if (configLayerName == null || configLayerName.trim().length() == 0) {
            throw new Exception("ConfigLayerName is a required field!");
        }
        configLayerName = configLayerName.trim().toUpperCase();
        Class configLayerClass = (Class) configLayers.get(configLayerName);
        if (configLayerClass == null) {
            throw new Exception("Trying to fetch unregistered or non-existing configLayer ' " + configLayerName + "'.");
        }
        return  (ConfigLayer) configLayerClass.newInstance();
    }
    
    /*
     *
     */
    
    public static Map getConfigLayers() {
        return configLayers;
    }
    
    /*
     * Function for setting values in a configMap...
     */
    public abstract void processConfig(Map configMap) throws Exception;
    
    /*
     * In the case of WMS, an layer must be handled as an Image.. this is how..
     */
    public BufferedImage drawImage(DataWrapper data, Map parameterMap) throws Exception {
        OGCRequest ogcrequest = data.getOgcrequest();
        // Image width & heigth...
        int width  = Integer.parseInt(ogcrequest.getParameter(WMS_PARAM_WIDTH));
        int height = Integer.parseInt(ogcrequest.getParameter(WMS_PARAM_HEIGHT));
        // Create Image
        BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.drawRect(1,10,50,50);
        return modifyBaseImage(data, bufImage, parameterMap);
    }
    
    /*
     * Will be called after the drawImage function and allows extending classes to modify the base..
     */
    protected abstract BufferedImage modifyBaseImage(DataWrapper data, BufferedImage bufImage, Map parameterMap) throws Exception;
    
    
    /*
     * This will send the image to the client.. Only use this when not stacking up images..
     */
    public void sendImage(DataWrapper data, BufferedImage bufImage) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OGCRequest ogcrequest = data.getOgcrequest();
        String requestedImageType = ogcrequest.getParameter(WMS_PARAM_FORMAT);
        String imageType = null;
        if(requestedImageType.equalsIgnoreCase("image/jpeg")) {
            imageType = "JPEG";
        } else if(requestedImageType.equalsIgnoreCase("image/png")) {
            imageType = "PNG";
        } else if(requestedImageType.equalsIgnoreCase("image/gif")) {
            imageType = "GIF";
        } else {
            throw new Exception("Unsupported ImageType");
        }
        ImageIO.write(bufImage, imageType, baos);
        data.write(baos);
    }
    
    
    
}
