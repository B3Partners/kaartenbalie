package general;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.log4j.Logger;

/**
 *
 * @author rachelle
 */
public class B3TestCase extends TestCase {
    protected static String className;
    protected static Logger root   = null;
    protected static boolean configurated  = false;
    protected static MyEMFDatabase DB;
    protected String layerName    = "testLayer";
    protected String layerNames   = "testLayer1,testLayer2,testLayer3";
    
    public B3TestCase(String name){
        super(name);
        
        className   = name;
    }
    
    public static void setUpClass() throws Exception {
        System.out.println("configurating class");
          
        Log4jConfigurator.configure();
        
        //ConfigStub config    = new ConfigStub();
        //DB                  = new MyEMFDatabase();
        //DB.init(config);
        //DB.createEntityManager(MyEMFDatabase.MAIN_EM);
          
        configurated = true;
    }

    public static void tearDownClass() throws Exception {
    }
    
    public void setUp() throws Exception{
        super.setUp();
        
        if( !configurated ){
            setUpClass();
        }
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        
        Log4jConfigurator.shutdown();
    }
        
    /**
     * Checks if the string values are the same. Use this function for Strings instead of assertEquals()
     * 
     * @param expected      The expected string
     * @param actual        The string to test
     */
    public static void assertStringEquals(String expected,String actual){
        if( !expected.equals(actual) ){
            fail("Assertion failure. Expected <"+expected+"> but found <"+actual+">.");
            assertTrue(false);
        }
        else {
            assertTrue(true);
        }
    }
    
    /*
   public static Test suite(){
        throw new UnsupportedOperationException("Suit() should be overwritten in test classes");
    } */
}
