/*
 * CombineImagesOperation.java
 *
 * Created on October 16, 2007, 9:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain;

/**
 *
 * @author Chris Kramer
 */
public class CombineImagesOperation extends RequestOperation{
    
    private Integer numberOfImages;
    public CombineImagesOperation() {
        super();
    }
    public CombineImagesOperation(ClientRequest clientRequest) {
        super(clientRequest);
    }
    
    
    public Integer getNumberOfImages() {
        return numberOfImages;
    }
    
    public void setNumberOfImages(Integer numberOfImages) {
        this.numberOfImages = numberOfImages;
    }
    

    
    
}
