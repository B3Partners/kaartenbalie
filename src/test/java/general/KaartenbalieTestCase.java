/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2012,2013,2014 B3Partners BV
 * 
 * author : Rachelle Scheijen
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

package general;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.servletTestAPI.ConfigStub;
import nl.b3p.testStreet.B3TestCase;

public class KaartenbalieTestCase extends B3TestCase {

    protected MyEMFDatabase DB;
    protected EntityManager entityManager;

    public KaartenbalieTestCase(String name) {
        super(name);
    }

    /**
     * Initializes the class KaartenbalieTestCase 
     * 
     * @throws Exception 
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        try {
            ConfigStub config = new ConfigStub();
            DB = new MyEMFDatabase();
            DB.init(config);
            MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            this.entityManager = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        } catch (ServletException e) {
            System.out.println("Error initializing : " + e.getLocalizedMessage());
            throw (e);
        }
    }

    /**
     * Generates a user with administrator rights
     * 
     * @return The generated user
     */
    protected nl.b3p.kaartenbalie.core.server.User generateUser() {
        UserOverwrite user = new UserOverwrite();
        
        user.setIdTest(new Integer(1));
        user.setUsername("beheerder");
        user.setPassword("dsfdes23423");
        user.setFirstName("Beheerder");
        user.setSurname("Beheerders");
        user.setEmailAddress("beheerder@b3partners.nl");

        return user;
    }
    
    /**
     * Generates a controllers organization
     * 
     * @return The generated organization
     */
    protected Organization generateOrganization(){
        OrganizationOverwrite organization   = new OrganizationOverwrite();
        organization.setTestId(new Integer(1));
        organization.setName("Beheerders");
        
        return organization;
    }

    /**
     * Checks if the exception contains the message "Layer is not mapped"
     * Fails the calling test if the message is not present
     * 
     * @param e     The throws exception
     */
    protected void checkNotMapped(Exception e) {
        String error = e.getLocalizedMessage();
        if ( !error.contains("Layer is not mapped") ) {
            fail("Exception " + error);
            assertTrue(false);
        }
    }
}
