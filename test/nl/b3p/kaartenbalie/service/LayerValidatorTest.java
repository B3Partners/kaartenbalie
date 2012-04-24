package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import general.LayerTest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import nl.b3p.wms.capabilities.Layer;
import nl.b3p.wms.capabilities.SrsBoundingBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class LayerValidatorTest extends KaartenbalieTestCase {
    private Set<Layer> normalLayers;
    private LayerValidator instance;
    
    public LayerValidatorTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        this.normalLayers   = this.createLayers();
        this.instance       = new LayerValidator(this.normalLayers);
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
     
        this.normalLayers   = null;
        this.instance       = null;
    }

    /**
     * Test of getLayers method, of class LayerValidator.
     */
    @Test
    public void testGetLayers() {
        assertEquals(this.normalLayers,this.instance.getLayers());
    }

    /**
     * Test of setLayers method, of class LayerValidator.
     */
    @Test
    public void testSetLayers() {
        Set<Layer> testLayers   = new HashSet(this.normalLayers);
        LayerTest testLayer = new LayerTest();
        testLayer.setTestId(3);
        testLayers.add(testLayer);
        
        this.instance.setLayers(testLayers);
        assertEquals(testLayers,this.instance.getLayers());
    }

    /**
     * Test of validate method, of class LayerValidator.
     */
    @Test
    public void testValidate() {
        assertTrue(this.instance.validate());
    }

    /**
     * Test of validateLatLonBoundingBox method, of class LayerValidator.
     */
    @Test
    public void testValidateLatLonBoundingBox() {
        double minx = 180.0, miny = 90.0, maxx = -180.0, maxy = -90.0;
        
        SrsBoundingBox llbb =    this.instance.validateLatLonBoundingBox();
        assertEquals(minx,Double.parseDouble( llbb.getMinx()),0 );
        assertEquals(miny,Double.parseDouble( llbb.getMiny()),0 );
        assertEquals(maxx,Double.parseDouble( llbb.getMaxx()),0 );
        assertEquals(maxy,Double.parseDouble( llbb.getMaxy()),0 );
    }

    /**
     * Test of addLayerSupportedSRS method, of class LayerValidator.
     */
    @Test
    public void testAddLayerSupportedSRS() {
        /* Test with empty layer */
        Iterator it = this.normalLayers.iterator();
        Layer layer = (Layer) it.next();
        HashMap<String,String> supported   = new HashMap<String,String>();
        this.instance.addLayerSupportedSRS(layer, supported);
        assertEquals(0,supported.size());
        
        /* Test with filled layer */
        SrsBoundingBox srsbb    = new SrsBoundingBox();
        srsbb.setSrs("Test SRS");
        layer.addSrsbb(srsbb);
        this.instance.addLayerSupportedSRS(layer, supported);
        assertEquals(1,supported.size());
    }

    /**
     * Test of validateSRS method, of class LayerValidator.
     */
    @Test
    public void testValidateSRS() {
        /* Test with empty layer list. Expected 1 empty result */
        String[] result = instance.validateSRS();
        assertEquals(1,result.length);
        
        /* Test with filled layer */
        Iterator it = this.normalLayers.iterator();
        Layer layer;
        SrsBoundingBox srsbb;
        while( it.hasNext() ){
            layer = (Layer) it.next();
            srsbb    = new SrsBoundingBox();
            srsbb.setSrs("Test");
            layer.addSrsbb(srsbb);
        } 
        
        this.instance.setLayers(normalLayers);
        result = instance.validateSRS();
        assertEquals(1,result.length);
    }
    
    /**
     * Generates a test collection of layers
     * 
     * @return a collection of layers
     */
    private Set<Layer> createLayers(){
        LayerTest testLayer = new LayerTest();
        testLayer.setTestId(1);
        LayerTest layer2 = new LayerTest();
        layer2.setTestId(4);
        HashSet organizationLayers  = new HashSet<Layer>();
        
        organizationLayers.add(layer2);
        organizationLayers.add(testLayer);
        
        return organizationLayers;
    }
}
