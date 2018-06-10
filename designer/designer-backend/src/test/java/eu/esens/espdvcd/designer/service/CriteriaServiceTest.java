package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

public class CriteriaServiceTest {
    private CriteriaService eCertisCriteriaService,  predefinedCriteriaService;
    private final static Logger LOGGER = Logger.getLogger(CriteriaServiceTest.class.getName());

    @Before
    public void setUp() throws Exception {
        eCertisCriteriaService = new ECertisCriteriaService(SchemaVersion.V2);
        predefinedCriteriaService = new PredefinedCriteriaService(SchemaVersion.V1);
    }

    @Test
    public void getCriteria() {
    }

    @Test
    public void getUnselectedCriteria() {
    }

    @Test
    public void getTranslatedCriteria() throws RetrieverException {
//        List<SelectableCriterion> translatedCriteria = eCertisCriteriaService.getTranslatedCriteria("el");
//        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        LOGGER.info(writer.writeValueAsString(translatedCriteria));
        ECertisCriteriaExtractor ex = new ECertisCriteriaExtractor(SchemaVersion.V1);
//        List<SelectableCriterion> engCriteria = ex.getFullList();
        ex.setLang("el");
//        List<SelectableCriterion> elCriteria = ex.getFullList();
        LOGGER.info(ex.getCriterion("c27b7c4e-c837-4529-b867-ed55ce639db5").getDescription());
    }

    @Test
    public void getfilteredCriteriaList() {
    }
}