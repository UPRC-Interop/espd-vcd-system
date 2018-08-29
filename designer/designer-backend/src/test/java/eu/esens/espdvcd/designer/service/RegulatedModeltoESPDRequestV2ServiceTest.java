package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

public class RegulatedModeltoESPDRequestV2ServiceTest {

    @Test
    public void testCriterionNumberAfterImport() throws Exception{
        File espdRequestFile = new File(RegulatedModeltoESPDRequestV2ServiceTest.class.getResource("/REGULATED-ESPD-Request_2.0.2-1.xml").toURI());
        Assert.assertNotNull(espdRequestFile);
        ESPDRequest req = BuilderFactory.withEDMVersion2().getRegulatedModelBuilder().importFrom(new FileInputStream(espdRequestFile)).createESPDRequest();
        req.setCriterionList(req.getFullCriterionList().subList(0, req.getFullCriterionList().size()/2));
        System.out.println(String.format("Criteria count before getting the unselected: %s", req.getFullCriterionList().size()));

        CriteriaService criteriaService = new PredefinedCriteriaService(EDMVersion.V2);
        req.setCriterionList(criteriaService.getUnselectedCriteria(req.getFullCriterionList()));
        System.out.println(String.format("Criteria count after getting the unselected: %s", req.getFullCriterionList().size()));
        Assert.assertEquals( criteriaService.getCriteria().size(), req.getFullCriterionList().size());
    }

}