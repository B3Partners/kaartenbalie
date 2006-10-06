package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

public class PutStyles {
    
    private ArrayList dcpTypes = new ArrayList();
    
    public void addDCPType(DCPType dcpType) {
        dcpTypes.add(dcpType);
    }

    public ArrayList getDcpTypes() {
        return dcpTypes;
    }

    public void setDcpTypes(ArrayList dcpTypes) {
        this.dcpTypes = dcpTypes;
    }
    

    
}