package general;

/**
 *
 * @author rachelle
 */
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

public class Log4jConfigurator {
    private static boolean configured = false;

    public synchronized static void configure() {
        if (!configured) {                         
            String home         = System.getProperty("user.home");
            String seperator    = System.getProperty("file.separator");
            
            File logFile    = new File(home+seperator+"log4j.properties");
            if( logFile.exists() ){
                try {                    
                    PropertyConfigurator.configure(logFile.getAbsolutePath());
                                        
                    configured = true;
                } 
                catch(Exception ex) {
                    
                }
            }
            else {
                System.out.println("logfile error ");
            }
        }
    }

    public synchronized static void shutdown(){
        if( configured ){
            LogManager.shutdown();
            configured = false;
        }
    }
}