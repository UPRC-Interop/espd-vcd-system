package eu.esens.espdvcd.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public class HibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtil.class);
    private static final SessionFactory sessionFactoryForRDBMS;
//    private static final SessionFactory sessionFactoryForInMemoryDB;

    static {
        try {
            Configuration configuration =
                    new Configuration().configure(DB.RDBMS.hibernateConfiguration());
            StandardServiceRegistryBuilder standardServiceRegistryBuilder =
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactoryForRDBMS = configuration.buildSessionFactory(
                    standardServiceRegistryBuilder.build());

//            configuration =
//                    new Configuration().configure(DB.IN_MEMORY_DB.hibernateConfiguration());
//            standardServiceRegistryBuilder =
//                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//            sessionFactoryForInMemoryDB = configuration.buildSessionFactory(
//                    standardServiceRegistryBuilder.build())
        } catch (Throwable ex) {
            LOG.error("Initial SessionFactories creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactoryForRDBMS() {
        return sessionFactoryForRDBMS;
    }

//    public static SessionFactory getSessionFactoryForInMemoryDB() {
//        return sessionFactoryForInMemoryDB;
//    }

    public static void shutdownRDBMS() {
        // Close caches and connection pools
        getSessionFactoryForRDBMS().close();
    }

//    public static void shutdownInMemoryDB() {
//        getSessionFactoryForInMemoryDB().close();
//    }
}
