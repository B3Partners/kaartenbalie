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
package nl.b3p.kaartenbalie.core.server.accounting;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.User;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Account;
import nl.b3p.kaartenbalie.core.server.accounting.entity.LayerPriceComposition;
import nl.b3p.kaartenbalie.core.server.accounting.entity.Transaction;
import nl.b3p.kaartenbalie.core.server.accounting.entity.TransactionLayerUsage;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Chris
 */
public class AccountManager {

    private static final Log log = LogFactory.getLog(AccountManager.class);
    /**
     * 
     */
    public static final long serialVersionUID = 856294562L;
    private static boolean enableAccounting = false;
    private BigDecimal balance;
    private static ThreadLocal tluHolder = new ThreadLocal();
    private static Map managers;
    private Integer organizationId;

    private AccountManager() {
    }
    

    static {
        managers = new HashMap();
    }

    /**
     * 
     * @param organizationId
     * @return
     */
    public synchronized static AccountManager getAccountManager(Integer organizationId) throws Exception {
        AccountManager accountManager = (AccountManager) managers.get(organizationId);
        if (accountManager == null) {
            accountManager = new AccountManager(organizationId);
            if (enableAccounting) {
                EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
                Account account = (Account) em.find(Account.class, organizationId);
                if (account == null) {
                    Organization organization = (Organization) em.find(Organization.class, organizationId);
                    account = new Account(organization);
                    organization.setAccount(account);
                    em.persist(account);
                    em.flush();
                }
                managers.put(organizationId, accountManager);
            }
        }
        return accountManager;
    }

