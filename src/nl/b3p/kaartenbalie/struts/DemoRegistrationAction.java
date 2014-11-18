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

import java.security.Principal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.b3p.commons.services.FormUtils;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.wms.capabilities.ServiceProvider;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.securityfilter.filter.SecurityRequestWrapper;

public class DemoRegistrationAction extends UserAction {

    private static final Log log = LogFactory.getLog(DemoRegistrationAction.class);
    protected static final String PREDEFINED_SP_ABBR = "demo.spabbr";
    protected static final String NEXTPAGE = "nextPage";

    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            user = new User();
            Calendar gc = new GregorianCalendar();
            gc.add(Calendar.MONTH, 1);
            user.setPersonalURL(User.createCode());
            user.setTimeout(gc.getTime());
        }
        populateUserForm(user, dynaForm, request);
        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }

    /** Method for saving a new service provider from input of a user.
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
    // <editor-fold defaultstate="" desc="save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUser(dynaForm, request, true);
        ActionForward af = saveCheck(user, mapping, dynaForm, request);
        if (af != null) {
            return af;
        }
        Organization organization = user.getMainOrganization();
        if (organization == null) {
            organization = new Organization();
        }

        populateRegistrationObject(user, organization, dynaForm, request);

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (organization.getId() == null) {
            em.persist(organization);
        } else {
            em.merge(organization);
        }

        user.setMainOrganization(organization);
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        em.flush();

        /*
         * Make sure that the system will accept the user already as logged in.
         * In order to do this we need to get the SecurityRequestWrapper and set
         * this user as Principal in the requets.
         */

        Principal principal = (Principal) user;
        if (request instanceof SecurityRequestWrapper) {
            SecurityRequestWrapper srw = (SecurityRequestWrapper) request;
            srw.setUserPrincipal(principal);
        }

        populateUserForm(user, dynaForm, request);

        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(NEXTPAGE);
    }
    // </editor-fold>
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param request The HTTP Request we are processing.
     * @param session Session object for the database.
     */
    // <editor-fold defaultstate="" desc="getStandardDemoLayerSet(HttpServletRequest request, Session session) method.">

    private Set getStandardDemoLayerSet(HttpServletRequest request) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        /*
         * Because every new demo user has access to the predefined server which will
         * be supported by B3Partners we first need to get this WMS server from the database
         * in order to set the rights to this new user.
         */
        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String spAbbr = messages.getMessage(locale, PREDEFINED_SP_ABBR);

        ServiceProvider stdServiceProvider = null;
        try {
            stdServiceProvider = (ServiceProvider) em.createQuery(
                    "from ServiceProvider sp where "
                    + "lower(sp.abbr) = lower(:abbr) ").setParameter("abbr", spAbbr).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        return stdServiceProvider.getAllLayers();
    }
    // </editor-fold>

    /** Method that fills a user and organization object with the user input from the forms.
     *
     * @param request The HTTP Request we are processing.
     * @param form The DynaValidatorForm bean for this request.
     * @param user User object that to be filled
     * @param organization Organization object that to be filled
     */
    // <editor-fold defaultstate="" desc="populateRegistrationObject(HttpServletRequest request, DynaValidatorForm dynaForm, User user, Organization organization) method.">
    private void populateRegistrationObject(User user, Organization organization, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {

        populateUserObject(user, dynaForm, request);

        Set orglayers = organization.getLayers();
        if (orglayers == null || orglayers.isEmpty()) {
            Set layers = getStandardDemoLayerSet(request);
            if (layers != null) {
                organization.setLayers(new HashSet(layers));
                // We nemen aan dat de standard set ok is
                organization.setHasValidGetCapabilities(true);
            }
        }

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        if (!user.checkRole(Roles.GUEST)) {
            List roles = em.createQuery("from Roles").getResultList();
            Iterator roleIt = roles.iterator();
            while (roleIt.hasNext()) {
                Roles role = (Roles) roleIt.next();
                if (role.getRole().equalsIgnoreCase(Roles.GUEST)) {
                    user.addRole(role);
                }
            }
        }

        organization.setName(FormUtils.nullIfEmpty(dynaForm.getString("organizationName")));
        organization.setTelephone(FormUtils.nullIfEmpty(dynaForm.getString("organizationTelephone")));

    }
    // </editor-fold>
}
