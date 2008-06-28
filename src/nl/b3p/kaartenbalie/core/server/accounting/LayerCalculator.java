/*
 * LayerCalculator.java
 *
 * Created on December 24, 2007, 1:52 PM
 *
 */
package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPricing;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris van Lith, Jytte Schaeffer
 */
public class LayerCalculator {

    private static final Log log = LogFactory.getLog(LayerCalculator.class);
    /**
     * 
     */
    protected EntityManager em;
    /**
     * 
     */
    protected boolean externalEm = false;

    /**
     * Constructor met eigen Entitymanager
     */
    public LayerCalculator() {
        em = MyEMFDatabase.createEntityManager();
    }

    /**
     * Constructor met entitymanager van buiten
     * @param em
     */
    public LayerCalculator(EntityManager em) {
        this.em = em;
        externalEm = true;
    }

    /**
     * Deze methode berekent de prijs van een layer. Het kan zijn dat een layer
     * niet toegankelijk is of geen prijs info heeft; dit wordt dan in de
     * LayerPriceComposition als status opgenomen.
     * <p>
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
     */
    public LayerPriceComposition calculateLayerComplete(String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) {
        long startTime = System.currentTimeMillis();
        if (!AccountManager.isEnableAccounting()) {
            return null;
        }
        LayerPriceComposition tLC = new LayerPriceComposition(spAbbr, layerName, validationDate, scale, projection, planType, units, service, operation);
        tLC.setService(service);
        BigDecimal layerPrice = null;
        try {
            try {
                layerPrice = calculateLayer(tLC, spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
            } catch (NoPrizingException npe) {
                tLC.setMethod(LayerPriceComposition.METHOD_NONE);
            }
        } catch (LayerNotAvailableException lnae) {
            tLC.setMethod(LayerPriceComposition.METHOD_BLOCKED);
        }
        tLC.setLayerIsFree(new Boolean(layerPrice == null));
        if (layerPrice == null) {
            tLC.setLayerPrice(new BigDecimal(0));
        } else {
            tLC.setLayerPrice(layerPrice);
        }
        tLC.setCalculationTime(System.currentTimeMillis() - startTime);
        return tLC;
    }

    /**
     * Deze methode verwijst naar methode waar het echte werk wordt gedaan. De
     * methode bestaat om specialisatie in subklasse mogelijk te maken.
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
     * @return prijs van de layer
     * @throws nl.b3p.kaartenbalie.core.server.accounting.LayerNotAvailableException
     * @throws nl.b3p.kaartenbalie.core.server.accounting.NoPrizingException
     */
    protected BigDecimal calculateLayer(LayerPriceComposition tLC, String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws LayerNotAvailableException, NoPrizingException {
        BigDecimal layerPrice = null;
        layerPrice = calculateLayer(spAbbr, layerName, validationDate, projection, scale, units, planType, service, operation);
        tLC.setMethod(LayerPriceComposition.METHOD_OWN);
        return layerPrice;
    }

    /**
     * Deze methode haalt alle geldige prijsbepalingen op voor een service
     * provider en een bepaald type service (WMS of WFS)
     * <p>
     * @param spabbr
     * @param validationDate
     * @param service
     * @return lijst van geldige prijsbepalingen voor service provider
     */
    public List getSpLayerPricingList(String spabbr, Date validationDate, String service) {
        return em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate <= :validationDate " +
                "AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil >= :validationDate OR lp.validUntil IS null) " +
                "AND (lp.service = :service) " +
                "ORDER BY lp.indexCount DESC").
                setParameter("serverProviderPrefix", spabbr).
                setParameter("validationDate", validationDate).
                setParameter("service", service).
                getResultList();
    }

    /**
     * Deze methode haalt alle geldige prijsbepalingen op voor een bepaalde
     * kaartlaag, service (WMS of WFS), operation (bv GetMap), plantype 
     * (bv per request).
     * <p>
     * @param spAbbr
     * @param layerName
     * @param validationDate
     * @param planType
     * @param service
     * @param operation
     * @return lijst met prijsbepalingen
     */
    protected List getActiveLayerPricingList(String spAbbr, String layerName, Date validationDate, int planType, String service, String operation) {
        return em.createQuery(
                "FROM LayerPricing AS lp " +
                "WHERE (lp.deletionDate IS null OR lp.deletionDate > :validationDate) " +
                "AND lp.planType = :planType " +
                "AND lp.layerName = :layerName " +
                "AND lp.serverProviderPrefix = :serverProviderPrefix " +
                "AND lp.creationDate <= :validationDate " +
                "AND (lp.validFrom <= :validationDate OR lp.validFrom IS null) " +
                "AND (lp.validUntil >= :validationDate OR lp.validUntil IS null) " +
                "AND (lp.service = :service) " +
                "AND (lp.operation = :operation OR lp.operation IS null) " +
                "ORDER BY lp.indexCount DESC").
                setParameter("layerName", layerName).
                setParameter("serverProviderPrefix", spAbbr).
                setParameter("validationDate", validationDate).
                setParameter("planType", new Integer(planType)).
                setParameter("operation", operation).
                setParameter("service", service).
                getResultList();
    }

    /**
     * Deze methode checkt of een bepaalde prijsbepaling bestaat.
     * <p>
     * @param spAbbr
     * @param layerName
     * @param validationDate
     * @param planType
     * @param service
     * @param operation
     * @return true als prijsbepaling bestaat
     * @see getActiveLayerPricingList
     */
    public boolean ActiveLayerPricingExists(String spAbbr, String layerName, Date validationDate, int planType, String service, String operation) {
        try {
            List possibleLayerPricings = getActiveLayerPricingList(spAbbr, layerName, validationDate, planType, service, operation);
            if (possibleLayerPricings == null || possibleLayerPricings.size() == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Error collecting layer pricings: ", e);
            return false;
        }
    }

    /**
     * Deze methode doorloopt alle mogelijke layer pricings. De lijst is gesorteerd
     * van nieuw naar oud, dus de nieuwste (eerste) prijs wordt geselecteerd. 
     * Als er een pricing bij zit die een projectie heeft, dan wordt vereist 
     * dat de aanvraag ook een schaal en projectie heeft en dat deze overeenkomen 
     * met de pricing.
     * <p>
     * Deze methode geeft een LayerNotAvailableException wanneer de layer pricing
     * een projectie heeft, maar de projectie en schaal niet overeenkomen. Het is 
     * dan niet toegestaan deze layer te zien (ook niet via een andere princing).
     * <p>
     * Indien de layer pricing geen projectie heeft geeft deze methode 
     * NoPrizingException indien er geen pricing gevonden kan worden. Via een 
     * andere methode kan dan alsnog een pricing gevonden worden.
     * <p>
     * @param spAbbr
     * @param layerName
     * @param validationDate
     * @param projection
     * @param scale
     * @param planType
     * @param service
     * @param operation
     * @return
     * @throws LayerNotAvailableException layer mag niet getoond worden
     * @throws NoPrizingException geen prijsinfo bekend, dus verder zoeken
     */
    public LayerPricing getActiveLayerPricing(String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, int planType, String service, String operation) throws LayerNotAvailableException, NoPrizingException {

        List possibleLayerPricings = getActiveLayerPricingList(spAbbr, layerName, validationDate, planType, service, operation);
        Iterator layerPricingIter = possibleLayerPricings.iterator();

        LayerPricing layerPricing = null;
        boolean requireProjection = false;
        while (layerPricingIter.hasNext()) {
            LayerPricing plp = (LayerPricing) layerPricingIter.next(); // plp == possibleLayerPricing
            // sortering is van nieuw naar oud, eerste pricing
            if (layerPricing == null) {
                layerPricing = plp;
            }
            if (plp.getProjection() != null) {
                requireProjection = true;
                break;
            }
        }

        if (requireProjection) {
            if (projection == null) {
                log.error("Projection cannot be null");
                throw new LayerNotAvailableException("Projection cannot be null");
            }
            if (scale == null) {
                log.error("Scale cannot be null");
                throw new LayerNotAvailableException("Scale cannot be null");
            }
            while (layerPricingIter.hasNext()) {
                LayerPricing plp = (LayerPricing) layerPricingIter.next(); // plp == possibleLayerPricing
                if (plp.getProjection() == null) {
                    continue;
                }

                if ((plp.getMinScale() == null || plp.getMinScale().compareTo(scale) <= 0) &&
                        (plp.getMaxScale() == null || plp.getMaxScale().compareTo(scale) >= 0) &&
                        plp.getProjection().equalsIgnoreCase(projection)) {
                    layerPricing = plp;
                    break;
                }
            }
            if (layerPricing == null) {
                throw new LayerNotAvailableException();
            }
        }

        if (layerPricing == null) {
            throw new NoPrizingException();
        }
        return layerPricing;
    }

    /**
     * Deze methode bepaalt de prijs van de layer. Indien een van de vereiste
     * gegevens niet is opgegeven dan wordt de layer niet beschikbaar gemeld
     * via LayerNotAvailableException. 
     * <p>
     * De layer naam is verplicht, speciaal bij WMS layers worden layers zonder 
     * naam gebruikt als placeholders voor sublayers. Dergelijke layers kunnen 
     * geen prijsinfo hebben. Er wordt dan een NoPrizingException gegooid.
     * <p>
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
     * @throws NoPrizingException 
     * @throws LayerNotAvailableException 
     */
    public BigDecimal calculateLayer(String spAbbr, String layerName, Date validationDate, String projection, BigDecimal scale, BigDecimal units, int planType, String service, String operation) throws NoPrizingException, LayerNotAvailableException {
        if (spAbbr == null) {
            log.error("spAbbr is required!");
            throw new LayerNotAvailableException("spAbbr is required!");
        }
        if (validationDate == null) {
            log.error("ValidationDate is a required field!");
            throw new LayerNotAvailableException("ValidationDate is a required field!");
        }
        if (units == null) {
            log.error("Units is a required field!");
            throw new LayerNotAvailableException("Units is a required field!");
        }
        if (planType <= 0) {
            log.error("PlanType is a required field!");
            throw new LayerNotAvailableException("PlanType is a required field!");
        }
        if (layerName == null || layerName.trim().length() == 0) {
            throw new NoPrizingException("Layer is a placeholder and therefor cannot hold pricingInformation.");
        }

        BigDecimal layerPrice = null;

        LayerPricing layerPricing = getActiveLayerPricing(spAbbr, layerName, validationDate, projection, scale, planType, service, operation);
        if (layerPricing.getUnitPrice() != null &&
                (layerPricing.getLayerIsFree() == null ||
                (layerPricing.getLayerIsFree() != null && layerPricing.getLayerIsFree().booleanValue() == false))) {
            layerPrice = layerPricing.getUnitPrice().multiply(units);
        }

        if (layerPrice != null && layerPrice.compareTo(new BigDecimal(0)) < 0) {
            layerPrice = new BigDecimal(0);
        }
        return layerPrice;

    }

    /**
     * Sluit een eigen entity manager
     * @throws java.lang.Exception
     */
    public void closeEntityManager() throws Exception {
        if (externalEm) {
            log.error("Trying to close an external EntityManager. This is not the place to do it!");
            throw new Exception("Trying to close an external EntityManager. This is not the place to do it!");
        }
        em.close();
    }
}
