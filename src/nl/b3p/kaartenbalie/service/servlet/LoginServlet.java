package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service to check kaartenbalie login without fetching wms getcap. This way we
 * can check (ldap) user/passw each time and cache the ServiceProvider object
 * seperately in Gisviewer.
 *
 * @author Boy de Wit
 */
public class LoginServlet extends GeneralServlet {

    protected static Log logger = LogFactory.getLog(LoginServlet.class);

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean canLogin = canLogin(request);

        if (!canLogin) {
            response.sendError(response.SC_FORBIDDEN, "Ongeldige inlog!");
        }
    }

    private boolean canLogin(HttpServletRequest request) {
        Object identity = null;
        EntityTransaction tx = null;
        User user = null;
        
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            tx = em.getTransaction();

            tx.begin();
            
            user = checkLogin(request, null);

            tx.commit();
        } catch (AccessDeniedException ex) {
            /* Still commit changes to User lastLoginState. */
            if (tx != null && tx.isActive()) {
                tx.commit();
            }

            return false;
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            
            return false;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
        
        if (user == null) {
            return false;
        }

        return true;
    }

    @Override
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getServletInfo() {
        return "Service to check kaartenbalie login without fetching wms getcap.";
    }
}
