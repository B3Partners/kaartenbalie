/*
 * CustomDBDialect.java
 *
 * Created on October 8, 2007, 11:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.persistence;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 *
 * @author Chris Kramer
 */
public class CustomDBDialect extends MySQL5InnoDBDialect {
    
    /** Creates a new instance of CustomDBDialect */
    public CustomDBDialect() {
}
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }


    
}
