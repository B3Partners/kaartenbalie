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
package nl.b3p.kaartenbalie.core.server.reporting.domain.requests;

/**
 *
 * @author Chris Kramer
 */
public abstract class ServiceProviderRequest {

    private Integer id;    //Data fields
    private Long bytesSend;
    private Long bytesReceived;
    private Integer responseStatus;
    private Long requestResponseTime;
    private String providerRequestURI;
    private Long msSinceRequestStart;
    private Class exceptionClass;
    private String exceptionMessage;    //Relational Mappings
    private ClientRequest clientRequest;

    public ServiceProviderRequest() {
    }

    public ServiceProviderRequest(ClientRequest clientRequest) {
        this();
        this.setClientRequest(clientRequest);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getProviderRequestURI() {
        return providerRequestURI;
    }

    public void setProviderRequestURI(String providerRequestURI) {
        this.providerRequestURI = providerRequestURI;
    }

    public Long getRequestResponseTime() {
        return requestResponseTime;
    }

    public void setRequestResponseTime(Long requestResponseTime) {
        this.requestResponseTime = requestResponseTime;
    }

    public Long getBytesSend() {
        return bytesSend;
    }

    public void setBytesSend(Long bytesSend) {
        this.bytesSend = bytesSend;
    }

    public Long getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(Long bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public Long getMsSinceRequestStart() {
        return msSinceRequestStart;
    }

    public void setMsSinceRequestStart(Long msSinceRequestStart) {
        this.msSinceRequestStart = msSinceRequestStart;
    }

    public Class getExceptionClass() {
        return exceptionClass;
    }

    public void setExceptionClass(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
