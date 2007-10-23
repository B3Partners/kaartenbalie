/*
 * $Id: LogoutAction.java 6914 2007-09-25 17:47:19Z Chris $
 */

package nl.b3p.kaartenbalie.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;

/**
 * Deze Action invalideert de sessie (met eventuele authenticatie info).
 */
public class LogoutAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.getSession().invalidate();
        return mapping.findForward("success");
    }
}

