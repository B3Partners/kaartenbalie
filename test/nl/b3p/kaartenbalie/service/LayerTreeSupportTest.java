package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import general.LayerTest;
import general.ServiceProviderTest;
import java.util.HashSet;
import java.util.Set;
import nl.b3p.ogc.wfs.v110.WfsLayer;
import nl.b3p.ogc.wfs.v110.WfsServiceProvider;
import nl.b3p.wms.capabilities.Layer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class LayerTreeSupportTest extends KaartenbalieTestCase {
    private LayerTest testLayer;
    private WfsLayer testWfsLayer;
    
    public LayerTreeSupportTest(String name){
        super(name);
    }

    /**
     * Test of hasVisibility method, of class LayerTreeSupport.
     */
    @Test
    public void testHasVisibility_Layer_Set() {
        Set organizationLayers  = this.createLayers();
        
        assertFalse(LayerTreeSupport.hasVisibility(this.testLayer,null));
        assertFalse(LayerTreeSupport.hasVisibility(this.testLayer,new HashSet<Layer>()) );
               
        assertTrue(LayerTreeSupport.hasVisibility(this.testLayer,organizationLayers));
    }

    /**
     * Test of hasVisibility method, of class LayerTreeSupport.
     */
    @Test
    public void testHasVisibility_WfsLayer_Set() {
        Set organizationLayers  = this.createWfsLayers();
        
        assertFalse(LayerTreeSupport.hasVisibility(this.testWfsLayer,null));
        assertFalse(LayerTreeSupport.hasVisibility(this.testWfsLayer,new HashSet<Layer>()));
        
        assertTrue(LayerTreeSupport.hasVisibility(this.testWfsLayer,organizationLayers));
    }

    /**
     * Test of createTreeList method, of class LayerTreeSupport.
     */
    @Test
    public void testCreateTreeList(){
        Set organizationLayers  = this.createLayers();
        Set emptyLayers         = new HashSet<Layer>();
        
        try {        
            JSONObject test         = new JSONObject();
            test = LayerTreeSupport.createTreeList(organizationLayers, emptyLayers, test, true);
            assertFalse(test.has("children"));
        
            test = new JSONObject();        
            test = LayerTreeSupport.createTreeList(organizationLayers, emptyLayers, test, false);
            assertTrue(test.has("children"));
        
            test = new JSONObject();
            test = LayerTreeSupport.createTreeList(organizationLayers, organizationLayers, test, true);
            assertTrue(test.has("children"));
        }
        catch(JSONException e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of createWfsTreeList method, of class LayerTreeSupport.
     */
    @Test
    public void testCreateWfsTreeList(){
        Set organizationLayers  = this.createWfsLayers();
        Set emptyLayers         = new HashSet<WfsLayer>();
        
        try {        
            JSONObject test         = new JSONObject();
            test = LayerTreeSupport.createWfsTreeList(organizationLayers, emptyLayers, test, true);
            assertFalse(test.has("children"));
        
            test = new JSONObject();        
            test = LayerTreeSupport.createWfsTreeList(organizationLayers, emptyLayers, test, false);
            assertTrue(test.has("children"));
        
            test = new JSONObject();
            test = LayerTreeSupport.createWfsTreeList(organizationLayers, organizationLayers, test, true);
            assertTrue(test.has("children"));
        }
        catch(JSONException e){
            fail("Exception : "+ e.getLocalizedMessage());
        }
    }

    /**
     * Test of serviceProviderToJSON method, of class LayerTreeSupport.
     */
    @Test
    public void testServiceProviderToJSON_WfsServiceProvider(){
        WfsServiceProvider serviceProvider  =  new WfsServiceProvider();
        serviceProvider.setId(1);
        serviceProvider.setGivenName("test WFS service provider");
        
        try {
            JSONObject result = LayerTreeSupport.serviceProviderToJSON(serviceProvider);
            
            assertStringEquals("wfs" + serviceProvider.getId(),result.getString("id"));
            assertStringEquals(serviceProvider.getGivenName(),result.getString("name"));
            assertStringEquals("serviceprovider",result.getString("type"));
        }
        catch(JSONException e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of serviceProviderToJSON method, of class LayerTreeSupport.
     */
    @Test
    public void testServiceProviderToJSON_ServiceProvider(){
        ServiceProviderTest serviceProvider  =  new ServiceProviderTest();
        serviceProvider.setTestId(1);
        serviceProvider.setGivenName("test service provider");
        
        try {
            JSONObject result = LayerTreeSupport.serviceProviderToJSON(serviceProvider);
            
            assertStringEquals("wms" + serviceProvider.getId(),result.getString("id"));
            assertStringEquals(serviceProvider.getGivenName(),result.getString("name"));
            assertStringEquals("serviceprovider",result.getString("type"));
        }
        catch(JSONException e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of getLayerByUniqueName method, of class LayerTreeSupport.
     */
    @Test
    public void testGetLayerByUniqueName(){
        try {
            LayerTreeSupport.getLayerByUniqueName(this.entityManager, "");
            
            fail("Expected function to throw a exception. Incorrect layername");
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        try {
            LayerTreeSupport.getLayerByUniqueName(this.entityManager, "_example");
            
            fail("Expected function to throw a exception. Invalid layername");
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        try {
            LayerTreeSupport.getLayerByUniqueName(this.entityManager, "layer_example");
            fail("Expected function to fail. Layer is not mapped");
        }
        catch(Exception e){
            String message  = e.getLocalizedMessage();
            if( message.contains("Layer is not mapped") ){
                assertTrue(true);
            }
            else {
                fail("Exception : "+message);
            }
        }
    }

    /**
     * Test of getWfsLayerByUniqueName method, of class LayerTreeSupport.
     */
    @Test
    public void testGetWfsLayerByUniqueName(){
        try {
            LayerTreeSupport.getWfsLayerByUniqueName(this.entityManager, "");
            
            fail("Expected function to throw a exception. Incorrect layername");
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        try {
            LayerTreeSupport.getWfsLayerByUniqueName(this.entityManager, "_example");
            
            fail("Expected function to throw a exception. Invalid layername");
        }
        catch(Exception e){
            assertTrue(true);
        }
        
        try {
            LayerTreeSupport.getWfsLayerByUniqueName(this.entityManager, "layer_example");
            fail("Expected function to fail. Layer is not mapped");
        }
        catch(Exception e){
            String message  = e.getLocalizedMessage();
            if( message.contains("Layer is not mapped") ){
                assertTrue(true);
            }
            else {
                fail("Exception : "+message);
            }
        }
    }

    /**
     * Test of createTree method, of class LayerTreeSupport.
     */
    @Test
    public void testCreateTree(){
        try {
            String rootName     = "tests";
            Set<Layer> organizationLayers   = this.createLayers();
            JSONObject result = LayerTreeSupport.createTree(this.entityManager, 
                    rootName, organizationLayers, false);

            assertTrue(result.has("name"));
            assertStringEquals(rootName,result.getString("name"));
            assertTrue(result.has("id"));
            assertStringEquals("wms" + rootName,result.getString("id"));
            assertTrue(result.has("children"));
            
            organizationLayers   = this.createLayers();
            result = LayerTreeSupport.createTree(this.entityManager, 
                    rootName, organizationLayers, true);

            assertTrue(result.has("name"));
            assertStringEquals(rootName,result.getString("name"));
            assertTrue(result.has("id"));
            assertStringEquals("wms" + rootName,result.getString("id"));
            assertTrue(result.has("children"));
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }

    /**
     * Test of createWfsTree method, of class LayerTreeSupport.
     */
    @Test
    public void testCreateWfsTree(){
        try {
            String rootName     = "tests";
            Set<Layer> organizationLayers   = this.createLayers();
            JSONObject result = LayerTreeSupport.createWfsTree(this.entityManager, 
                    rootName, organizationLayers, false);

            assertTrue(result.has("name"));
            assertStringEquals(rootName,result.getString("name"));
            assertTrue(result.has("id"));
            assertStringEquals("wfs" + rootName,result.getString("id"));
            assertTrue(result.has("children"));
            
            organizationLayers   = this.createLayers();
            result = LayerTreeSupport.createWfsTree(this.entityManager, 
                    rootName, organizationLayers, true);

            assertTrue(result.has("name"));
            assertStringEquals(rootName,result.getString("name"));
            assertTrue(result.has("id"));
            assertStringEquals("wfs" + rootName,result.getString("id"));
            assertTrue(result.has("children"));
        }
        catch(Exception e){
            fail("Exception : "+e.getLocalizedMessage());
        }
    }
    
    /**
     * Generates a test collection of layers
     * 
     * @return a collection of layers
     */
    private Set<Layer> createLayers(){
        this.testLayer = new LayerTest();
        this.testLayer.setTestId(1);
        LayerTest layer2 = new LayerTest();
        layer2.setTestId(4);
        HashSet organizationLayers  = new HashSet<Layer>();
        
        organizationLayers.add(layer2);
        organizationLayers.add(this.testLayer);
        
        return organizationLayers;
    }

    /**
     * Generates a test collection of Wfs layers
     * 
     * @return a collection of Wfs layers
     */
    private Set<Layer> createWfsLayers() {
        this.testWfsLayer = new WfsLayer();
        this.testWfsLayer.setId(1);
        this.testWfsLayer.setTitle("layer 1");
        this.testWfsLayer.setName("layer 1");
        WfsLayer layer2 = new WfsLayer();
        layer2.setId(2); 
        layer2.setTitle("layer 2");
        layer2.setName("layer 2");
        HashSet organizationLayers  = new HashSet<WfsLayer>();
        
        organizationLayers.add(layer2);
        organizationLayers.add(this.testWfsLayer);
        
        return organizationLayers;
    }
}
