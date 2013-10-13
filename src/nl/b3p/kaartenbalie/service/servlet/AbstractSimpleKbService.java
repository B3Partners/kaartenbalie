/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.utils.OGCRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy
 * Created on 30-aug-2011, 14:47:24
 */
public abstract class AbstractSimpleKbService extends CallWMSServlet{
    
    private static Log log = LogFactory.getLog(AbstractSimpleKbService.class);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("Initializing "+this.getClass().getName());
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String url=request.getRequestURL().toString();
        String personalCode = OGCRequest.findPersonalCode(url);

        if (log.isDebugEnabled()){
            if (request.getQueryString()!=null){
                url+=request.getQueryString();
            }
            log.debug("Incoming request: "+url);
        }
        
        Object identity = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        PrintWriter out = response.getWriter();
        try {
            log.debug("Create entity manager ......");
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.SLD_EM);            
            em = getEntityManager();
            tx = em.getTransaction();
            tx.begin();           
            //check login.            
            User user = checkLogin(request, em, personalCode);
            
            //maak van de parameter een Integer array.            
            processRequest(request,response,out);
            tx.commit();            
        } catch (Exception ex) {
            log.error("Error: ", ex);
            try {
                tx.rollback();
            } catch (Exception ex2) {
                log.error("Error trying to rollback: ", ex2);
            }            
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.SLD_EM);
            out.close();
        }
    }
    
    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws ServletException, IOException, Exception;
    
    protected static EntityManager getEntityManager() throws Exception {
        return MyEMFDatabase.getEntityManager(MyEMFDatabase.SLD_EM);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


}
