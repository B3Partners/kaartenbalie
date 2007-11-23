/*
 * AccountManager.java
 *
 * Created on November 15, 2007, 9:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class AccountManager extends HttpServlet implements Runnable {
    
    private static Thread amThread;
    public static final long serialVersionUID = 856294562L;
    private static boolean enableAccounting = false;
    private boolean threadSuspended;
    
    
    /** Creates a new instance of AccountManager */
    public AccountManager() {
        
    }
    
    public void init(ServletConfig config) throws ServletException {
        amThread = new Thread(this);
        amThread.start();
    }
    public static AccountStub getAccountStub() {
        AccountStub accountStub = new AccountStub();
        return accountStub;
    }
    
    
    public static void main(String [] args) throws Exception {
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        AccountManager am = new AccountManager();
        am.init(null);
        
    }
    
    public void run() {
        try {
            
            synchronized(this) {
                while (threadSuspended) {
                    wait();
                }
            }
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void setEnableAccounting(boolean state) {
        enableAccounting = state;
    }
    
    
}
