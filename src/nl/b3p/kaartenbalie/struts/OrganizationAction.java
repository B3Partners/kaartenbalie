/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.struts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.kaartenbalie.core.server.datawarehousing.DwObjectAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jytte
 */
public abstract class OrganizationAction extends KaartenbalieCrudAction {

    private final static String SUCCESS = "success";
    private static final Log log = LogFactory.getLog(OrganizationAction.class);
    protected static final String ORGANIZATION_LINKED_ERROR_KEY = "error.organizationstilllinked";
    protected static final String CAPABILITY_WARNING_KEY = "warning.saveorganization";
    protected static final String ORG_NOTFOUND_ERROR_KEY = "error.organizationnotfound";
    protected static final String DELETE_ADMIN_ERROR_KEY = "error.deleteadmin";
    protected static final String USER_JOINED_KEY = "beheer.org.user.joined";
    protected static final String CREDITS_JOINED_KEY = "beheer.org.credits.joined";

    /** Creates a new instance of OrganizationAction */
    public OrganizationAction() {
    }

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
        this.createLists(dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request);
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
    // <editor-fold defaultstate="" desc="edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Organization organization = getOrganization(dynaForm, request, false);
        if (organization == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, ORG_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        populateOrganizationForm(organization, dynaForm, request);
        return super.edit(mapping, dynaForm, request, response);
    }
    // </editor-fold>
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        EntityManager em = getEntityManager();
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String userJoinedMessage = messages.getMessage(locale, USER_JOINED_KEY);
        String creditsJoinedMessage = messages.getMessage(locale, CREDITS_JOINED_KEY);
        StringBuffer strMessage = null;

        Set userList = organization.getUser();
        if (userList != null && !userList.isEmpty()) {
            strMessage = new StringBuffer();
            Iterator it = userList.iterator();
            boolean notFirstUser = false;
            while (it.hasNext()) {
                User u = (User) it.next();
                if (notFirstUser) {
                    strMessage.append(", ");
                } else {
                    strMessage.append(userJoinedMessage);
                    strMessage.append(": ");
                    notFirstUser = true;
                }
                strMessage.append(u.getSurname());
            }
            addAlternateMessage(mapping, request, null, strMessage.toString());
        }
        Account acc = organization.getAccount();
        if (acc != null) {
            BigDecimal cb = acc.getCreditBalance();
            if (cb != null && cb.doubleValue() > 0) {
                cb.setScale(2, RoundingMode.HALF_UP);
                strMessage = new StringBuffer();
                strMessage.append(creditsJoinedMessage);
                strMessage.append(": ");
                strMessage.append(cb.toString());
                addAlternateMessage(mapping, request, null, strMessage.toString());
            }
        }
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser != null) {
            sessionOrg = sessionUser.getOrganization();
        }
        if (sessionOrg != null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request);
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
        EntityManager em = getEntityManager();
        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        Organization organization = getOrganization(dynaForm, request, false);
        if (null == organization) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        User sessionUser = (User) request.getUserPrincipal();
        Organization sessionOrg = null;
        if (sessionUser != null) {
            sessionOrg = sessionUser.getOrganization();
        }
        if (sessionOrg != null && sessionOrg.getId().equals(organization.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }
        em.remove(organization);
        em.flush();
        getDataWarehousing().enlist(Organization.class, organization.getId(), DwObjectAction.REMOVE);
        return super.delete(mapping, dynaForm, request, response);
    }

    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
    // <editor-fold defaultstate="" desc="create(DynaValidatorForm form, HttpServletRequest request) method.">
    public ActionForward create(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws HibernateException, JSONException, Exception {
        request.setAttribute("layerList", createTree());
        return super.create(mapping, dynaForm, request, response);
    }
    // </editor-fold
    /* Creates a list with the available layers.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws HibernateException, JSONException, Exception
     */
// <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">
    protected void createLists(DynaValidatorForm form, HttpServletRequest request) throws HibernateException, JSONException, Exception {
        super.createLists(form, request);
        EntityManager em = getEntityManager();
        List organizationlist = em.createQuery("from Organization").getResultList();
        request.setAttribute("organizationlist", organizationlist);
    }
// </editor-fold>
    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return an Organization object.
     */
    protected Organization getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {
        EntityManager em = getEntityManager();
        Organization organization = null;
        Integer id = getID(dynaForm);

        if (null == id && createNew) {
            organization = new Organization();
        } else if (null != id) {
            organization = (Organization) em.find(Organization.class, id);
        }
        return organization;
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

    protected abstract JSONObject createTree() throws JSONException;

    protected abstract void populateOrganizationForm(Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws JSONException;
}
