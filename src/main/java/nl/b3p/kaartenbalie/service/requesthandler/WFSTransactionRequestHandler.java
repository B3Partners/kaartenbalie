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
package nl.b3p.kaartenbalie.service.requesthandler;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.ogc.utils.LayerSummary;
import nl.b3p.ogc.utils.OGCCommunication;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;
import nl.b3p.ogc.utils.OGCResponse;
import nl.b3p.ogc.utils.SpLayerSummary;
import nl.b3p.ogc.utils.WFSTransactionResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

public class WFSTransactionRequestHandler extends WFSRequestHandler {

    private static final Log log = LogFactory.getLog(WFSGetFeatureRequestHandler.class);

    /** Creates a new instance of WfsTransactionRequestHandler */
    public WFSTransactionRequestHandler() {
    }
    
    /**
     * layer names moeten nog vervangen worden voor het naar de service provider word gestuurt
     * @return 
     */
    public List replaceLayerNames(List elementList, String spAbbr) throws Exception {
        List newElementList = new ArrayList();
        Iterator tor = elementList.iterator();
        while (tor.hasNext()) {
            Object o = tor.next();
            if (o instanceof nl.b3p.xml.wfs.v110.Delete) {
                nl.b3p.xml.wfs.v110.Delete delete = (nl.b3p.xml.wfs.v110.Delete) o;

                LayerSummary m = OGCCommunication.splitLayerWithoutNsFix(delete.getTypeName(), (spAbbr!=null), null, "app");
                delete.setTypeName(OGCCommunication.buildFullLayerName(m));

                String oldId = delete.getFilter().getGmlObjectId().getId().toString();
                delete.getFilter().getGmlObjectId().setId(OGCCommunication.replaceIds(oldId, spAbbr, null));

                newElementList.add(delete);
            } else if (o instanceof nl.b3p.xml.wfs.v110.Update) {
                nl.b3p.xml.wfs.v110.Update update = (nl.b3p.xml.wfs.v110.Update) o;

                LayerSummary m = OGCCommunication.splitLayerWithoutNsFix(update.getTypeName(), (spAbbr!=null), null, "app");
                update.setTypeName(OGCCommunication.buildFullLayerName(m));

                String oldId = update.getFilter().getGmlObjectId().getId().toString();
                update.getFilter().getGmlObjectId().setId(OGCCommunication.replaceIds(oldId, spAbbr, null));

                newElementList.add(update);
            } else if (o instanceof nl.b3p.xml.wfs.v110.Insert) {
                nl.b3p.xml.wfs.v110.Insert insert = (nl.b3p.xml.wfs.v110.Insert) o;
                StringWriter sw = new StringWriter();
                Marshaller m = new Marshaller(sw);
                m.marshal(insert);
                String makeInsert = OGCCommunication.replaceIds(sw.toString(), spAbbr, "app");
                nl.b3p.xml.wfs.v110.Insert newInsert = (nl.b3p.xml.wfs.v110.Insert) Unmarshaller.unmarshal(nl.b3p.xml.wfs.v110.Insert.class, new StringReader(makeInsert));
                newElementList.add(newInsert);
            }
        }
        return newElementList;
    }
    
    public String prepareRequest4Sp(OGCRequest ogcrequest, SpLayerSummary sp) throws Exception{

        // Is accounting nodig voor transaction request's?
        /*spUrls = prepareAccounting(orgId, data, spUrls);
         if(spUrls==null || spUrls.isEmpty()) {
         log.error("No urls qualify for request.");
         throw new UnsupportedOperationException("No Serviceprovider available! User might not have rights to any Serviceprovider!");
         }*/
        
        String prefix = sp.getSpAbbr();
        // layer names moeten nog vervangen worden voor het naar de service provider word gestuurt
        List elementList = ogcrequest.getTransactionElementList(prefix);
        List newElementList = replaceLayerNames(elementList, prefix);
        ogcrequest.setTransactionElementList(newElementList, prefix);
        ogcrequest.setAbbr(prefix);

         return null;
    }
    
    public List<LayerSummary> prepareRequestLayers(OGCRequest ogcrequest) throws Exception {
        
        if (ogcrequest.getParameter(OGCConstants.WFS_PARAM_OPERATION) != null) {
                throw new UnsupportedOperationException("Transaction request with Key value Pairs is not suported yet!");
        }
        
        List<String> allLayers = null;
        if (ogcrequest.getHttpMethod().equals("POST")) {
            Set layers = ogcrequest.getGetFeatureFilterMap().keySet();
            allLayers.addAll(layers);
        } else {
            String typeName = ogcrequest.getParameter(OGCConstants.WFS_PARAM_TYPENAME);
            allLayers = Arrays.asList(typeName.split(","));
        }

        String spInUrl = ogcrequest.getServiceProviderName();
        List<LayerSummary> lsl = LayerSummary.createLayerSummaryList(allLayers,
                ogcrequest.getServiceProviderName(), (spInUrl==null));
        
        if (!checkNumberOfSps(lsl, 1)) {
            log.error("More then 1 service provider addressed. Not supported (yet)");
            throw new UnsupportedOperationException("More then 1 service provider addressed. Not supported (yet)");
        }
        return lsl;
    }

    public OGCResponse getNewOGCResponse() {
        return new WFSTransactionResponse();
    }

    public void getRequest(DataWrapper data, User user) throws IOException, Exception {
        writeResponse(data, user);
    }

}
