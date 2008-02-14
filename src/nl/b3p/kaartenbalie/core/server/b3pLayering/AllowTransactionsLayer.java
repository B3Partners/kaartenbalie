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
import java.util.Map;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import nl.b3p.ogc.utils.KBConstants;


public class AllowTransactionsLayer extends ConfigLayer implements KBConstants{
    
    public static final String NAME = "allowwithdrawls";
    public static final String TITLE = "kb_afboekingen_toestaan";
    public static final String configValue = "allowTransactions";
    private static final String introduction = "Eén of meer van de door u opgevraagde lagen vereisen authorizatie voor het afboeken van het credittegoed. ";
    
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
            drawTitledMessageBox(g2,
                    TITLE,  introduction + MESSAGE_AUTHORIZATION_START + TITLE + MESSAGE_AUTHORIZATION_END + MESSAGE_NO_DISPLAY_AGAIN + MESSAGE_REQUIRED_CREDITS,
                    alignCenter - (boxWidth/2),
                    alignMiddle - (boxHeight /2),
                    boxWidth,boxHeight);
        }
        return bufImage;
    }
    
    
}
