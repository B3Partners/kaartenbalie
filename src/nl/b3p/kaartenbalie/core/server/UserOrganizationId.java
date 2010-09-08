package nl.b3p.kaartenbalie.core.server;

import java.io.Serializable;

public class UserOrganizationId implements Serializable {

    private User user;
    private Organization organization;

    public UserOrganizationId() {
    }

    public UserOrganizationId(Organization organization, User user) {
        this.organization = organization;
        this.user = user;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
