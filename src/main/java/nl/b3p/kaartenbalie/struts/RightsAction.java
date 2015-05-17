package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;

/**
 *
 * @author B3Partners
 */
public class RightsAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(RightsAction.class);

    /* Execute method which handles all unspecified requests.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        createLists(dynaForm, request);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }


    @Override
    protected void createLists(DynaValidatorForm dynaForm, HttpServletRequest request)
            throws HibernateException,
            JSONException,
            Exception {
        super.createLists(dynaForm, request);
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        List organizationlist = em.createQuery("from Organization order by name").getResultList();
        request.setAttribute("organizationlist", organizationlist);

        List wfsSpList = em.createQuery("from WfsServiceProvider order by given_name").getResultList();
        List wfsDtoList = ServiceProviderDTO.createList(wfsSpList);
        List wmsSpList = em.createQuery("from ServiceProvider order by given_name").getResultList();
        List wmsDtoList = ServiceProviderDTO.createList(wmsSpList);

        List spDtoList = new ArrayList();

        if (wmsDtoList != null) {
            spDtoList.addAll(wmsDtoList);
        }

        if (wfsDtoList != null) {
            spDtoList.addAll(wfsDtoList);
        }

        if (spDtoList.size() > 0) {
            Collections.sort(spDtoList);
        }

        request.setAttribute("serviceproviderlist", spDtoList);

     }
}
