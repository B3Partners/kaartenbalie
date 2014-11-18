/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006-2014 B3Partners BV
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
package nl.b3p.kaartenbalie.core.server;

import java.security.MessageDigest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.ogc.utils.KBConfiguration;
import nl.b3p.wms.capabilities.Roles;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class User implements Principal {

    private static final Log log = LogFactory.getLog(User.class);    

    private Integer id;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String username;
    private String password;
    private String personalURL;
    private String defaultGetMap;
    private Date timeout;
    private Organization mainOrganization;
    private Set organizations = new HashSet();
    private Set roles = new HashSet();
    private Set ips = new HashSet();
    private String lastLoginStatus;
    
    public static final String LOGIN_STATE_INVALID_IP = "Ongeldig IP";
    public static final String LOGIN_STATE_EXPIRED = "Account verlopen";
    public static final String LOGIN_STATE_WRONG_PASSW = "Verkeerd wachtwoord";
 
    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set getOrganizations() {
        return organizations;
    }

    public Set getAllOrganizations() {
        Set orgs = new HashSet();
        Set lorgs = getOrganizations();
        if (lorgs!=null && !lorgs.isEmpty()) {
            orgs.addAll(lorgs);
        }
        Organization mainOrg = getMainOrganization();
        if (mainOrg!=null && !orgs.contains(mainOrg)) {
            orgs.add(mainOrg);
        }
        return orgs;
    }

    public void setOrganizations(Set organizations) {
        this.organizations = organizations;
    }

    public void addOrganization(Organization o) {
        organizations.add(o);
    }

    public String getOrganisationCodes() {
        Set orgs = getAllOrganizations();
        StringBuilder codes = new StringBuilder();
        Iterator it = orgs.iterator();

        while (it.hasNext()) {
            if (codes.length() > 0) {
                codes.append(",");
            }

            Organization org = (Organization) it.next();
            
            if (org.getCode() != null) {
                codes.append(org.getCode());
            }
        }

        return codes.toString();
    }

    public Integer getMainOrganizationId() {
        return getMainOrganization().getId();
    }

    public Integer[] getOrganizationIds() {
        Set orgs = getAllOrganizations();
        ArrayList oidList = new ArrayList();
        Iterator it = orgs.iterator();
        while (it.hasNext()) {
            Organization org = (Organization) it.next();
            oidList.add(org.getId());
         }
        Integer[] orgIds = new Integer[oidList.size()];
        orgIds = (Integer[]) oidList.toArray(orgIds);
        return orgIds;
    }

    public void setMainOrganization(Organization mainOrganization) {
        this.mainOrganization = mainOrganization;
    }

    public Organization getMainOrganization() {
        return mainOrganization;
    }

    public Set getLayers() {
        Set orgs = getAllOrganizations();
        Set layerSet = new HashSet();
        Iterator it = orgs.iterator();
        while (it.hasNext()) {
            Organization org = (Organization) it.next();
            layerSet.addAll(org.getLayers());
        }
        if (layerSet.size() == 0) {
            return null;
        }
        return layerSet;
    }


    /* impl principal*/
    public String getName() {
        return username;
    }
    
    
    public String getPersonalURL(HttpServletRequest request){
        return getPersonalURL(request, null);
    }

    /**
     * creates full personal url from code and request info.
     * for backwards compatibility first checks if full url
     * is already stored in database.
     *
     * @param request
     * @return
     */
    public String getPersonalURL(HttpServletRequest request, String serviceProviderCode) {

        if (personalURL == null) {
            return null;
        }

        if (personalURL.startsWith("http")) {
            return personalURL;
        }

        StringBuffer baseUrl = CallWMSServlet.createBaseUrl(request);
        baseUrl.append("/services/");
        if(serviceProviderCode != null && !serviceProviderCode.equals("")){
            baseUrl.append(serviceProviderCode+"/");
        }
        baseUrl.append(personalURL);

        return baseUrl.toString();
    }

    public String getPersonalURL() {
        return personalURL;
    }

    public void setPersonalURL(String personalURL) {
        this.personalURL = personalURL;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    public String getDefaultGetMap() {
        return defaultGetMap;
    }

    public void setDefaultGetMap(String s) {
        defaultGetMap = s;
    }
    // </editor-fold>

    public Set getRoles() {
        return roles;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public void addRole(Roles role) {
        if (roles == null) {
            roles = new HashSet();
        }
        roles.add(role);
    }

    public void deleteRole(Roles role) {
        if (roles != null) {
            roles.remove(role);
        }
    }

    public boolean checkRole(String role) {
        if (roles == null) {
            return false;
        }
        Iterator it = roles.iterator();
        while (it.hasNext()) {
            Roles theUserroles = (Roles) it.next();
            if (role.equalsIgnoreCase(theUserroles.getRole())) {
                return true;
            }
        }
        return false;
    }

    public String getRolesAsString() {
        String rolesStr = "";
        Iterator it = this.roles.iterator();
        while (it.hasNext()) {
            Roles theUserroles = (Roles) it.next();
            rolesStr = rolesStr + theUserroles.getRole() + " ";
        }
        return rolesStr;
    }

    public Set getIps() {
        return ips;
    }

    public void setIps(Set ips) {
        this.ips = ips;
    }

    public void addIps(String ip) {
        if (ips == null) {
            ips = new HashSet();
        }
        ips.add(ip);
    }

    public String getLastLoginStatus() {
        return lastLoginStatus;
    }

    public void setLastLoginStatus(String lastLoginStatus) {
        this.lastLoginStatus = lastLoginStatus;
    }
    
    Object o = null;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User) || o == null) {
            return false;
        }
        User ouser = (User) o;
        if (this.username == null) {
            return false;
        }
        return (this.username.equals(ouser.getUsername()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.username != null ? this.username.hashCode() : 0);
        return hash;
    }

    public static boolean isValidCode(String code) {
        if (code==null || code.length()!=32) {
            return false;
        }
        try {
            Hex.decodeHex(code.toCharArray());
        } catch (DecoderException ex) {
            return false;
        }
        return true;
    }

    public static String createCode() {
        Random rd = new Random();
        StringBuilder toBeHashedString = new StringBuilder();
        toBeHashedString.append((new Date()).toString());
        toBeHashedString.append(rd.nextLong());

        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance(KBConfiguration.MD_ALGORITHM);
            md.update(toBeHashedString.toString().getBytes(KBConfiguration.CHARSET));
            hash = md.digest();
        } catch (Exception ex) {
            log.info("Can not create hash (ignoring): " + ex.getLocalizedMessage());
            hash = toBeHashedString.toString().getBytes();
        }
 
        return new String(Hex.encodeHex(hash));
    }
}
