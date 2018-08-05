package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.esens.espdvcd.validator.ValidationResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class RegulatedModeltoESPDRequestV2ServiceTest {

    @Test
    public void testCriterionNumberAfterImport() throws Exception{
        File espdRequestFile = new File(RegulatedModeltoESPDRequestV2ServiceTest.class.getResource("/REGULATED-ESPD-Request_2.0.2-1.xml").toURI());
        Assert.assertNotNull(espdRequestFile);
        ESPDRequest req = BuilderFactory.withEDMVersion2().getRegulatedModelBuilder().importFrom(new FileInputStream(espdRequestFile)).createESPDRequest();
        CriteriaService criteriaService = new PredefinedCriteriaService(SchemaVersion.V2);
        req.setCriterionList(criteriaService.getUnselectedCriteria(req.getFullCriterionList()));
        Assert.assertEquals( 63, req.getFullCriterionList().size());
    }

}