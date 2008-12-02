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
package nl.b3p.kaartenbalie.core.server.accounting.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class TransactionLayerUsage extends Transaction {

    private static final Log log = LogFactory.getLog(TransactionLayerUsage.class);
    private Set layerPriceCompositions;
    private Set pricedLayerNames;

    /** Creates a new instance of TransactionLayerUsage */
    public TransactionLayerUsage() {
        super();
        layerPriceCompositions = new HashSet();
        pricedLayerNames = new HashSet();
        this.setType(WITHDRAW);
    }

    public void validate() throws Exception {
        if (this.getType() != WITHDRAW) {
            log.error("Only WITHDRAW is allowed for this type of transaction.");
            throw new Exception("Only WITHDRAW is allowed for this type of transaction.");
        }
    }

    public Set getLayerPriceCompositions() {
        return layerPriceCompositions;
    }

    public void setLayerPriceCompositions(Set layerPriceCompositions) {
        this.layerPriceCompositions = layerPriceCompositions;
    }

    public void registerUsage(LayerPriceComposition lpc) throws Exception {
        if (lpc == null) {
            log.error("Not allowed to add a null value to registerUsage.");
            throw new Exception("Not allowed to add a null value to registerUsage.");
        }
        if (lpc.getLayerPrice() == null || lpc.getLayerPrice().compareTo(new BigDecimal("0")) < 0) {
            log.error("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
            throw new Exception("Invalid value for lpc.layerPrice: " + lpc.getLayerPrice());
        }
        if (lpc.getLayerIsFree() == null || (lpc.getLayerIsFree() != null && !lpc.getLayerIsFree().booleanValue())) {
            creditAlteration = creditAlteration.add(lpc.getLayerPrice());
            pricedLayerNames.add(lpc.getLayerName());
        }
        lpc.setTransactionLayerUsage(this);
        layerPriceCompositions.add(lpc);
    }

    public String getPricedLayerNames() {
        if (pricedLayerNames == null || pricedLayerNames.isEmpty()) {
            return null;
        }
        StringBuffer pln = new StringBuffer();
        Iterator it = pricedLayerNames.iterator();
        while (it.hasNext()) {
            if (pln.length() > 0) {
                pln.append(", ");
            }
            pln.append((String) it.next());
        }
        return pln.toString();
    }
}
