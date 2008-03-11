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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import nl.b3p.kaartenbalie.service.KBImageTool;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;

/**
 *
 * @author Chris Kramer
 */
public abstract class ConfigLayer extends Layer {
    
    private static Map configLayers;
    
    protected ConfigLayer() {
    }
    
    /*
     * This should be the default constructor...
     */
    protected ConfigLayer(String name, String title) {
        this.setName(name.toLowerCase());
        this.setTitle(title);
        SrsBoundingBox srsbb1 = new SrsBoundingBox();
        srsbb1.setLayer(this);
        srsbb1.setMinx("3.3");
        srsbb1.setMiny("50.5");
        srsbb1.setMaxx("7.2");
        srsbb1.setMaxy("53.5");
        addSrsbb(srsbb1);
        SrsBoundingBox srsbb2 = new SrsBoundingBox();
        srsbb2.setLayer(this);
        srsbb2.setSrs("EPSG:28992");
        srsbb2.setMinx("12000");
        srsbb2.setMiny("304000");
        srsbb2.setMaxx("280000");
        srsbb2.setMaxy("620000");
        addSrsbb(srsbb2);
        setMetaData(KBConfiguration.SERVICEPROVIDER_BASE_HTTP); //??? 7.1.4.5.14
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
//        configLayers.put(BalanceLayer.NAME, BalanceLayer.class);
        configLayers.put(KBTitleLayer.NAME, KBTitleLayer.class);
    }
    
    /*
     * use this to get the right instance of a class..
     */
    public static ConfigLayer forName(String configLayerName) throws Exception{
        
        if (configLayerName == null || configLayerName.trim().length() == 0) {
            throw new Exception("ConfigLayerName is a required field!");
        }
        configLayerName = configLayerName.trim();
        Class configLayerClass = (Class) configLayers.get(configLayerName);
        
        if (configLayerClass == null) {
            throw new Exception("Trying to fetch unregistered or non-existing configLayer ' " + configLayerName + "'.");
        }
        return  (ConfigLayer) configLayerClass.newInstance();
    }
    
    
    
    public static BufferedImage handleRequest(String url, Map parameterMap) throws Exception {
        String mime = "image/png";
        parameterMap = handleParameterMap(parameterMap);
        parameterMap.put("showname", new Boolean(false));
        parameterMap.put("asrequest", new Boolean(true));
        parameterMap.put("transparant", new Boolean(true));
        OGCRequest ogcrequest = new OGCRequest(url);
        String[] layers = ogcrequest.getParameter(OGCConstants.WMS_PARAM_LAYERS).split(",");
        BufferedImage[] bufImages = new BufferedImage[layers.length];
        for (int i = 0; i < layers.length; i++) {
            ConfigLayer configLayer = forName(layers[i]);
            bufImages[i] = configLayer.drawImage(ogcrequest, parameterMap);
        }
        String mimeType = KBImageTool.getMimeType("image/png");
        if (mimeType == null) {
            throw new Exception("unsupported mime type: " + mime);
        }
        return KBImageTool.combineImages(bufImages, mimeType);
    }
    
    public static Map getConfigLayers() {
        return configLayers;
    }
    
    /*
     * Start of the imageHandling process
     */
    public BufferedImage drawImage(OGCRequest ogcrequest, Map parameterMap) throws Exception {
        return imageHandler(ogcrequest, handleParameterMap(parameterMap));
    }
    
    protected static Map handleParameterMap(Map parameterMap) throws Exception {
        if (parameterMap == null) {
            parameterMap = new HashMap();
        }
        Boolean transparant = (Boolean) parameterMap.get("transparant");
        if (transparant == null) {
            transparant = new Boolean(false);
            parameterMap.put("transparant", transparant);
        }
        Boolean asrequest = (Boolean) parameterMap.get("asrequest");
        if (asrequest == null) {
            asrequest = new Boolean(false);
            parameterMap.put("asrequest", asrequest);
        }
        return parameterMap;
    }
    
