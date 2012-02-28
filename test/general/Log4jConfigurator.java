package general;

/**
 *
 * @author rachelle
 */
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4jConfigurator {
    private static String logfile   = "/tmp/logs/kaartenbalie2.log";
    private static boolean configured = false;

    public synchronized static void configure() {
        System.out.println("configurating ");
        if (!configured) {            
            System.out.println("configurating logger");
            
            // TODO Auto-generated constructor stub
            Logger rootLogger = Logger.getRootLogger();
            try {
                rootLogger.addAppender(new FileAppender(new PatternLayout(),"/tmp/logs/kaartenbalie.log", false));
                
                configured = true;
                System.out.println("configurated logger");
            } 
            catch (IOException ex) {
                java.util.logging.Logger.getLogger(Log4jConfigurator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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