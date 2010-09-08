package nl.b3p.kaartenbalie.core.server;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserOrganizationId)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        UserOrganizationId rhs = (UserOrganizationId) obj;
        return new EqualsBuilder()
                .append(user, rhs.user)
                .append(organization, rhs.organization)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(57, 21)
                .append(user)
                .append(organization)
                .toHashCode();
    }
}
