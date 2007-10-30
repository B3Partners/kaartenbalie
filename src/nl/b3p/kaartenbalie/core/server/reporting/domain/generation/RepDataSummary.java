/*
 * SummarySet.java
 *
 * Created on October 26, 2007, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.generation;

import nl.b3p.kaartenbalie.core.server.reporting.control.RequestReporting;
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
    
    public RepDataSummary(Report report) {
        super(report);
    }
    public Element toElement(Document doc, Element rootElement) {
        Element summary = doc.createElement("summary");
        
        if (ctoHits != null && ctoAverageResponse != null) {
            Element cto = doc.createElement("ServerTransferOperation");
            cto.setAttribute("hits", ctoHits.toString());
            cto.appendChild(RequestReporting.createElement(doc,"averageDuration", ctoAverageResponse.toString()));
            summary.appendChild(cto);
        }
        
        if (getCioHits() != null && getCioAverageResponse() != null) {
            Element cio = doc.createElement("CombineImagesOperation");
            cio.setAttribute("hits", getCioHits().toString());
            cio.appendChild(RequestReporting.createElement(doc,"averageDuration", getCioAverageResponse().toString()));
            summary.appendChild(cio);
        }
        
        if (getCxoHits() != null && getCxoAverageResponse() != null) {
            Element cxo = doc.createElement("ClientXFerOperation");
            cxo.setAttribute("hits", getCxoHits().toString());
            cxo.appendChild(RequestReporting.createElement(doc,"averageDuration", getCxoAverageResponse().toString()));
            cxo.appendChild(RequestReporting.createElement(doc,"dataSize", getCxoData().toString()));
            summary.appendChild(cxo);
        }
        if (getRoHits() != null && getRoAverageResponse() != null) {
            Element ro = doc.createElement("RequestOperation");
            ro.setAttribute("hits", getRoHits().toString());
            ro.appendChild(RequestReporting.createElement(doc,"averageDuration", getRoAverageResponse().toString()));
            ro.appendChild(RequestReporting.createElement(doc,"bytesReceivedFromUser", getRoUpload().toString()));
            ro.appendChild(RequestReporting.createElement(doc,"bytesSendToUser", getRoDownload().toString()));
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
