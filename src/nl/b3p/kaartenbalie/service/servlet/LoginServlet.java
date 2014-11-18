package nl.b3p.kaartenbalie.service.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.service.AccessDeniedException;
import nl.b3p.kaartenbalie.service.requesthandler.DataWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Service to check kaartenbalie login without fetching wms getcap. This way we
 * can check (ldap) user/passw each time and cache the ServiceProvider object
 * seperately in Gisviewer.
 *
 * @author Chris van Lith
 */
public class LoginServlet extends GeneralServlet {

    protected static Log log = LogFactory.getLog(LoginServlet.class);

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = checkLogin(request, null);
            log.debug("Username: " + user==null?"(null)":user.getName());
            if (user == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access forbidden for Kaartenbalie");
            }
        } catch (AccessDeniedException ex) {
            response.addHeader("WWW-Authenticate","Basic realm=\"Kaartenbalie login\"");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied for Kaartenbalie");
        }
     }

    @Override
    public String getServletInfo() {
        return "Service to check kaartenbalie login without fetching wms getcap.";
    }

    @Override
    public void parseRequestAndData(DataWrapper data, User user) throws IllegalArgumentException, UnsupportedOperationException, IOException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
