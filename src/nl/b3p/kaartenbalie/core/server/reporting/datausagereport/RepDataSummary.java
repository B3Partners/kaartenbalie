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
package nl.b3p.kaartenbalie.core.server.reporting.datausagereport;

import nl.b3p.kaartenbalie.core.server.monitoring.DataMonitoring;
import nl.b3p.wms.capabilities.XMLElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Chris Kramer
 */
public class RepDataSummary extends RepData implements XMLElement {

    /** Creates a new instance of SummarySet */
    private Long ctoHits;
    private Double ctoAverageResponse;
    private Long cioHits;
    private Double cioAverageResponse;
    private Long cxoHits;
    private Long cxoData;
    private Double cxoAverageResponse;
    private Long roHits;
    private Long roUpload;
    private Long roDownload;
    private Double roAverageResponse;

    private RepDataSummary() {
    }

    public RepDataSummary(DataUsageReport report) {
        super(report);
    }

    public Element toElement(Document doc, Element rootElement) {
        Element summary = doc.createElement("summary");

        if (ctoHits != null && ctoAverageResponse != null) {
            Element cto = doc.createElement("ServerTransferOperation");
            cto.setAttribute("hits", ctoHits.toString());
            cto.appendChild(DataMonitoring.createElement(doc, "averageDuration", ctoAverageResponse.toString()));
            summary.appendChild(cto);
        }

        if (getCioHits() != null && getCioAverageResponse() != null) {
            Element cio = doc.createElement("CombineImagesOperation");
            cio.setAttribute("hits", getCioHits().toString());
            cio.appendChild(DataMonitoring.createElement(doc, "averageDuration", getCioAverageResponse().toString()));
            summary.appendChild(cio);
        }

        if (getCxoHits() != null && getCxoAverageResponse() != null) {
            Element cxo = doc.createElement("ClientXFerOperation");
            cxo.setAttribute("hits", getCxoHits().toString());
            cxo.appendChild(DataMonitoring.createElement(doc, "averageDuration", getCxoAverageResponse().toString()));
            cxo.appendChild(DataMonitoring.createElement(doc, "dataSize", getCxoData().toString()));
            summary.appendChild(cxo);
        }
        if (getRoHits() != null && getRoAverageResponse() != null) {
            Element ro = doc.createElement("RequestOperation");
            ro.setAttribute("hits", getRoHits().toString());
            ro.appendChild(DataMonitoring.createElement(doc, "averageDuration", getRoAverageResponse().toString()));
            ro.appendChild(DataMonitoring.createElement(doc, "bytesReceivedFromUser", getRoUpload().toString()));
            ro.appendChild(DataMonitoring.createElement(doc, "bytesSendToUser", getRoDownload().toString()));
            summary.appendChild(ro);
        }

        return summary;
    }

    public Long getCioHits() {
        return cioHits;
    }

    public void setCioHits(Long cioHits) {
        this.cioHits = cioHits;
    }

    public Long getCxoHits() {
        return cxoHits;
    }

    public void setCxoHits(Long cxoHits) {
        this.cxoHits = cxoHits;
    }

    public Long getCxoData() {
        return cxoData;
    }

    public void setCxoData(Long cxoData) {
        this.cxoData = cxoData;
    }

    public Long getRoHits() {
        return roHits;
    }

    public void setRoHits(Long roHits) {
        this.roHits = roHits;
    }

    public Long getRoUpload() {
        return roUpload;
    }

    public void setRoUpload(Long roUpload) {
        this.roUpload = roUpload;
    }

    public Long getRoDownload() {
        return roDownload;
    }

    public void setRoDownload(Long roDownload) {
        this.roDownload = roDownload;
    }

    public Double getCioAverageResponse() {
        return cioAverageResponse;
    }

    public void setCioAverageResponse(Double cioAverageResponse) {
        this.cioAverageResponse = cioAverageResponse;
    }

    public Double getCxoAverageResponse() {
        return cxoAverageResponse;
    }

    public void setCxoAverageResponse(Double cxoAverageResponse) {
        this.cxoAverageResponse = cxoAverageResponse;
    }

    public Double getRoAverageResponse() {
        return roAverageResponse;
    }

    public void setRoAverageResponse(Double roAverageResponse) {
        this.roAverageResponse = roAverageResponse;
    }

    public Long getCtoHits() {
        return ctoHits;
    }

    public void setCtoHits(Long ctoHits) {
        this.ctoHits = ctoHits;
    }

    public Double getCtoAverageResponse() {
        return ctoAverageResponse;
    }

    public void setCtoAverageResponse(Double ctoAverageResponse) {
        this.ctoAverageResponse = ctoAverageResponse;
    }
}
