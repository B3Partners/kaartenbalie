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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import nl.b3p.ogc.utils.KBCrypter;
import nl.b3p.wms.capabilities.Roles;
import nl.b3p.kaartenbalie.core.server.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;
import org.securityfilter.filter.SecurityRequestWrapper;

public class UserAction extends KaartenbalieCrudAction {

    private static final Log log = LogFactory.getLog(UserAction.class);
    protected static final String NON_UNIQUE_USERNAME_ERROR_KEY = "error.nonuniqueusername";
    protected static final String USER_NOTFOUND_ERROR_KEY = "error.usernotfound";
    protected static final String NONMATCHING_PASSWORDS_ERROR_KEY = "error.passwordmatch";
    protected static final String NO_PASSWORDS_ERROR_KEY = "error.nopassword";
    protected static final String DELETE_ADMIN_ERROR_KEY = "error.deleteadmin";
    protected static final String DATE_PARSE_ERROR_KEY = "error.dateparse";
    protected static final String DATE_INPUT_ERROR_KEY = "error.dateinput";
    protected static final String CAPABILITY_WARNING_KEY = "warning.saveorganization";
    protected static final String LAST_JOINED_KEY = "beheer.user.last.joined";
    protected static final String NOROLES_WARNING_KEY = "warning.noroles";

