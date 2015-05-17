package nl.b3p.kaartenbalie.service;

import java.lang.reflect.Method;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

/**
 * Dit filter maakt geen transactie aan.
 * Dit filter dient geconfigureerd in dwr.xml:
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
 * @see DwrTransactionFilter
 */
public class DwrEntityManagerFilter implements AjaxFilter {

    private static final Log log = LogFactory.getLog(DwrEntityManagerFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {

        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.DWR_EM);
            Object ret = chain.doFilter(obj, method, params);
            return ret;
        } catch (Exception e) {
            log.error("Exception occured during DWR call - no transaction active", e);
            throw e;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.DWR_EM);
        }
    }
}
