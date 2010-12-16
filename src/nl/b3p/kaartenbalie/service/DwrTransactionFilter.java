package nl.b3p.kaartenbalie.service;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;
import org.directwebremoting.annotations.GlobalFilter;

/**
 * Dit filter maakt een transactie aan.
 * Dit filter dient in dwr.xml geconfigureerd te worden.
 *
<dwr>
<allow>
<!-- globaal -->
<filter class="nl.b3p.commons.jpa.DwrTransactionFilter"/>
...
<create creator="new" javascript="MyClass">
<!-- alleen op enkele class -->
<filter class="nl.b3p.commons.jpa.DwrTransactionFilter"/>
</create>
</allow>
</dwr>
 *
 * @author Chris
 * @see DwrEntityManagerFilter
 */
@GlobalFilter
public class DwrTransactionFilter implements AjaxFilter {

    private static final Log log = LogFactory.getLog(DwrTransactionFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {

        EntityManager em = null;
        EntityTransaction tx = null;

        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.DWR_EM);
            em = MyEMFDatabase.getEntityManager(MyEMFDatabase.DWR_EM);
            tx = em.getTransaction();
            tx.begin();
            
            Object ret = chain.doFilter(obj, method, params);

            if (tx != null && tx.isActive()) {
                log.debug("Committing active transaction");
                tx.commit();
            }
            return ret;
        } catch (Exception e) {
            log.error("Exception occured during DWR call" + (tx.isActive() ? ", rolling back transaction" : " - no transaction active"), e);

            if (tx.isActive()) {
                try {
                    tx.rollback();
                } catch (Exception e2) {
                    /* log de exception maar swallow deze verder, omdat alleen
                     * wordt gerollback()'d indien er al een eerdere exception
                     * was gethrowed. Die wordt door deze te swallowen verder
                     * gethrowed.
                     */
                    log.error("Exception rolling back transaction", e2);
                }
            }
            throw e;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.DWR_EM);
        }
    }
}
