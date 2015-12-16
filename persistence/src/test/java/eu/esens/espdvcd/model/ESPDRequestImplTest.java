package eu.esens.espdvcd.model;

import eu.esens.espdvcd.persistence.dao.GenericDAO;
import eu.esens.espdvcd.persistence.DB;
import org.junit.Test;

/**
 * Author: Panagiotis NICOLAOU
 * Creation date: 12/1/2015
 * Email: pnikola@unipi.gr
 */
public class ESPDRequestImplTest {

    @Test
    public void testCreateESPDRequest() {
        GenericDAO<ESPDRequestImpl> espdRequestDAO = new GenericDAO<>(DB.RDBMS);
        ESPDRequestImpl espdRequest = new ESPDRequestImpl();
//        espdRequestDAO.persist(espdRequest);
    }
}
