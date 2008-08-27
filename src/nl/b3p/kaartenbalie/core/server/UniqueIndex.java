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
package nl.b3p.kaartenbalie.core.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris Kramer
 */
public class UniqueIndex {

    private static final Log log = LogFactory.getLog(UniqueIndex.class);
    private Integer id;
    private String indexName;
    private Integer indexCount;
    public static final String INDEX_LAYER_PRICING = "layerpricing";

    public UniqueIndex() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public Integer getIndexCount() {
        return indexCount;
    }

    public void setIndexCount(Integer indexCount) {
        this.indexCount = indexCount;
    }

    /*
     *
     */
    public synchronized static Integer createNextUnique(String indexName) throws Exception {
        if (indexName == null) {
            log.error("Missing indexName");
            throw new Exception("Missing indexName");
        }
        indexName = indexName.toUpperCase();
        Integer nextUnique = null;
        Object identity = null;
        EntityTransaction et = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.TRANSACTION_EM);
            EntityManager em = MyEMFDatabase.getEntityManager2(MyEMFDatabase.TRANSACTION_EM);
            et = em.getTransaction();
            et.begin();
            UniqueIndex ui = null;
            try {
                ui = (UniqueIndex) em.createQuery(
                        "FROM UniqueIndex AS ui " +
                        "WHERE ui.indexName = :indexName").setParameter("indexName", indexName).getSingleResult();
                nextUnique = new Integer(ui.getIndexCount().intValue() + 1);
                ui.setIndexCount(nextUnique);
            } catch (NoResultException nre) {
                ui = new UniqueIndex();
                ui.setIndexName(indexName);
                nextUnique = new Integer(0);
                ui.setIndexCount(nextUnique);
                em.persist(ui);
            }
            et.commit();
        } catch (Exception e) {
            if (et != null) {
                et.rollback();
            }
            throw e;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.TRANSACTION_EM);
        }
        if (nextUnique == null) {
            log.error("Unable to generate next unique number!");
            throw new Exception("Unable to generate next unique number!");
        }
        return nextUnique;
    }
}
