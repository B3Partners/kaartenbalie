package general;

import nl.b3p.kaartenbalie.core.server.Organization;

/**
 *
 * @author rachelle
 */
public class OrganizationOverwrite extends Organization {
    public void setTestId(Integer id){
        super.setId(id);
    }
}
