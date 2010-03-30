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
package nl.b3p.kaartenbalie.service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A connection manager that provides access to a single HttpConnection
 * that will use a proxy.  This
 * manager makes no attempt to provide exclusive access to the contained
 * HttpConnection.
 *
 * @author Marco Nijdam
 */
public class AutoProxyHttpConnectionManager extends SimpleHttpConnectionManager {

	private final Log log = LogFactory.getLog(this.getClass());
	
    /**
     * This method always returns the same connection object. If the connection is already
     * open, it will be closed and the new host configuration will be applied.
     *
     * @param hostConfiguration The host configuration specifying the connection
     *        details.
     * @param timeout this parameter has no effect. The connection is always returned
     *        immediately.
     * @since 3.0
     */
    public HttpConnection getConnectionWithTimeout(
        HostConfiguration hostConfiguration, long timeout) {

        // configure proxies for URI
        ProxySelector selector = ProxySelector.getDefault();

		try {
	        List<Proxy> proxyList = selector.select(new URI(hostConfiguration.getHostURL()));
	        Proxy proxy = proxyList.get(0);

	        if (!proxy.equals(Proxy.NO_PROXY)) {
	            InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
	            String hostName = socketAddress.getHostName();
	            int port = socketAddress.getPort();
	            hostConfiguration.setProxy(hostName,port);
	            if (log.isDebugEnabled()) {
	            	log.debug("proxy set to: " + hostName + ":" + port + " for " + hostConfiguration.getHostURL());
	            }
	        } else {
	            hostConfiguration.setProxyHost(null);
	            if (log.isDebugEnabled()) {
	            	log.debug("no proxy for: " + hostConfiguration.getHostURL());
	            }
	        }
		} catch (URISyntaxException excp) {
			log.error("Invalid URI: " + hostConfiguration.getHostURL(), excp);
		}
        return super.getConnectionWithTimeout(hostConfiguration, timeout);
    }

}
