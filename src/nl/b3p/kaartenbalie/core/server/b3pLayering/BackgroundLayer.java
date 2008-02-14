/*
 * BackgroundLayer.java
 *
 * Created on February 12, 2008, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import nl.b3p.ogc.utils.OGCRequest;

/**
 *
 * @author Chris Kramer
 */
public abstract class BackgroundLayer extends ConfigLayer{
    
    /** Creates a new instance of BackgroundLayer */
    public BackgroundLayer(String name, String title) {
        super(name, title);
    }
    
    /* Override */
    public BufferedImage imageHandler(OGCRequest ogcrequest, Map parameterMap, boolean transparant) throws Exception {
        BufferedImage bufImage = createBaseImage(ogcrequest, parameterMap);
        Graphics2D g2d = (Graphics2D) bufImage.getGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(20,20, bufImage.getWidth() - 40, bufImage.getHeight() - 40);
        addTagsToImage(bufImage, parameterMap);
        return modifyBaseImage(bufImage, parameterMap);
    }
}
