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

/**
 *
 * @author Chris
 */
public class UserOrganization {

    private User user;
    private Organization organization;
    private String type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization org) {
        this.organization = org;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object uo) {
        if (!(uo instanceof UserOrganization) || uo == null) {
            return false;
        }
        UserOrganization luo = (UserOrganization) uo;
        if ((luo.organization.getId() == this.organization.getId())
                && (luo.user.getId() == this.user.getId())
                && ((this.type != null && this.type.equals(luo.type))
                || (this.type == null && luo.type == null))) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = this.organization.hashCode() + this.user.hashCode();
        if (this.type!=null) {
            hash += this.type.hashCode();
        }
        return hash;
    }
}
