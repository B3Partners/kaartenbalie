/*
 * AllowTransactionsLayer.java
 *
 * Created on January 25, 2008, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Map;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.KBConstants;


public class AllowTransactionsLayer extends ConfigLayer implements KBConstants{
    
    public static final String NAME = "allowwithdrawals";
    public static final String TITLE = "AfboekingOK";
    public static final String configValue = "allowTransactions";
    public static final String creditMutation = "creditMutation";
    public static final String pricedLayers = "pricedLayers";
    
    public AllowTransactionsLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
        configMap.put(configValue, new Boolean(true));
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        Boolean asRequest = (Boolean) parameterMap.get("asrequest");
        if (!asRequest.booleanValue()) {
            int alignCenter = (bufImage.getWidth() /2);
            int alignMiddle = (bufImage.getHeight() /2);
            Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
            int boxWidth = 400;
            int boxHeight = 150;
            
            StringBuffer message = new StringBuffer();
            message.append(MESSAGE_AUTHORIZATION_INTRO);
            message.append(MESSAGE_AUTHORIZATION_START);
            message.append(TITLE);
            message.append(" (");
            message.append(NAME);
            message.append(")");
            message.append(MESSAGE_AUTHORIZATION_END);
            message.append(MESSAGE_NO_DISPLAY_AGAIN);
            message.append(MESSAGE_REQUIRED_CREDITS);
            
            BigDecimal cm = (BigDecimal) parameterMap.get(creditMutation);
            String cmt = "n/a";
            if (cm != null) {
                cmt = cm.toString();
            }
            message.append(cmt);
            
            String pln = (String) parameterMap.get(pricedLayers);
            if (pln!=null && pln.length()>0) {
                message.append(" (");
                message.append(pln);
                message.append(")");
            }
            
            drawTitledMessageBox(g2,
                    TITLE, message.toString(),
                    alignCenter - (boxWidth/2),
                    alignMiddle - (boxHeight /2),
                    boxWidth,boxHeight);
        }
        return bufImage;
    }
    
    
}