    /** Creates a new instance of AccountManager
     * @param organizationId 
     */
    public AccountManager(Integer organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 
     * @param transactionClass
     * @param description
     * @return
     * @throws java.lang.Exception
     */
    public Transaction prepareTransaction(
            Class transactionClass, String description) throws Exception {
        if (!isEnableAccounting()) {
            return null;
        }
        if (!Transaction.class.isAssignableFrom(transactionClass)) {
            log.error("Class transactionClass is not assignable.");
            throw new Exception("Class transactionClass is not assignable.");
        }
        Transaction transaction = null;
        EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
        Account account = (Account) em.find(
                Account.class,
                organizationId);

        transaction = (Transaction) transactionClass.newInstance();
        transaction.setStatus(Transaction.PENDING);
        transaction.setAccount(account);
        transaction.setDescription(description);
        em.persist(transaction);
        em.flush();

        return transaction;
    }

    /**
     * 
     * @return
     * @throws java.lang.Exception
     */
    public TransactionLayerUsage beginTLU()
            throws Exception {
        TransactionLayerUsage tlu = (TransactionLayerUsage) prepareTransaction(TransactionLayerUsage.class, null);
        tluHolder.set(tlu);
        return tlu;
    }

    /**
     * 
     * @return
     */
    public TransactionLayerUsage getTLU() {
        return (TransactionLayerUsage) tluHolder.get();
    }

    /**
     * 
     */
    public void endTLU() {
        tluHolder.set(null);
    }

    /**
     * 
     * @param accountTransaction
     * @return
     * @throws java.lang.Exception
     */
    public Transaction nullvalidateTransaction(Transaction accountTransaction) throws Exception {
        if (accountTransaction == null) {
            log.error("Trying to nullvalidate a null transaction.");
            throw new Exception("Trying to nullvalidate a null transaction.");
        }

        if (accountTransaction.getCreditAlteration() == null) {
            log.error("Trying to nullvalidate a transaction with a nullvalue for creditAlteration.");
            throw new Exception("Trying to nullvalidate a transaction with a nullvalue for creditAlteration.");
        }

        if (accountTransaction.getCreditAlteration().doubleValue() < 0) {
            log.error("Transaction creditalteration cannot be less then zero.");
            throw new TransactionDeniedException("Transaction creditalteration cannot be less then zero.");
        }

        if (accountTransaction.getCreditAlteration().doubleValue() == 0) {
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            em.remove(em.find(Transaction.class, accountTransaction.getId()));
            em.flush();
            accountTransaction = null;
        }
        return accountTransaction;
    }

    /**
     * 
     * @param accountTransaction
     * @param user
     * @throws java.lang.Exception
     */
    public synchronized void commitTransaction(Transaction accountTransaction, User user) throws Exception {
        if (!enableAccounting) {
            return;
        }

        if (accountTransaction != null) {
            //Create an EntityManager
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
            //Get the account and set the current balance. Update the class variable at the same time.
            Account account = (Account) em.find(Account.class, organizationId);
            balance = account.getCreditBalance();
            //Set the account & user for the accountTransaction.
            accountTransaction.setAccount(account);
            accountTransaction.setUser(user);

            try {
                /*
                 * If the class is an TransactionLayerUsage, we'll have to do some work before testing.
                 * We need to persist the LayerPriceCompositions into the entityManager..
                 */
                if (accountTransaction.getClass().equals(TransactionLayerUsage.class)) {
                    TransactionLayerUsage tlu = (TransactionLayerUsage) accountTransaction;
                    Iterator iterPriceComp = tlu.getLayerPriceCompositions().iterator();
                    while (iterPriceComp.hasNext()) {
                        LayerPriceComposition lpc = (LayerPriceComposition) iterPriceComp.next();
                        em.persist(lpc);
                    }
                }
                /*
                 *Done, Check if the creditAlteration is less then zero or equal to zero and possibly throw an Exception
                 */
                if (accountTransaction.getCreditAlteration().doubleValue() < 0) {
                    throw new TransactionDeniedException("Transaction creditalteration cannot be less then zero.");
                }
                //Run validation (checks what type of transaction is allowed..)
                accountTransaction.validate();
                //Scale the creditAlteration...
                accountTransaction.setCreditAlteration(accountTransaction.getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP));

                //Now check if the transaction either has to deposit or withdraw...
                BigDecimal newBalance = null;
                if (accountTransaction.getType() == accountTransaction.DEPOSIT) {
                    newBalance = balance.add(accountTransaction.getCreditAlteration());
                } else if (accountTransaction.getType() == accountTransaction.WITHDRAW) {
                    newBalance = balance.subtract(accountTransaction.getCreditAlteration());
                    if (newBalance.doubleValue() < 0) {
                        throw new TransactionDeniedException(
                                "Insufficient credits for transaction. " +
                                "Required credits: " + accountTransaction.getCreditAlteration().setScale(2, BigDecimal.ROUND_HALF_UP).toString() + ", " +
                                "Current balance: " + balance.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    }
                } else {
                    log.error("Unsupported transaction type");
                    throw new Exception("Unsupported transaction type");
                }
                account.setCreditBalance(newBalance);
                accountTransaction.setMutationDate(new Date());
                accountTransaction.setStatus(accountTransaction.ACCEPTED);
                em.merge(accountTransaction);
                em.flush();
                balance = newBalance;
            } catch (TransactionDeniedException tde) {
                accountTransaction.setErrorMessage(tde.getMessage());
                accountTransaction.setStatus(accountTransaction.REFUSED);
                em.merge(accountTransaction);
                em.flush();
                throw tde;
            }
        }
    }

    /**
     * 
     * @return
     */
    public double getBalance() throws Exception {
        if (!enableAccounting) {
            return 0.0;
        }

        if (balance == null) {
            Object identity = null;
            try {
                identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
                EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);
                Account account = (Account) em.find(Account.class, organizationId);
                balance = account.getCreditBalance();
            } finally {
                MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
            }

        }
        if (balance != null) {
            return balance.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } else {
            return 0.0;
        }

    }

    /**
     * 
     * @param listMax
     * @param transactionType
     * @return
     */
    public List getTransactions(
            int listMax, Class transactionType) throws Exception {
        return getTransactions(0, listMax, transactionType);
    }

    /**
     * 
     * @param firstResult
     * @param listMax
     * @param transactionType
     * @return
     */
    public List getTransactions(
            int firstResult, int listMax, Class transactionType) throws Exception {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            List resultList = null;
            if (Transaction.class.isAssignableFrom(transactionType)) {
                StringBuffer q = new StringBuffer();
                q.append(
                        "FROM ");
                q.append(transactionType.getSimpleName());
                q.append(
                        " AS transaction ");
                q.append(
                        " WHERE transaction.account.id = :accid");
                q.append(
                        " ORDER by transaction.transactionDate DESC");
                Query query = em.createQuery(q.toString());
                query.setParameter(
                        "accid", organizationId);
                query.setFirstResult(firstResult);
                query.setMaxResults(listMax);
                resultList = query.getResultList();
            }
            return resultList;
        } finally {
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }

    }

    /**
     * 
     * @param state
     */
    public static void setEnableAccounting(boolean state) {
        enableAccounting = state;
    }

    /**
     * 
     * @return
     */
    public static boolean isEnableAccounting() {
        return enableAccounting;
    }
}
