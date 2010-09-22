package nl.b3p.kaartenbalie.struts;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;

public class RoleAction extends KaartenbalieCrudAction {

    private static final Log logger = LogFactory.getLog(RoleAction.class);

    protected static final String ROLE_JOINED_MESSAGE_KEY = "beheer.role.user.joined";
    protected static final String ROLE_PROTECTED_MESSAGE_KEY = "beheer.role.protected";
    protected static final String ROLE_NOTFOUND_ERROR_KEY = "error.rolenotfound";


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
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }

    /* Edit method which handles all editable requests.
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
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Roles role = getRole(dynaForm, request, false);
        if (role == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ROLE_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateRoleForm(role, dynaForm, request);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /* Method for saving a new organization from input of a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        logger.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        ActionErrors errors = dynaForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            addMessages(request, errors);
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, VALIDATION_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Roles role = getRole(dynaForm, request, true);
        if (null == role) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        
        populateRoleObject(dynaForm, role);
        
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String roleProtectedMessage = messages.getMessage(locale, ROLE_PROTECTED_MESSAGE_KEY);
        if (role.isProtectedRole()) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, null, roleProtectedMessage);
            return getAlternateForward(mapping, request);
        }

        if (role.getId() == null) {
            em.persist(role);
        } else {
            em.merge(role);
        }
        em.flush();
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Roles role = getRole(dynaForm, request, false);
        if (null == role) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String roleJoinedMessage = messages.getMessage(locale, ROLE_JOINED_MESSAGE_KEY);
        String roleProtectedMessage = messages.getMessage(locale, ROLE_PROTECTED_MESSAGE_KEY);

        logger.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        
        List userList = em.createQuery("from User u join u.roles r where r.id = :roleid")
                .setParameter("roleid", role.getId())
                .getResultList();

        StringBuilder strMessage = new StringBuilder();
        
        if (userList != null && !userList.isEmpty()) {
            String userName = "";
            boolean notFirstUser = false;
            
            strMessage.append(roleJoinedMessage.toString());

            Iterator iter = userList.iterator();
            while (iter.hasNext()) {
                Object[] user_roles = (Object[])iter.next();
                User u = null;

                if (user_roles != null)
                    u = (User) user_roles[0];

                if (u != null)
                    userName = u.getUsername();

                if (notFirstUser) {
                    strMessage.append(", ");
                } else {
                    strMessage.append(": ");
                    notFirstUser = true;
                }

                strMessage.append(userName);
            }

            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, null, strMessage.toString());
            
            return getDefaultForward(mapping, request);
        }

        if (role.isProtectedRole()) {
            addAlternateMessage(mapping, request, null, roleProtectedMessage);
        }

        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /* Method for deleting an organization selected by a user.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     *
     * @return an Actionforward object.
     *
     * @throws Exception, HibernateException
     */
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, Exception {
        logger.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Roles role = getRole(dynaForm, request, false);
        if (null == role) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String roleProtectedMessage = messages.getMessage(locale, ROLE_PROTECTED_MESSAGE_KEY);
        String roleJoinedMessage = messages.getMessage(locale, ROLE_JOINED_MESSAGE_KEY);

        if (role.isProtectedRole()) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, null, roleProtectedMessage);
            return getAlternateForward(mapping, request);
        }

        List userList = em.createQuery("from User u join u.roles r where r.id = :roleid").setParameter("roleid", role.getId()).getResultList();
        if (userList != null && !userList.isEmpty()) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, null, roleJoinedMessage);
            return getAlternateForward(mapping, request);
        }

        em.remove(role);
        em.flush();

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }

    /* Method which will fill the JSP form with the data of  a given organization.
     *
     * @param organization Organization object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */

    protected void populateRoleForm(Roles role, DynaValidatorForm dynaForm, HttpServletRequest request) throws JSONException {
        dynaForm.set("id", role.getId().toString());
        dynaForm.set("name", role.getRole());
    }

    /* Method that fills an organization object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param organization Organization object that to be filled
     * @param layerList List with all the layers
     * @param selectedLayers String array with the selected layers for this organization
     */
    private void populateRoleObject(DynaValidatorForm dynaForm, Roles role) throws Exception {
        role.setRole(FormUtils.nullIfEmpty(dynaForm.getString("name")));
    }


    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
    protected void createLists(DynaValidatorForm form, HttpServletRequest request) throws HibernateException, JSONException, Exception {
        super.createLists(form, request);
        logger.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        List roleslist = em.createQuery("from Roles").getResultList();
        request.setAttribute("roleslist", roleslist);
    }

    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    protected Roles getRole(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        logger.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        Roles role = null;
        Integer id = getID(dynaForm);

        if (null == id && createNew) {
            role = new Roles();
        } else if (null != id) {
            role = (Roles) em.find(Roles.class, id);
        }
        return role;
    }

    /* Method which gets the hidden id in a form.
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
    private Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
}
