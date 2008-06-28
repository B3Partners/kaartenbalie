/*
 * ExtLayerCalculator.java
 *
 * Created on December 24, 2007, 1:52 PM
 *
 */
package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.wms.capabilities.Layer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Deze klasse is een extentie van LayerCalculator en voegt als extra mogelijkheid
 * toe dat ook de parent en de childs doorzocht kunnen worden voor het bepalen
 * van de prijzen van een layer.
 * <p>
 * Deze extra mogelijkheden zijn nodig voor WMS layers omdat deze genest kunnen
 * zijn.
 * <p>
 * @author Chris van Lith
 * @see LayerCalculator
 */
public class ExtLayerCalculator extends LayerCalculator {

    private static final Log log = LogFactory.getLog(ExtLayerCalculator.class);

    /**
     * Constructor met eigen Entitymanager
     */
    public ExtLayerCalculator() {
        em = MyEMFDatabase.createEntityManager();
    }

    /**
     * Constructor met entitymanager van buiten
     * @param em
     */
    public ExtLayerCalculator(EntityManager em) {
        this.em = em;
        externalEm = true;
    }

    /**
     * 
     * @param spAbbr
     * @param layerName
     * @return WMS layer object
     */
    protected Layer getWMSLayer(String spAbbr, String layerName) {
        return (Layer) em.createQuery(
                "FROM Layer AS l " +
                "AND l.name = :layerName " +
                "AND l.spAbbr = :serverProviderPrefix").
                setParameter("layerName", layerName).
                setParameter("serverProviderPrefix", spAbbr).
                getSingleResult();
    }

