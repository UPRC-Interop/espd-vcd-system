package eu.esens.espdvcd.persistence;

/**
 * TODO: Add description.
 *
 */
public enum DB {
    RDBMS("hibernate.rdbms.cfg.xml"),
    IN_MEMORY_DB("hibernate.in_memory_db.cfg.xml");

    private final String hibernateConfiguration;

    DB(String hibernateConfiguration) {
        this.hibernateConfiguration = hibernateConfiguration;
    }

    public String hibernateConfiguration() {
        return hibernateConfiguration;
    }
}