    protected BufferedImage createBaseImage(OGCRequest ogcrequest, Map parameterMap) throws Exception {
        
        Boolean transparant = (Boolean) parameterMap.get("transparant");
        // Image width & heigth...
        int width  = Integer.parseInt(ogcrequest.getParameter(OGCConstants.WMS_PARAM_WIDTH));
        int height = Integer.parseInt(ogcrequest.getParameter(OGCConstants.WMS_PARAM_HEIGHT));
        BufferedImage bufImage = null;
        
        bufImage =  new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufImage.createGraphics();
        if (!transparant.booleanValue()) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,width,height);
        }
        return bufImage;
    }
    
    protected void addTagsToImage(BufferedImage bufImage, Map parameterMap){
        Graphics2D g2d = (Graphics2D) bufImage.getGraphics();
        Boolean showName = (Boolean) parameterMap.get("showname");
        if (showName == null || (showName != null && showName.booleanValue() == true)) {
            drawTitledMessageBox(g2d,"KaartenBalie", "Message for Layer '" + super.getName() + "'", 20,20,300,30);
        }
    }
    
    public BufferedImage imageHandler(OGCRequest ogcrequest, Map parameterMap) throws Exception {
        BufferedImage bufImage = createBaseImage(ogcrequest, parameterMap);
        addTagsToImage(bufImage, parameterMap);
        return modifyBaseImage(bufImage, parameterMap);
    }
    
    
    
    
    /*
     * This will send the image to the client.. Only use this when not stacking up images..
     */
    public void sendImage(DataWrapper data, BufferedImage bufImage) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OGCRequest ogcrequest = data.getOgcrequest();
        String requestedImageType = ogcrequest.getParameter(OGCConstants.WMS_PARAM_FORMAT);
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
    
    /*
     * Abstracts
     */
    
    /* Will be called after the drawImage function and allows extending classes to modify the base.. */
    protected abstract BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception;
    
    /* Function for setting values in a configMap... */
    public abstract void processConfig(Map configMap) throws Exception;
    
    /**/
    
    
    /*
     * Drawing....
     */
    private void conditionalWrite(Graphics2D g2d, String line, int x, int y, int maxY ) {
        line = line.trim();
        if (line.length() > 0 && y < maxY) {
            g2d.drawString(line,x,y);
        }
    }
    protected void drawEdgedBox(Graphics g2d,int x,int y,int w,int h) {
        g2d.setColor(KBConfiguration.OHD_borderBoxBackground);
        g2d.fillRect(x,y,w,h);
        g2d.setColor(KBConfiguration.OHD_borderBoxTopLeft);
        g2d.drawLine(x,y,x+w,y); //Top
        g2d.drawLine(x,y+h,x,y); //Left
        g2d.setColor(KBConfiguration.OHD_borderBoxBottomRight);
        g2d.drawLine(x+w,y+h,x,y+h); //Bottom
        g2d.drawLine(x+w,y,x+w,y+h); //Right
        
        
    }
    protected void drawTitledMessageBox(Graphics2D g2d, String title, String message, int x, int y, int w, int h) {
        /* Do some calculations and init variables. */
        g2d.setFont(KBConfiguration.OHD_messageBoxFont);
        FontMetrics fm = g2d.getFontMetrics();
        int labelHeight = KBConfiguration.OHD_messageBoxFont.getSize() + (KBConfiguration.OHD_padding * 2);
        int angling = labelHeight;
        Rectangle2D testRectangle = fm.getStringBounds(title, g2d);
        int labelWidth = (int) testRectangle.getWidth();
        
        if (w  < labelWidth+ (2*angling)) {
            w = labelWidth + (2*angling);
        }
        y+=labelHeight;
        /* Now draw the box...    */
        drawMessageBox(g2d, message, x,y,w,h);
        
        /* Draw the label background */
        g2d.setColor(KBConfiguration.OHD_labelBoxColor);
        GeneralPath label = new GeneralPath();
        label.moveTo(x,y);
        label.lineTo(x+angling,y-labelHeight);
        label.lineTo(x+angling+labelWidth, y-labelHeight);
        label.lineTo( x + (angling*2) + labelWidth, y);
        label.closePath();
        g2d.fill(label);
        
        /* Draw the label Lines..  */
        g2d.setColor(KBConfiguration.OHD_borderBoxTopLeft);
        g2d.drawLine(x,y, x+angling,y-labelHeight);
        g2d.drawLine(x+angling,y-labelHeight, x+angling+labelWidth, y-labelHeight);
        g2d.setColor(KBConfiguration.OHD_borderBoxBottomRight);
        g2d.drawLine(x+angling+labelWidth, y-labelHeight,  x + (angling*2) + labelWidth, y);
        g2d.setColor(KBConfiguration.OHD_borderBoxBackground);
        g2d.drawLine(x + (angling*2) + labelWidth, y, x,y);
        /*Then add the title... */
        g2d.setColor(KBConfiguration.OHD_labelFontBoxColor);
        g2d.drawString(title, x + angling, y - KBConfiguration.OHD_padding);
        
    }
    protected void drawMessageBox(Graphics2D g2d, String message, int x, int y, int w, int h) {
        drawEdgedBox(g2d, x,y,w,h);
        
        if (message == null) {
            message = "null";
        }
        
        /*
         * Padding...
         */
        x += KBConfiguration.OHD_padding;
        y += KBConfiguration.OHD_padding;
        w -= KBConfiguration.OHD_padding;
        h -= KBConfiguration.OHD_padding;
        
        g2d.setFont(KBConfiguration.OHD_messageBoxFont);
        g2d.setColor(KBConfiguration.OHD_fontBoxColor);
        FontMetrics fm = g2d.getFontMetrics();
        
        
        int fontHeight = KBConfiguration.OHD_messageBoxFont.getSize();
        int yOffset = y ;
        String[] lines = message.split("\n");
        for (int j = 0; j< lines.length; j++) {
            String[] words = lines[j].split(" ");
            String line = "";
            for (int i = 0; i< words.length; i++) {
                String testLine = new String(line + " " + words[i]);
                Rectangle2D testRectangle = fm.getStringBounds(testLine, g2d);
                if (testRectangle.getWidth() > w) {
                    conditionalWrite(g2d, line, x, yOffset + fontHeight, y+h);
                    line = words[i];
                    yOffset += fontHeight;
                } else {
                    line = testLine;
                }
            }
            conditionalWrite(g2d, line, x, yOffset + fontHeight,y+ h);
            yOffset += (fontHeight + KBConfiguration.OHD_lineSpacing);
        }
        
    }
    
    
    
}
