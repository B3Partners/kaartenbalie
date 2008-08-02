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
package nl.b3p.kaartenbalie.core.server.persistence;

import java.sql.Types;
import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 *
 * @author Chris Kramer
 */
public class CustomDBDialect extends MySQL5InnoDBDialect {

    public CustomDBDialect() {
        /*
         * This fixes a problem when mapping Decimal types from ie BigDecimal. Somehow
         * mySQL and hibernate messes up with the validation of these types..
         */
        registerColumnType(Types.NUMERIC, "decimal($p,$s)");
    }

    /*
     * There have been some issues with the table creation. This was the only sollution that actually worked. Please note that
     * when switching database types this code should be reviewed again.
     */
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";

    }
}
