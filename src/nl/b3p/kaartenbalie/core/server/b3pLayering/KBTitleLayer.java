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
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author Chris Kramer
 */
public class KBTitleLayer extends ConfigLayer{
    
    public static final String NAME = "kaartenbalie";
    public static final String TITLE = "Creditinfo";
    public KBTitleLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        
        StringBuffer message = new StringBuffer();
        
        BigDecimal cm = (BigDecimal) parameterMap.get(AllowTransactionsLayer.creditMutation);
        if (cm != null) {
            message.append(" (");
            message.append(MESSAGE_TITLE_CREDITS);
            message.append(cm.toString());
            message.append(MESSAGE_TITLE_UNIT);
        }
        
        Double balance = (Double) parameterMap.get(BalanceLayer.creditBalance);
        if (balance != null) {
            if (message.length()==0)
                message.append(" (");
            else
                message.append(", ");
            message.append(MESSAGE_TITLE_BALANCE);
            message.append(balance.toString());
            message.append(MESSAGE_TITLE_UNIT);
        }
        if (message.length()>0)
            message.append(")");
        
        message.insert(0,MESSAGE_TITLE_LAYER);
        
        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        int padding = 20;
        int w = bufImage.getWidth();
        int h = 19;
        int x = 0;
        int y = bufImage.getHeight() - h - padding;
        drawMessageBox(g2, message.toString(),x,y,w,h);
        return bufImage;
    }
    
}
