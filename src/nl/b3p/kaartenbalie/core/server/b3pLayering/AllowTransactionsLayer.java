/*
 * AllowTransactionsLayer.java
 *
 * Created on January 25, 2008, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.util.Map;

/**
 *
 * @author Chris Kramer
 */
public class AllowTransactionsLayer extends ConfigLayer{
    
    public static final String NAME = "ALLOWWITHDRAWLS";
    public static final String TITLE = "Afboekingen toestaan";
    public static final String configValue = "allowTransactions";
    public AllowTransactionsLayer() {
        super(NAME, TITLE);
    }
    
    public AllowTransactionsLayer(String name, String title) {
        super(name, title);
    }
    
    public void processConfig(Map configMap) throws Exception {
        configMap.put(configValue, new Boolean(true));
    }
    
}
