/*
 * CombineImagesOperation.java
 *
 * Created on October 16, 2007, 9:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.reporting.domain.operations;

import nl.b3p.kaartenbalie.core.server.reporting.domain.requests.ClientRequest;

/**
 *
 * @author Chris Kramer
 */
public class ClientXFerOperation extends Operation{
    
    private Long dataSize;
    
    public ClientXFerOperation() {
        super();
    }
    public ClientXFerOperation(ClientRequest clientRequest) {
        super(clientRequest);
    }

    public Long getDataSize() {
        return dataSize;
    }

    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }
    
  
    

    
    
}
