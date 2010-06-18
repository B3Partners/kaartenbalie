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
package nl.b3p.kaartenbalie.core.server;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import nl.b3p.kaartenbalie.service.servlet.CallWMSServlet;
import nl.b3p.wms.capabilities.Roles;

public class User implements Principal {

    private Integer id;
    private String firstName;
    private String surname;
    private String emailAddress;
    private String username;
    private String password;
    private String personalURL;
    private String defaultGetMap;
    private Date timeout;
    private Set userOrganizations;
    private Set roles;
    private Set ips;
    // <editor-fold defaultstate="" desc="getter and setter methods.">

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
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

    public Set getUserOrganizations() {
        return userOrganizations;
    }

    public void setUserOrganizations(Set userOrganizations) {
        this.userOrganizations = userOrganizations;
    }

    public void addUserOrganization(UserOrganization uo) {
        if (userOrganizations == null) {
            userOrganizations = new HashSet();
        }
        userOrganizations.add(uo);
    }

    public String getOrganisationCodes() {
        Set uorgs = this.getUserOrganizations();
        if (uorgs == null || uorgs.size() == 0) {
            return null;
        }
        StringBuffer codes = new StringBuffer();
        Iterator it = uorgs.iterator();
        while (it.hasNext()) {
            if (codes.length() != 0) {
                codes.append(",");
            }
            UserOrganization uorg = (UserOrganization) it.next();
            Organization org = uorg.getOrganization();
            codes.append(org.getCode());
        }
        return codes.toString();
    }

    public Integer[] getOrganizationIds() {
        return getOrganizationIds(null);
    }

    public Integer getMainOrganizationId() {
        Integer[] oids = getOrganizationIds("main");
        if (oids.length > 0) {
            // should never be more than one
            return oids[0];
        }
        return null;
    }

    private Integer[] getOrganizationIds(String type) {
        Set uorgs = this.getUserOrganizations();
        if (uorgs == null || uorgs.size() == 0) {
            return null;
        }
        Organization fallbackOrg = null;
        ArrayList oidList = new ArrayList();
        Iterator it = uorgs.iterator();
        while (it.hasNext()) {
            UserOrganization uorg = (UserOrganization) it.next();
            Organization org = uorg.getOrganization();
            fallbackOrg = org;
            if (type == null || (type != null && type.equals(uorg.getType()))) {
                oidList.add(org.getId());
            }
        }
        if (oidList.size() == 0 && fallbackOrg != null) {
            oidList.add(fallbackOrg);
        }
        Integer[] orgIds = new Integer[oidList.size()];
        orgIds = (Integer[]) oidList.toArray(orgIds);
        return orgIds;
    }

    public Organization getMainOrganization() {
        Set orgs = getOrganizations("main");
        if (orgs != null && orgs.size() > 0) {
            // should never be more than one
            return (Organization) orgs.toArray(new Organization[orgs.size()])[0];
        }
        return null;
    }

    public Set getOrganizations() {
        return getOrganizations(null);
    }

    private Set getOrganizations(String type) {
        Set uorgs = this.getUserOrganizations();
        if (uorgs == null || uorgs.size() == 0) {
            return null;
        }
        Organization fallbackOrg = null;
        Set oidSet = new HashSet();
        Iterator it = uorgs.iterator();
        while (it.hasNext()) {
            UserOrganization uorg = (UserOrganization) it.next();
            Organization org = uorg.getOrganization();
            fallbackOrg = org;
            if (type == null || (type != null && type.equals(uorg.getType()))) {
                oidSet.add(org);
            }
        }
        if (oidSet.size() == 0 && fallbackOrg != null) {
            oidSet.add(fallbackOrg);
        }
        return oidSet;
    }

    public Set getLayers() {
        return getLayers(null);
    }

    private Set getLayers(String type) {
        Set uorgs = this.getUserOrganizations();
        if (uorgs == null || uorgs.size() == 0) {
            return null;
        }
        Set layerSet = new HashSet();
        Iterator it = uorgs.iterator();
        while (it.hasNext()) {
            UserOrganization uorg = (UserOrganization) it.next();
            if (type == null || (type != null && type.equals(uorg.getType()))) {
                Organization org = uorg.getOrganization();
                layerSet.addAll(org.getLayers());
            }
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

    /**
     * creates full personal url from code and request info.
     * for backwards compatibility first checks if full url
     * is already stored in database.
     *
     * @param request
     * @return
     */
    public String getPersonalURL(HttpServletRequest request) {
        if (personalURL.startsWith("http")) {
            return personalURL;
        }
        StringBuffer baseUrl = CallWMSServlet.createBaseUrl(request);
        baseUrl.append("/services/");
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
}
