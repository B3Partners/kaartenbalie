package general;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author rachelle
 */
public class TestFinder {
    /**
     * MAINwill only be runned from the commandline
     * NOT from de IDE
     */
    
    
    /**
     * Collect de suite method from the given class
     * 
     * @param className     The class to  test
     * @param args          Tests to test instead of the suite
     */
    public static void run(Class className,String[] args){
        TestSuite suite = null;
        
        if( args.length > 0 ){
            try {
                Constructor ctor = className.getConstructor(new Class[]{ String.class });
                
                suite   = new TestSuite();
                for(int i=0; i<args.length; i++){
                    suite.addTest((TestCase) ctor.newInstance(new Object[]{args[i]}));
                }
            }
            catch(Exception e){
                System.err.println("loading fail "+e.getMessage());
                System.exit(1);
            }
        }
        else {
            try {
                Method suite_method = className.getMethod("suite",new Class[0]);
                suite   = (TestSuite) suite_method.invoke(null, null);
            }
            catch(Exception e){
                // Whoops no public suite() in that class
                
                suite   = new TestSuite(className);
            }
        }
        
        junit.textui.TestRunner.run(suite);
    }
}