    /* Execute method which handles all executable requests.
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
    // <editor-fold defaultstate="" desc="unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    @Override
    public ActionForward unspecified(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, USER_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        populateUserForm(user, dynaForm, request);
        createLists(dynaForm, request);

        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }
    // </editor-fold>
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

    public ActionForward cancel(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        createLists(dynaForm, request);

        prepareMethod(dynaForm, request, LIST, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return mapping.findForward(SUCCESS);
    }
    
    @Override
    public ActionForward edit(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, USER_NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        populateUserForm(user, dynaForm, request);
        createLists(dynaForm, request);
        prepareMethod(dynaForm, request, EDIT, LIST);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>
    /* Method for saving a new or updating an existing user.
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

    @Override
    public ActionForward save(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = getUser(dynaForm, request, true);
        ActionForward af = saveCheck(user, mapping, dynaForm, request);
        if (af != null) {
            return af;
            /*
             * Check if this user gets a valid capability when using this personalURL
             */
        }

        Organization org = user.getMainOrganization();
        if (org != null && !org.getHasValidGetCapabilities()) {
            // TODO: we should check for the combination of all organisations
            addAlternateMessage(mapping, request, CAPABILITY_WARNING_KEY);
        }

        /* controleren of er rollen zijn gekozen */
        Integer[] roleSelected = (Integer[])dynaForm.get("roleSelected");
        int size = roleSelected.length;

        if (size < 1) {
            addAlternateMessage(mapping, request, NOROLES_WARNING_KEY);
        }

        populateUserObject(user, dynaForm, request);

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
        em.flush();

        updatePrincipal(request, user);

        populateUserForm(user, dynaForm, request);
        createLists(dynaForm, request);

        prepareMethod(dynaForm, request, EDIT, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>

    public ActionForward saveCheck(User user, ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        log.debug("Getting entity manager ......");
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

        if (user == null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        try {
            User dbUser = (User) em.createQuery(
                    "from User u where "
                    + "lower(u.username) = lower(:username) ").setParameter("username", FormUtils.nullIfEmpty(dynaForm.getString("username"))).getSingleResult();
            if (dbUser != null && (dbUser.getId() != user.getId())) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, NON_UNIQUE_USERNAME_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        } catch (NoResultException nre) {
            //this is good!; This means that there are no other users in the DB with this username..
            //therefore nothing has to be done here.
        }

        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        String repeatpassword = FormUtils.nullIfEmpty(dynaForm.getString("repeatpassword"));

        /* wil nieuwe gebruiker aanmaken maar ww zijn niet ingevuld */
        if (user.getId() == null && password == null) {
            prepareMethod(dynaForm, request, EDIT, EDIT);
            addAlternateMessage(mapping, request, NO_PASSWORDS_ERROR_KEY);

            return getAlternateForward(mapping, request);
        }

        if (password != null || repeatpassword != null) {
            if (password == null || !password.equals(repeatpassword)) {
                prepareMethod(dynaForm, request, EDIT, LIST);
                addAlternateMessage(mapping, request, NONMATCHING_PASSWORDS_ERROR_KEY);
                return getAlternateForward(mapping, request);
            }
        }

        Date oldDate = user.getTimeout();
        Date newDate = FormUtils.FormStringToDate(dynaForm.getString("timeout"), request.getLocale());
        if (newDate == null && oldDate != null) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, DATE_PARSE_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        // Alles ok
        return null;
    }

    @Override
    public ActionForward deleteConfirm(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        User sessionUser = (User) request.getUserPrincipal();
        if (sessionUser.getId().equals(user.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        MessageResources messages = getResources(request);
        Locale locale = getLocale(request);
        String lastJoinedMessage = messages.getMessage(locale, LAST_JOINED_KEY);

        Organization org = user.getMainOrganization();
        Set userList = null;

        if (org != null) {
            userList = org.getUsers();
        }

        if (userList == null || userList.size() <= 1) {
            addAlternateMessage(mapping, request, null, lastJoinedMessage);
        }

        prepareMethod(dynaForm, request, DELETE, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);

        return getDefaultForward(mapping, request);
    }

    /* Method for deleting a user.
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
    // <editor-fold defaultstate="" desc="delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) method.">
    @Override
    public ActionForward delete(ActionMapping mapping, DynaValidatorForm dynaForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        if (!isTokenValid(request)) {
            prepareMethod(dynaForm, request, EDIT, LIST);
            addAlternateMessage(mapping, request, TOKEN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        User user = getUser(dynaForm, request, false);
        if (user == null) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, NOTFOUND_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        User sessionUser = (User) request.getUserPrincipal();
        if (sessionUser.getId().equals(user.getId())) {
            prepareMethod(dynaForm, request, LIST, EDIT);
            addAlternateMessage(mapping, request, DELETE_ADMIN_ERROR_KEY);
            return getAlternateForward(mapping, request);
        }

        /* 
         * Als je een gebruiker probeert te verwijderen waarvan de main organization
         * null is geeft hij een fout. Wellicht als in het verleden handmatig een
         * aanpassing is gedaan in de database. Tijdelijk even op de beheerder org zetten
         * waarna de gebruiker gewist kan worden.
         *
         * TODO: Nagaan op welke plekken die main org null zou kunnen worden.
        */
        if (user.getMainOrganization() == null) {
            user.setMainOrganization(sessionUser.getMainOrganization());
            em.merge(user);
            em.flush();
        }

        em.remove(user);
        em.flush();

        dynaForm.initialize(mapping);
        prepareMethod(dynaForm, request, LIST, EDIT);
        addDefaultMessage(mapping, request, ACKNOWLEDGE_MESSAGES);
        return getDefaultForward(mapping, request);
    }
    // </editor-fold>
    /* Creates a list of all the users in the database.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     *
     * @throws Exception
     */
    // <editor-fold defaultstate="" desc="createLists(DynaValidatorForm form, HttpServletRequest request) method.">

    @Override
    public void createLists(DynaValidatorForm form, HttpServletRequest request) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        super.createLists(form, request);
        List userList = em.createQuery("from User order by username").getResultList();
        request.setAttribute("userlist", userList);

        Date now = new Date();
        List<User> invalidUsers = em.createQuery("from User where timeout < :date")
                .setParameter("date", now)
                .getResultList();

        List<Integer> invalidUserIds = new ArrayList();
        for (User u : invalidUsers) {
            invalidUserIds.add(u.getId());
        }

        request.setAttribute("invalidUserIds", invalidUserIds);

        List organizationlist = em.createQuery("from Organization order by name").getResultList();
        request.setAttribute("organizationlist", organizationlist);

        List roles = em.createQuery("from Roles order by id").getResultList();
        request.setAttribute("userrolelist", roles);
    }
    // </editor-fold>
    //-------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    //-------------------------------------------------------------------------------------------------------
    /* Private method which gets the hidden id in a form.
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
    // <editor-fold defaultstate="" desc="getID(DynaValidatorForm dynaForm) method.">

    protected Integer getID(DynaValidatorForm dynaForm) {
        return FormUtils.StringToInteger(dynaForm.getString("id"));
    }
    // </editor-fold>
    /* Method which returns the user with a specified id or a new user if no id is given.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param createNew A boolean which indicates if a new object has to be created.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a User object.
     */
    // <editor-fold defaultstate="" desc="getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew, Integer id) method.">

    protected User getUser(DynaValidatorForm dynaForm, HttpServletRequest request, boolean createNew) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        User sessUser = (User) request.getUserPrincipal();
        // Alleen beheeders mogen iemand anders bewerken
        if (!request.isUserInRole(Roles.ADMIN) && !createNew) {
            if (sessUser == null) {
                return null;
            }
            return (User) em.createQuery("from User u where u.id = :id").setParameter("id", sessUser.getId()).getSingleResult();
        }

        User user = null;
        Integer id = getID(dynaForm);
        if (null == id && createNew) {
            user = new User();
        } else if (null != id) {
            user = (User) em.find(User.class, new Integer(id.intValue()));
        }

        if (user == null) {
            return sessUser;
        }
        return user;
    }

    protected boolean updatePrincipal(HttpServletRequest request, User user) {
        User sessUser = (User) request.getUserPrincipal();
        if (!sessUser.equals(user)) {
            return false;
        }
        // need to update logged in user
        if (request instanceof SecurityRequestWrapper) {
            SecurityRequestWrapper srw = (SecurityRequestWrapper) request;
            srw.setUserPrincipal(user);
            return true;
        }
        return false;
    }

    // </editor-fold>
    /* Method which returns the organization with a specified id.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param id An Integer indicating which organization id has to be searched for.
     *
     * @return a Organization object.
     */
    // <editor-fold defaultstate="" desc="getOrganization(DynaValidatorForm dynaForm, HttpServletRequest request, Integer id) method.">
    private Organization getOrganization(Integer id) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        return (Organization) em.find(Organization.class, id);
    }
    // </editor-fold>
    /* Method which will fill-in the JSP form with the data of a given user.
     *
     * @param user User object from which the information has to be printed.
     * @param form The DynaValidatorForm bean for this request.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) method.">

    protected void populateUserForm(User user, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {

        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();

        dynaForm.set("id", FormUtils.IntegerToString(user.getId()));
        dynaForm.set("firstname", user.getFirstName());
        dynaForm.set("surname", user.getSurname());
        dynaForm.set("emailAddress", user.getEmailAddress());
        dynaForm.set("username", user.getUsername());
        dynaForm.set("personalURL", user.getPersonalURL(request));
        dynaForm.set("currentAddress", request.getRemoteAddr());

        dynaForm.set("password", "");
        dynaForm.set("repeatpassword", "");

        Integer[] roleSelected = null;
        Set uroles = user.getRoles();
        if (uroles != null && !uroles.isEmpty()) {
            ArrayList roleSet = new ArrayList(uroles);
            roleSelected = new Integer[roleSet.size()];
            for (int i = 0; i < roleSet.size(); i++) {
                Roles role = (Roles) roleSet.get(i);
                roleSelected[i] = role.getId();
            }
        }
        dynaForm.set("roleSelected", roleSelected);

        String[] orgSelected = null;
        Set orgs = user.getAllOrganizations();
        ArrayList orgSet =new ArrayList(orgs);

        Organization mainOrg = user.getMainOrganization();
        orgSelected = new String[orgSet.size()];
        for (int i = 0; i < orgSet.size(); i++) {
            Organization org = (Organization) orgSet.get(i);
            orgSelected[i] = FormUtils.IntegerToString(org.getId());
        }
        if (orgSelected.length>0) {
            dynaForm.set("orgSelected", orgSelected);
        }
        if (mainOrg!=null) {
            dynaForm.set("mainOrganization", FormUtils.IntegerToString(mainOrg.getId()));
        }

        StringBuilder registeredIP = new StringBuilder();
        Set userips = user.getIps();
        if (userips != null && !userips.isEmpty()) {
            Iterator it = userips.iterator();
            while (it.hasNext()) {
                String ipaddress = (String) it.next();
                if (registeredIP.length() > 0) {
                    registeredIP.append(",");
                }
                registeredIP.append(ipaddress);
            }
        } else {
            registeredIP.append(request.getRemoteAddr());
        }
        dynaForm.set("registeredIP", registeredIP.toString());

        Date timeout = user.getTimeout();
        if (timeout == null) {
            timeout = getDefaultTimeOut(3);
        }
        dynaForm.set("timeout", FormUtils.DateToFormString(timeout, request.getLocale()));

        if (mainOrg == null) {
            dynaForm.set("organizationName", "");
            dynaForm.set("organizationTelephone", "");
        } else {
            dynaForm.set("organizationName", mainOrg.getName());
            dynaForm.set("organizationTelephone", mainOrg.getTelephone());
        }


    }
    // </editor-fold>
    /* Method that fills a user object with the user input from the forms.
     *
     * @param form The DynaValidatorForm bean for this request.
     * @param user User object that to be filled.
     * @param request The HTTP Request we are processing.
     */
    // <editor-fold defaultstate="" desc="populateUserObject(DynaValidatorForm dynaForm, User user, HttpServletRequest request) method.">

    protected void populateUserObject(User user, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        log.debug("Getting entity manager ......");
        EntityManager em = getEntityManager();
        user.setFirstName(FormUtils.nullIfEmpty(dynaForm.getString("firstname")));
        user.setSurname(FormUtils.nullIfEmpty(dynaForm.getString("surname")));
        user.setEmailAddress(FormUtils.nullIfEmpty(dynaForm.getString("emailAddress")));
        user.setUsername(FormUtils.nullIfEmpty(dynaForm.getString("username")));

        String password = FormUtils.nullIfEmpty(dynaForm.getString("password"));
        String repeatpassword = FormUtils.nullIfEmpty(dynaForm.getString("repeatpassword"));

        if (password != null && repeatpassword != null) {
            if (password.equals(repeatpassword)) {
                String encpw = KBCrypter.encryptText(FormUtils.nullIfEmpty(dynaForm.getString("password")));
                user.setPassword(encpw);
            } else {
                throw new Exception("New passwords do not match!");
            }
        }

        String mainOrgId = dynaForm.getString("mainOrganization");
        String[] orgSelected = dynaForm.getStrings("orgSelected");
        Set orgSelectedSet = new HashSet();

        for (int i = 0; i < orgSelected.length; i++) {
            orgSelectedSet.add(orgSelected[i]);
        }

        if (!orgSelectedSet.contains(mainOrgId)) {
            // main org is always selected
            orgSelectedSet.add(mainOrgId);
        }

        user.setOrganizations(new HashSet());
        List orgList = em.createQuery("from Organization").getResultList();
        Iterator it = orgSelectedSet.iterator();

        while (it.hasNext()) {
            String orgId = (String) it.next();
            Iterator it2 = orgList.iterator();
            while (it2.hasNext()) {
                Organization org = (Organization) it2.next();
                if (org.getId().toString().equals(orgId)) {
                    user.addOrganization(org);
                    if (org.getId().toString().equals(mainOrgId)) {
                        user.setMainOrganization(org);
                    }
                }
            }
        }

        /* ingevulde rollen bewaren */
        Integer[] rolesSelected = (Integer[])dynaForm.get("roleSelected");

        /* zit op gebruiker form rolesSelected kan geen lengte hebben
             * als je niets aanvinkt */
        if (rolesSelected != null && rolesSelected.length == 0) {
            user.getRoles().clear();
        }

        if (rolesSelected != null && rolesSelected.length > 0) {
            if (rolesSelected[0] != -1) {
                List newRoles = em.createQuery("from Roles where id in (:ids)")
                        .setParameter("ids", Arrays.asList(rolesSelected)).getResultList();

                user.getRoles().retainAll(newRoles);
                user.getRoles().addAll(newRoles);
            }
        }

        String regip = dynaForm.getString("registeredIP");
        Set newset = new HashSet();

        if (regip.length() > 0) {
            String[] registeredIP = regip.split(",");
            int ipsize = registeredIP.length;
            for (int i = 0; i < ipsize; i++) {
                if (registeredIP[i] != null && registeredIP[i].length() > 0) {
                    newset.add(registeredIP[i]);
                }
            }
        }

        user.setIps(compareSets(user.getIps(), newset));

        setPersonalUrlandTimeout(user, dynaForm, request);

    }
    // </editor-fold>

    protected void setPersonalUrlandTimeout(User user, DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {

        Date oldDate = user.getTimeout();
        Date newDate = FormUtils.FormStringToDate(dynaForm.getString("timeout"), request.getLocale());
        if (newDate == null) {
            newDate = getDefaultTimeOut(120);
        }

        boolean urlNeedsRefresh = true;
        String persURL = user.getPersonalURL();
        // check of er uberhaupt een url en timeout zijn
        if (persURL != null
                && persURL.length() > 0
                && oldDate != null) {
            urlNeedsRefresh = false;
            // check of timeout veranderd is
            if (oldDate.before(newDate)) {
                urlNeedsRefresh = true;
            } else {
                if (persURL.startsWith("http")) {
                    urlNeedsRefresh = true;
                }
            }
        }
        if (!urlNeedsRefresh) {
            return;
        }

        user.setPersonalURL(User.createCode(user, newDate, request));
        user.setTimeout(newDate);


    }

    public Set compareSets(
            Set oldset, Set newset) {
        if (oldset == null) {
            oldset = new HashSet();


        }

        Set tempRemoveSet = new HashSet();
        Iterator it = oldset.iterator();


        while (newset != null && it.hasNext()) {
            String userip = (String) it.next();


            if (!newset.contains(userip)) {
                tempRemoveSet.add(userip);


            }

        }

        Iterator removeIt = tempRemoveSet.iterator();


        while (removeIt.hasNext()) {
            String removableIP = (String) removeIt.next();
            oldset.remove(removableIP);


        }

        Iterator newit = newset.iterator();


        while (newit.hasNext()) {
            String userip = (String) newit.next();


            if (!oldset.contains(userip)) {
                oldset.add(userip);


            }

        }
        return oldset;


    }

    public Date getDefaultTimeOut(
            int months) {
        Calendar gc = new GregorianCalendar();
        gc.add(Calendar.MONTH, months);


        return gc.getTime();

    }
}
