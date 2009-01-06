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
package nl.b3p.kaartenbalie.core.server.monitoring;

/**
 *
 * @author Chris van Lith
 */
public class Operation {

    public static final int ACCOUNTING = 2;
    public static final int CLIENT_TRANSFER = 3;
    public static final int COMBINE_IMAGES = 4;
    public static final int REQUEST = 5;
    public static final int SERVER_TRANSFER = 6;
    public static final String[] NAME = new String[]{
        "UNKNOWN",
        "ACCOUNTING",
        "CLIENT_TRANSFER",
        "COMBINE_IMAGES",
        "REQUEST",
        "SERVER_TRANSFER"
    };
    private Integer id;
    private Long msSinceRequestStart;
    private Long duration;
    private ClientRequest clientRequest;
    private int type;
    private Integer numberOfImages;
    private Integer bytesReceivedFromUser;
    private Integer bytesSendToUser;
    private Long dataSize;

    public Operation() {
    }

    public Operation(ClientRequest clientRequest) {
        this();
        this.setClientRequest(clientRequest);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ClientRequest getClientRequest() {
        return clientRequest;
    }

    public void setClientRequest(ClientRequest clientRequest) {
        this.clientRequest = clientRequest;
    }

    public Long getMsSinceRequestStart() {
        return msSinceRequestStart;
    }

    public void setMsSinceRequestStart(Long msSinceRequestStart) {
        this.msSinceRequestStart = msSinceRequestStart;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the numberOfImages
     */
    public Integer getNumberOfImages() {
        return numberOfImages;
    }

    /**
     * @param numberOfImages the numberOfImages to set
     */
    public void setNumberOfImages(Integer numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    /**
     * @return the bytesReceivedFromUser
     */
    public Integer getBytesReceivedFromUser() {
        return bytesReceivedFromUser;
    }

    /**
     * @param bytesReceivedFromUser the bytesReceivedFromUser to set
     */
    public void setBytesReceivedFromUser(Integer bytesReceivedFromUser) {
        this.bytesReceivedFromUser = bytesReceivedFromUser;
    }

    /**
     * @return the bytesSendToUser
     */
    public Integer getBytesSendToUser() {
        return bytesSendToUser;
    }

    /**
     * @param bytesSendToUser the bytesSendToUser to set
     */
    public void setBytesSendToUser(Integer bytesSendToUser) {
        this.bytesSendToUser = bytesSendToUser;
    }

    /**
     * @return the dataSize
     */
    public Long getDataSize() {
        return dataSize;
    }

    /**
     * @param dataSize the dataSize to set
     */
    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }
}
