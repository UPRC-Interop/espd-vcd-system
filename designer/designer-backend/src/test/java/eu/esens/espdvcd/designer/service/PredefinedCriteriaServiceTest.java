package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.model.SelectableCriterion;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PredefinedCriteriaServiceTest {

    CriteriaService predefinedCriteriaService = new PredefinedCriteriaService();

    @Test
    public void getCriteria() throws Exception {
        System.out.println(predefinedCriteriaService.getCriteria().size());
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