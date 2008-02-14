/*
 * BalanceLayer.java
 *
 * Created on February 11, 2008, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 *
 * @author Chris Kramer
 */
public class KBTitleLayer extends ConfigLayer{
    
    public static final String NAME = "kaartenbalie";
    public static final String TITLE = "kb_titlebar_layer";
    public KBTitleLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        
        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        int padding = 20;
        int w = bufImage.getWidth();
        int h = 19;
        int x = 0;
        int y = bufImage.getHeight() - h - padding;
        drawMessageBox(g2, "Kaartenbalie",x,y,w,h);
        return bufImage;
    }
    
}
