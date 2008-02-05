/*
 * CustomLayer.java
 *
 * Created on January 25, 2008, 10:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.util.HashMap;
import java.util.Map;
import nl.b3p.kaartenbalie.core.server.accounting.AccountManager;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;

/**
 *
 * @author Chris Kramer
 */
public abstract class ConfigLayer extends Layer {
    
    private static Map configLayers;
    
    static {
        configLayers = new HashMap();
        if (AccountManager.isEnableAccounting()) {
            configLayers.put(AllowTransactionsLayer.NAME, new AllowTransactionsLayer());
            configLayers.put(Allow100CTALayer.NAME, new Allow100CTALayer());
        }
    }
    
    public static void processConfig(String name, Map configMap) throws Exception{
        ConfigLayer configLayer = getConfigLayer(name);
        configLayer.processConfig(configMap);
    }
    public static Map getConfigLayers() {
        return configLayers;
    }
    
    public static ConfigLayer getConfigLayer(String name) throws Exception{
        ConfigLayer configLayer = (ConfigLayer) configLayers.get(name);
        if (configLayer == null) {
            throw new Exception("Config Layer for name '" + name + "' not found!");
        }
        return configLayer;
    }
    
    public ConfigLayer(String name, String title) {
        this.setName(name);
        this.setTitle(title);
        SrsBoundingBox srsbb1 = new SrsBoundingBox();
        srsbb1.setLayer(this);
        srsbb1.setMinx("0");
        srsbb1.setMiny("0");
        srsbb1.setMaxx("1");
        srsbb1.setMaxy("1");
        addSrsbb(srsbb1);
        SrsBoundingBox srsbb2 = new SrsBoundingBox();
        srsbb2.setLayer(this);
        srsbb2.setSrs("EPSG:28992");
        addSrsbb(srsbb2);
        setMetaData("http://www.b3partners.nl"); //??? 7.1.4.5.14
        setQueryable("0");
        setCascaded("0");
        setOpaque("0");
    }
    
    
    protected abstract void processConfig(Map configMap) throws Exception;
}
