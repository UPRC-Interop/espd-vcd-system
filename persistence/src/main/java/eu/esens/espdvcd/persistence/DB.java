package eu.esens.espdvcd.persistence;

/**
 * TODO: Add description.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public enum DB {
    RDBMS("hibernate.rdbms.cfg.xml"),
    IN_MEMORY_DB("hibernate.in_memory.cfg.xml");

    private final String hibernateConfiguration;

    DB(String hibernateConfiguration) {
        this.hibernateConfiguration = hibernateConfiguration;
    }

    public String hibernateConfiguration() {
        return hibernateConfiguration;
    }
}