    /**
     * 
     * @param tLC
     * @param spAbbr
     * @param layerName
     * @param validationDate
     * @param projection
     * @param scale
     * @param units
     * @param planType
     * @param service
     * @param operation
     * @return
     * @throws nl.b3p.kaartenbalie.core.server.accounting.LayerNotAvailableException
     * @throws nl.b3p.kaartenbalie.core.server.accounting.NoPrizingException
     */
    @Override
    protected BigDecimal calculateLayer(LayerPriceComposition tLC, String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws LayerNotAvailableException, NoPrizingException {
        BigDecimal layerPrice = null;
        try {
            layerPrice = super.calculateLayer(spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
        } catch (NoPrizingException npe) {
            layerPrice = calculateParentAndChildLayers(tLC, spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
        }
        return layerPrice;
    }

    /**
     * Deze methode wordt aangeroepen indien in de kaartlaag zelf geen prijsinfo
     * is gevonden. Eerst wordt dan in de parant layers gekeken of er prijs info
     * gevonden kan worden. Als dat niet het geval is worden de child layers
     * doorzocht. Als er uiteindelijk geen prijs info gevonden is dan is de 
     * layer gratis.
     * <p>
     * Deze methode wordt alleen aangeroepen voor WMS omdat daar geneste layers
     * mogelijk zijn.
     * <p>
     * @param tLC
     * @param spAbbr
     * @param layerName
     * @param validationDate
     * @param projection
     * @param scale
     * @param units
     * @param planType
     * @param service
     * @param operation
     * @return de prijs van de layer
     * @throws NoPrizingException 
     */
    protected BigDecimal calculateParentAndChildLayers(LayerPriceComposition tLC, String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws NoPrizingException {
        Layer layer = getWMSLayer(spAbbr, layerName);

        BigDecimal layerPrice = null;
        try {
            layerPrice = calculateParentLayer(layer, validationDate, projection, scale, units, planType, service, operation);
            tLC.setMethod(LayerPriceComposition.METHOD_PARENTS);
        } catch (NoPrizingException npe) {
            layerPrice = calculateChildLayers(layer, validationDate, projection, scale, units, planType, service, operation);
            tLC.setMethod(LayerPriceComposition.METHOD_CHILDS);
        }
        return layerPrice;
    }

    /**
     * Deze methode telt 2 prijs BigDecimals op.
     * @param returnValue
     * @param addValue
     * @return nieuwe prijs
     */
    protected BigDecimal addToLayerPrice(
            BigDecimal returnValue, BigDecimal addValue) {
        if (addValue != null) {
            if (returnValue == null) {
                returnValue = new BigDecimal(0);
            }

            returnValue = returnValue.add(addValue);
        }

        return returnValue;
    }

    /**
     * Deze methode probeert de prijs van een layer te bepalen op basis van prijzen
     * van child layers. Als geen van de child layers een prijs heeft dan wordt
     * een NoPrizingException gegooid.
     * <p>
     * Alle child layers worden doorlopen. Als een child layer geen prijs heeft,
     * dan wordt voor dat child recursief onderzocht of childs daarvan een prijs 
     * heeft.
     * <p>
     * @param layer
     * @param validationDate
     * @param projection
     * @param scale
     * @param units
     * @param planType
     * @param service
     * @param operation
     * @return de prijs op basis van child layer pricing.
     * @throws NoPrizingException 
     */
    protected BigDecimal calculateChildLayers(Layer layer, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws NoPrizingException {
        BigDecimal layerPrice = null;
        boolean hasNoPrice = true;

        Set childLayers = layer.getLayers();
        log.debug("Controleer of " + layer.getName() + " childlayers heeft.");
        if (childLayers != null && childLayers.size() > 0) {
            Iterator iterChilds = childLayers.iterator();
            while (iterChilds.hasNext()) {
                Layer childLayer = (Layer) iterChilds.next();
                try {
                    String spAbbr = childLayer.getName();
                    String layerName = childLayer.getSpAbbr();
                    BigDecimal thisLayerPrice = calculateLayer(spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
                    layerPrice =
                            addToLayerPrice(layerPrice, thisLayerPrice);
                    hasNoPrice =
                            false;
                    log.debug("Pricing gevonden in layer: " + childLayer.getName());
                } catch (Exception npe) {
                    //LayerNotAvailableException en NoPrizingException
                    log.debug("Geen pricing gevonden in layer: " + childLayer.getName() + ", oorzaak: ", npe);
                    try {
                        BigDecimal thisLayerPrice = calculateChildLayers(childLayer, validationDate, projection, scale, units, planType, service, operation);
                        layerPrice = addToLayerPrice(layerPrice, thisLayerPrice);
                        hasNoPrice = false;
                        log.debug("Pricing in childlayers gevonden van layer: " + childLayer.getName());
                    } catch (Exception npe2) {
                        //LayerNotAvailableException en NoPrizingException
                        log.debug("Geen pricing in childlayers gevonden van layer: " + childLayer.getName() + ", oorzaak: ", npe2);
                    }

                }

            }
        }
        if (hasNoPrice) {
            log.debug("Geen pricing gevonden in layer: " + layer.getName());
            throw new NoPrizingException();
        }

        return layerPrice;
    }

    /**
     * Deze methode probeert de prijs van een layer te bepalen op basis van een
     * prijs van een parent layer. Alle parent layers worden recursief doorzocht
     * tot een prijs gevonden wordt.
     * <p>
     * @param layer
     * @param validationDate
     * @param projection
     * @param scale
     * @param units
     * @param planType
     * @param service
     * @param operation
     * @return de prijs op basis van een parent prijs
     * @throws NoPrizingException 
     */
    protected BigDecimal calculateParentLayer(Layer layer, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws NoPrizingException {
        log.debug("Controleer of " + layer.getName() + " een parentlayer heeft.");
        BigDecimal layerPrice = null;
        Layer parentLayer = layer.getParent();
        if (parentLayer != null) {
            try {
                String spAbbr = parentLayer.getName();
                String layerName = parentLayer.getSpAbbr();
                layerPrice = calculateLayer(spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
                log.debug("Pricing gevonden voor layer: " + parentLayer.getName());
            } catch (Exception ex) {
                //LayerNotAvailableException en NoPrizingException
                log.debug("Geen pricing gevonden voor layer: " + parentLayer.getName() + " oorzaak: ", ex);
                layerPrice = calculateParentLayer(parentLayer, validationDate, projection, scale, units, planType, service, operation);
            }
        } else {
            log.debug("Geen parent gevonden van layer: " + layer.getName());
            throw new NoPrizingException();
        }

        return layerPrice;

    }
}
