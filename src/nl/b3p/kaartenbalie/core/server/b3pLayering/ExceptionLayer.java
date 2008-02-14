/*
 * ExceptionLayer.java
 *
 * Created on February 11, 2008, 2:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;

/**
 *
 * @author Chris Kramer
 */
public class ExceptionLayer extends BackgroundLayer{
    
    public static final String NAME = "exceptionlayer";
    public static final String TITLE = "kb_exception_layer";
    
    public ExceptionLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
        
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        
        String message = (String) parameterMap.get("message");
        Class type = (Class) parameterMap.get("type");
        StackTraceElement[] stacktrace = (StackTraceElement[]) parameterMap.get("stacktrace");
        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        
        int paddingsides = 30;
        int w = bufImage.getWidth() - (paddingsides*2);
        if (type != null) {
            drawTitledMessageBox(g2,
                    TITLE,  type.getCanonicalName(),
                    paddingsides,80,w,30);
        }
        int paddingbottom = 50;
        int y = 130;
        int h = bufImage.getHeight() - y - paddingbottom;
        drawMessageBox(g2, message, paddingsides,y,w,h);
        return bufImage;
    }

    
}
