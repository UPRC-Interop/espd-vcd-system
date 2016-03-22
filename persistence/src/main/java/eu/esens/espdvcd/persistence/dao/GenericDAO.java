package eu.esens.espdvcd.persistence.dao;

import eu.esens.espdvcd.persistence.DB;
import eu.esens.espdvcd.persistence.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * TODO: Add description.
 *
 */
public class GenericDAO<T> implements DAOInterface<T, Serializable> {
    private static final Logger LOG = LoggerFactory.getLogger(GenericDAO.class);
    private DB db;
    private Session currentSession = null;
    private Transaction transaction = null;

    public GenericDAO(DB db) {
        this.db = db;
    }

    private void beginTransaction() {
        switch (db) {
            case RDBMS:
                currentSession = HibernateUtil.getSessionFactoryForRDBMS().openSession();
                LOG.debug("Session opened. [RDBMS]");
                break;
//            case IN_MEMORY_DB:
//                currentSession = HibernateUtil.getSessionFactoryForInMemoryDB().openSession();
//                LOG.debug("Session opened. [IN_MEMORY]");
//                break;
        }
//        currentSession.setFlushMode(FlushMode.MANUAL);
//        ManagedSessionContext.bind(currentSession);
        transaction = currentSession.beginTransaction();
    }

    private void commitTransaction() {
//        currentSession.flush();
        transaction.commit();
        LOG.debug("Transaction committed.");
    }

    private void rollbackTransaction() {
        transaction.rollback();
        LOG.debug("Transaction rolled back.");
    }

    private void closeSession() {
        currentSession.close();
        LOG.debug("Session closed.");
    }

    @Override
    public Long getCountOf(Class<T> clazz) {
        Long data = -1L;
        try {
            beginTransaction();
            data = (Long) currentSession.createCriteria(clazz.getName()).setProjection(Projections.rowCount()).uniqueResult();
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
        return data;
    }

    @Override
    public void persist(T entity) {
        try {
            beginTransaction();
            currentSession.save(entity);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
    }

    @Override
    public void update(T entity) {
        try {
            beginTransaction();
            currentSession.update(entity);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
    }

    @Override
    public void delete(T entity) {
        try {
            beginTransaction();
            currentSession.delete(entity);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
    }

    @Override
    public T findById(Class<T> clazz, Serializable id) {
        T data = null;
        try {
            beginTransaction();
            data = currentSession.get(clazz, id);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
        return data;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Class<T> clazz) {
        List<T> data = null;
        try {
            beginTransaction();
            data = currentSession.createQuery("FROM " + clazz.getName()).list();
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
        return data;
    }

    @Override
    public void deleteAll(Class<T> clazz) {
        try {
            beginTransaction();
            List<T> entityList = findAll(clazz);
            entityList.forEach(this::delete);
            commitTransaction();
        } catch (HibernateException e) {
            rollbackTransaction();
            throw new RuntimeException(e);
        } finally {
            closeSession();
        }
    }
}
