/*
 * ConfigLayerException.java
 *
 * Created on February 7, 2008, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris Kramer
 */
public class ConfigLayerException extends Exception {
    
    private static final long serialVersionUID = 9144089171508939394L;
    
    private ConfigLayer configLayer;
    private Map parameterMap;
    public ConfigLayerException() {
        this(null);
    }
    
    public ConfigLayerException(ConfigLayer configLayer) {
        this(configLayer, new HashMap());
    }
    public ConfigLayerException(ConfigLayer configLayer, Map parameterMap) {
        super("Exception for configurationLayer '" + configLayer +"'");
        parameterMap = new HashMap();
        this.configLayer = configLayer;
    }
    
    public ConfigLayer getConfigLayer() {
        return configLayer;
    }
    public Map getParameterMap() {
        return parameterMap;
    }
    
    
}
