package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PredefinedCriteriaServiceTest {

    CriteriaService predefinedCriteriaService = new PredefinedCriteriaService(EDMVersion.V2);

    @Test
    public void getCriteria() throws Exception {
        System.out.println(new ObjectMapper().registerModule(new JavaTimeModule()).writerWithDefaultPrettyPrinter().writeValueAsString(predefinedCriteriaService.getCriteria()));
    }

    @Test
    public void getUnselectedCriteria() throws Exception {
        List<SelectableCriterion> theList = predefinedCriteriaService.getCriteria();
        theList.remove(30);
        List<SelectableCriterion> theListWithMissing30 = predefinedCriteriaService.getUnselectedCriteria(theList);
        Assert.assertFalse(theListWithMissing30.get(theListWithMissing30.size() - 1).isSelected());
        System.out.println(theListWithMissing30.size());
    }

    @Test
    public void getTranslatedCriteria() {
    }
}