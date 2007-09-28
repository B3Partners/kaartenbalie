/*
 * @(#)IPAddresses.java
 * @author N. de Goeij
 * @version 1.00, 25 september 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.core.server;

public class IPAddresses {
    private Integer id;
    private String ipaddress;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }
    
    public boolean compare(IPAddresses ipaddress) {
        return this.getIpaddress().equals(ipaddress.getIpaddress());
    }
}