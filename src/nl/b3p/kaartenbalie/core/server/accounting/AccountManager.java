/*
 * AccountManager.java
 *
 * Created on November 15, 2007, 9:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerUsageMutation;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;

/**
 *
 * @author Chris Kramer
 */
public class AccountManager {
    
    
    public static final long serialVersionUID = 856294562L;
    private static boolean enableAccounting = false;
    private BigDecimal balance;
    
    
    private static ThreadLocal tluHolder = new ThreadLocal();
    private static Map managers;
    
    
    static
    {
        managers = new HashMap();
    }
    
    public synchronized static AccountManager getAccountManager(Integer organizationId) {
        
        AccountManager accountManager = (AccountManager) managers.get(organizationId);
        if (accountManager == null) {
            accountManager = new AccountManager(organizationId);
            if (enableAccounting) {
                EntityManager em = MyEMFDatabase.createEntityManager();
                Organization organization = (Organization) em.find(Organization.class, organizationId);
                if (organization.getAccount() == null) {
                    EntityTransaction et = em.getTransaction();
                    et.begin();
                    Account account = new Account(organization);
                    organization.setAccount(account);
                    em.persist(account);
                    et.commit();
                }
                em.close();
            }
            managers.put(organizationId, accountManager);
        }
        return accountManager;
    }
    
    private Integer companyId;
    /** Creates a new instance of AccountManager */
    public AccountManager(Integer companyId) {
        this.companyId = companyId;
    }
    
    public TransactionLayerUsage beginTLU() {
        
        TransactionLayerUsage tlu = new TransactionLayerUsage();
        tluHolder.set(tlu);
        return tlu;
    }
    public TransactionLayerUsage getTLU() {
        return  (TransactionLayerUsage) tluHolder.get();
    }
    
    public void endTLU() {
        tluHolder.set(null);
    }
    
    
    
    public synchronized void doTransaction(Transaction transaction, User user) throws Exception{
        if (!enableAccounting) { return;}
        long time = System.currentTimeMillis();
        EntityManager em = MyEMFDatabase.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Account account = (Account)em.find(Account.class, companyId);
        balance = account.getCreditBalance();
        transaction.setAccount(account);
        transaction.setUser(user);
        
        try {
            
            if (transaction.getCreditAlteration().doubleValue() < 0) {
                throw new Exception("Transaction creditalteration cannot be less then zero.");
            }
            transaction.validate();
            transaction.setCreditAlteration(transaction.getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP));
            if (transaction.getType() == transaction.DEPOSIT) {
                balance =balance.add(transaction.getCreditAlteration());
            } else if (transaction.getType() == transaction.WITHDRAW) {
                
                if (transaction.getClass().equals(TransactionLayerUsage.class)) {
                    TransactionLayerUsage tlu = (TransactionLayerUsage) transaction;
                    Iterator lumIter = tlu.getLayerUsageMutations().iterator();
                    while(lumIter.hasNext()) {
                        LayerUsageMutation lum  = (LayerUsageMutation) lumIter.next();
                        //TODO
                        tlu.setCreditAlteration(tlu.getCreditAlteration().add(new BigDecimal(0.01)));
                    }
                }
                transaction.setCreditAlteration(transaction.getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP));
                if (transaction.getCreditAlteration().doubleValue() == 0) {
                    et.rollback();
                    em.close();
                    return;
                }
                if (balance.subtract(transaction.getCreditAlteration()).doubleValue() < 0) {
                    throw new Exception(
                            "Insufficient credits for transaction. " +
                            "Required credits: "  + transaction.getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP).toString() +  ", " +
                            "Current balance: " + balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
                balance =balance.subtract(transaction.getCreditAlteration());
                
                
            } else {
                throw new Exception("Unsupported transaction type");
            }
            account.setCreditBalance(balance);
            transaction.setMutationDate(new Date());
            transaction.setStatus(Transaction.ACCEPTED);
            
        } catch (Exception e) {
            transaction.setErrorMessage(e.getMessage());
            transaction.setStatus(Transaction.REFUSED);
            throw e;
        } finally {
            em.persist(transaction);
            et.commit();
            em.close();
        }
    }
    public double getBalance() {
        if (!enableAccounting) { return 0.0;}
        if (balance == null) {
            EntityManager em = MyEMFDatabase.createEntityManager();
            Account account = (Account) em.find(Account.class, companyId);
            balance = account.getCreditBalance();
            em.close();
        }
        if (balance != null) {
            return balance.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            return 0.0;
        }
        
    }
    
    public List getTransactions(int listMax, Class transactionType) {
        EntityManager em = MyEMFDatabase.createEntityManager();
        if (Transaction.class.isAssignableFrom(transactionType)) {
            return em.createQuery(
                    "FROM " + transactionType.getSimpleName() + " AS transaction ORDER by transaction.transactionDate DESC").setMaxResults(listMax).getResultList();
        }
        return null;
    }
    
    public static void setEnableAccounting(boolean state) {
        enableAccounting = state;
    }
    
    
    
}
