package nl.b3p.kaartenbalie.service;

import general.KaartenbalieTestCase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rachelle
 */
public class MapParserTest extends KaartenbalieTestCase {    

    private String map;
    private MapParser instance;
    
    public MapParserTest(String name) {
        super(name);
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        try {
            this.map = this.userDir + this.seperator + "mapfiles" + this.seperator + "example.map";
            
            this.instance = new MapParser(new File(this.map));
        } catch (IOException e) {
        }
    }
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        this.instance = null;
    }

    /**
     * Test of parse method, of class MapParser.
     */
    @Test
    public void testParse() {
        try {
            this.instance.parse();
            
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }

    /**
     * Test of getWebMetadata method, of class MapParser.
     */
    @Test
    public void testGetWebMetadata() {
        try {
            this.instance.parse();
            
            Map<String, String> result = this.instance.getWebMetadata();
            if (result.size() > 0) {
                assertTrue(true);
            } else {
                fail("Result empty");
            }
        } catch (FileNotFoundException e) {
            fail("Exception : " + e.getLocalizedMessage());
        } catch (Exception e) {
            fail("Exception : " + e.getLocalizedMessage());
        }
    }
}
