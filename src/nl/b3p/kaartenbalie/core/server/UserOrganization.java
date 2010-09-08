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

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public class UserOrganization {
    private UserOrganizationId key;

    private String type;

    public UserOrganization() {
    }

    public UserOrganization(User user, Organization organization, String type) {
        this.type = type;
        this.setKey(new UserOrganizationId(organization, user));
    }

    public UserOrganizationId getKey() {
        return key;
    }

    public void setKey(UserOrganizationId key) {
        this.key = key;
    }


    public User getUser() {
        return getKey().getUser();
    }

    public Organization getOrganization() {
        return getKey().getOrganization();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
