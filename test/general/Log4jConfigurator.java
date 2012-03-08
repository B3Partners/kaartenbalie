package general;

/**
 *
 * @author rachelle
 */
import java.io.File;
import org.apache.log4j.LogManager;
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