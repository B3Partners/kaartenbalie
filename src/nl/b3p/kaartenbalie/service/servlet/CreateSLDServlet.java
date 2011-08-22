/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.ogc.sld.SldWriter;
import nl.b3p.wms.capabilities.Style;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Roy
 */
public class CreateSLDServlet extends CallWMSServlet {

    private static Log log = null;
    public static final String STYLES_PARAM="styles";
    public static final String mimeType = "application/xml";
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Zet de logger
        log = LogFactory.getLog(this.getClass());
        log.info("Initializing Create SLD Servlet");
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Object identity = null;
        EntityManager em = null;
        EntityTransaction tx = null;
        PrintWriter out = response.getWriter();
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            log.debug("Getting entity manager ......");
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();
            tx.begin();           
            //check login.
            User user = checkLogin(request);
            //maak van de parameter een Integer array.
            String styleString=request.getParameter(STYLES_PARAM);
            String[] styleStringTokens=styleString.split(",");
            Integer[] styleIds= new Integer[styleStringTokens.length];
            for (int i=0; i < styleStringTokens.length; i++){
                styleIds[i]= new Integer(styleStringTokens[i]);
            }
            //haal de styles op.
            List<Style> styles= em.createQuery("from Style where id in ("+styleString+")")
                    .getResultList();
            
            
            SldWriter sldFact = new SldWriter();
            String xml = sldFact.createSLD(styles);
            
            response.setContentType(mimeType);            
            out.write(xml);
           
        } catch (Exception ex) {
            log.error("Error creating SLD: ", ex);
            try {
                tx.rollback();
            } catch (Exception ex2) {
                log.error("Error trying to rollback: ", ex2);
            }            
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
            out.close();
        }
        /*response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

        } finally {            
            out.close();
        }*/
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
