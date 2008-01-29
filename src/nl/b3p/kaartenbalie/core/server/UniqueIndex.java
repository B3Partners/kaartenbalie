/*
 * UniqueNumber.java
 *
 * Created on January 29, 2008, 4:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class UniqueIndex {
    
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
        if (indexName == null){
            throw new Exception("Missing indexName");
        }
        indexName = indexName.toUpperCase();
        Integer nextUnique = null;
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        try {
            UniqueIndex ui = null;
            try {
                ui = (UniqueIndex) em.createQuery(
                        "FROM UniqueIndex AS ui " +
                        "WHERE ui.indexName = :indexName")
                        .setParameter("indexName", indexName)
                        .getSingleResult();
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
            et.rollback();
            throw e;
        } finally {
            em.close();
        }
        if (nextUnique == null) {
            throw new Exception("Unable to generate next unique number!");
        }
        return nextUnique;
    }
    
    public static void main(String[] args) throws Exception{
        MyEMFDatabase.openEntityManagerFactory(MyEMFDatabase.nonServletKaartenbaliePU);
        System.out.println(createNextUnique("Lorem"));
    }
    
    
}
