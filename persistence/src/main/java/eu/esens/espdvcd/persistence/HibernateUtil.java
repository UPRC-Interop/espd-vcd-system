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
    private static final String HIBERNATE_ESPDVCD_CFG = "hibernate.espdvcd.cfg.xml";
    private static final SessionFactory sessionFactory;
    private static StandardServiceRegistryBuilder standardServiceRegistryBuilder;

    static {
        try {
            Configuration configuration =
                    new Configuration().configure(HibernateUtil.HIBERNATE_ESPDVCD_CFG);
            standardServiceRegistryBuilder =
                    new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(
                    standardServiceRegistryBuilder.build());
        } catch (Throwable ex) {
            LOG.error("Initial SessionFactories creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
