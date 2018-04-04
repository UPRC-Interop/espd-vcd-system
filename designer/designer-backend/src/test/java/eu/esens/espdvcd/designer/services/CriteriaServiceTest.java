package eu.esens.espdvcd.designer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CriteriaServiceTest {
    CriteriaService eCertisCriteriaService,  predefinedCriteriaService;
    private final static Logger LOGGER = Logger.getLogger(CriteriaServiceTest.class.getName());

    @Before
    public void setUp() throws Exception {
        eCertisCriteriaService = new ECertisCriteriaService();
        predefinedCriteriaService = new PredefinedCriteriaService();
    }

    @Test
    public void getCriteria() {
    }

    @Test
    public void getUnselectedCriteria() {
    }

    @Test
    public void getTranslatedCriteria() throws RetrieverException, JsonProcessingException {
//        List<SelectableCriterion> translatedCriteria = eCertisCriteriaService.getTranslatedCriteria("el");
//        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        LOGGER.info(writer.writeValueAsString(translatedCriteria));
        ECertisCriteriaExtractor ex = new ECertisCriteriaExtractor();
//        List<SelectableCriterion> engCriteria = ex.getFullList();
        ex.setLang("el");
//        List<SelectableCriterion> elCriteria = ex.getFullList();
        LOGGER.info(ex.getCriterion("c27b7c4e-c837-4529-b867-ed55ce639db5").getDescription());
    }

    @Test
    public void getfilteredCriteriaList() {
    }
}