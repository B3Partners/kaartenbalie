/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import junit.framework.TestCase;

/**
 *
 * @author rachelle
 */
public class B3TestCase extends TestCase {
    public B3TestCase(String name){
        super(name);
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
}
