/*
 * AllowTransactionsLayer.java
 *
 * Created on January 25, 2008, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.image.BufferedImage;
import java.util.Map;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;

/**
 *
 * @author Chris Kramer
 */
public class Allow100CTALayer extends ConfigLayer{
    
    public static final String NAME = "ALLOW100CWITHDRAWLS";
    public static final String TITLE = "> 100c toestaan";
    public static final String configValue = "allow100CTransactions";
    public Allow100CTALayer() {
        super(NAME, TITLE);
    }
    
    public void processConfig(Map configMap) throws Exception {
        configMap.put(configValue, new Boolean(true));
    }
    
    protected BufferedImage modifyBaseImage(DataWrapper data, BufferedImage bufImage,Map parameterMap) throws Exception {
        return bufImage;
    }
    
    
    
    
}
