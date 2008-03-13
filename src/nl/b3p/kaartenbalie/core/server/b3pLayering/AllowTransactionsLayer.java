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
import nl.b3p.ogc.utils.KBConfiguration;


public class AllowTransactionsLayer extends ConfigLayer{
    
    public static final String NAME = "allowwithdrawals";
    public static final String TITLE = "Afboekingen toestaan";
    public static final String allowTransactions = "allowTransactions";
    public static final String transactionsNeeded = "transactionsNeeded";
    public static final String creditMutation = "creditMutation";
    public static final String pricedLayers = "pricedLayers";
    
    public AllowTransactionsLayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
        configMap.put(allowTransactions, new Boolean(true));
        configMap.put(transactionsNeeded, new Boolean(false));
    }
    
    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        Boolean bAllowTransactions = (Boolean) parameterMap.get(allowTransactions);
        Boolean bTransactionsNeeded = (Boolean) parameterMap.get(transactionsNeeded);
        if ((bAllowTransactions==null || bAllowTransactions.booleanValue()==false) &&
                bTransactionsNeeded!=null && bTransactionsNeeded.booleanValue()==true) {
            // transacties zijn niet toegestaan maar wel nodig dan toon waarschuwing
            int alignCenter = (bufImage.getWidth() /2);
            int alignMiddle = (bufImage.getHeight() /2);
            Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
            int boxWidth = 400;
            int boxHeight = 150;
            
            StringBuffer message = new StringBuffer();
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_INTRO);
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_START);
            message.append(TITLE);
            message.append(" (");
            message.append(NAME);
            message.append(")");
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_END);
            message.append(KBConfiguration.MESSAGE_NO_DISPLAY_AGAIN);
            message.append(KBConfiguration.MESSAGE_REQUIRED_CREDITS);
            
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
