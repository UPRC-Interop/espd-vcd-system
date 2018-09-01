package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class RegulatedModeltoESPDRequestV2ServiceTest {

    @Test
    @Ignore
    public void testCriterionNumberAfterImport() throws Exception {
        File espdRequestFile = new File(RegulatedModeltoESPDRequestV2ServiceTest
                .class
                .getResource("/REGULATED-ESPD-Request_2.0.2-1.xml")
                .toURI());
        Assert.assertNotNull(espdRequestFile);
        ESPDRequest req = BuilderFactory
                .EDM_V2
                .createRegulatedModelBuilder()
                .importFrom(new FileInputStream(espdRequestFile))
                .createESPDRequest();

//        req.setCriterionList(req.getFullCriterionList().subList(0, req.getFullCriterionList().size() / 2));
        System.out.printf("Criteria count before getting the unselected: %s \n", req.getFullCriterionList().size());

        CriteriaService criteriaService = new RetrieverCriteriaService(EDMVersion.V2);
        req.setCriterionList(criteriaService.getUnselectedCriteria(req.getFullCriterionList()));
        System.out.printf("Criteria count after getting the unselected: %s, should be: %s\n"
                ,req.getFullCriterionList().size()
                ,criteriaService.getCriteria().size());
        Assert.assertEquals(criteriaService.getCriteria(), req.getFullCriterionList());
    }

}