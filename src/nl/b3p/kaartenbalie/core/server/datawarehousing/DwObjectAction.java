/*
 * DwObjectAction.java
 *
 * Created on November 5, 2007, 10:41 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.datawarehousing;

public class DwObjectAction {
    
    public static final int MERGE = 0;
    public static final int PERSIST = 1;
    public static final int REMOVE = 2;
    public static final int PERSIST_OR_MERGE = 3;
    private Class clazz;
    private Integer primaryKey;
    private int objectAction;
    
    
    public DwObjectAction(Class clazz, Integer primaryKey, int objectAction) {
        this.setClazz(clazz);
        this.setPrimaryKey(primaryKey);
        this.setObjectAction(objectAction);
    }
    
    public Integer getPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(Integer primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public int getObjectAction() {
        return objectAction;
    }
    
    public void setObjectAction(int objectAction) {
        this.objectAction = objectAction;
    }
    
    public Class getClazz() {
        return clazz;
    }
    
    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
    
    
    
}
