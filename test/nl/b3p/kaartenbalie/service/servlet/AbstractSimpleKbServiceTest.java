/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service.servlet;

import general.B3TestCase;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rachelle
 */
public class AbstractSimpleKbServiceTest extends B3TestCase {
    
    public AbstractSimpleKbServiceTest(String name) {
        super(name);
    }

    public static void setUpClass() throws Exception {
        System.out.println("setting up class");
    }

    public static void tearDownClass() throws Exception {
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        /* URL logFile  = getClass().getResource("/src/log4j_test.properties");
        System.out.println("logfile : "+logFile.getPath());
        File file = new File(logFile.getPath());
        if( !file.exists() ){
            throw new IOException("incorrect logfile");
        } */
        
        System.out.println("setting up test");
    }
    
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of init method, of class AbstractSimpleKbService.
     */
    public void testInit() throws Exception {
        System.out.println("init");
        ServletConfig config = null;
        AbstractSimpleKbService instance = new AbstractSimpleKbServiceImpl();
        instance.init(config);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServletInfo method, of class AbstractSimpleKbService.
     */
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        AbstractSimpleKbService instance = new AbstractSimpleKbServiceImpl();
        String expResult = "";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class AbstractSimpleKbServiceImpl extends AbstractSimpleKbService {

        public void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws ServletException, IOException, Exception {
        }
    }
}
