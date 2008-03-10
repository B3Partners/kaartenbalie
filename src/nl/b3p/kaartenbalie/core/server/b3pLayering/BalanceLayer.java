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
public class BalanceLayer extends ConfigLayer{
    
    public static final String NAME = "creditbalance";
    public static final String TITLE = "Credittegoed";
    public static final String creditBalance = "balance";
    
    public BalanceLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        
        Double balance = (Double) parameterMap.get(creditBalance);
        String message = "n/a";
        if (balance != null) {
            message = balance.toString();
        }
        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        int padding = 20;
        int w = 120;
        int h = 18;
        int x = bufImage.getWidth() - w - padding;
        int y = padding;
        drawTitledMessageBox(g2, "Balance",message + " c",x,y,w,h);
        
        
        
        return bufImage;
    }
    
}
