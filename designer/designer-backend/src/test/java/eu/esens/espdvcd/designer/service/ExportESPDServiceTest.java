package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ExportESPDServiceTest {


    File espdRequestFile;
    File espdResponseFile;
    ESPDRequest request;
    ESPDResponse response;
    ExportESPDService exportESPDService;
    ObjectWriter writer;

    @Before
    public void setUp() throws Exception {
        espdRequestFile = new File(ExportESPDServiceTest.class.getResource("/espd-request.xml").toURI());
        Assert.assertNotNull(espdRequestFile);

        espdResponseFile = new File(ExportESPDServiceTest.class.getResource("/espd-response.xml").toURI());
        Assert.assertNotNull(espdResponseFile);

        request = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom
                (ExportESPDServiceTest.class.getResourceAsStream("/espd-request.xml")).createESPDRequest();
        Assert.assertNotNull(request);

        response = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom
                (ExportESPDServiceTest.class.getResourceAsStream("/espd-response.xml")).createESPDResponse();
        Assert.assertNotNull(response);

        exportESPDService = new RegulatedExportESPDV1Service();
        writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void testCriterionNumberAfterImport() throws Exception {
        File espdRequestFile = new File(this.getClass().getClassLoader()
                .getResource("espd-response-v2-60.xml")
                .toURI());
        Assert.assertNotNull(espdRequestFile);
        ESPDRequest req = BuilderFactory
                .EDM_V2
                .createRegulatedModelBuilder()
                .importFrom(new FileInputStream(espdRequestFile))
                .createESPDRequest();

        req.setCriterionList(req.getFullCriterionList().subList(0, req.getFullCriterionList().size() / 2));
        System.out.printf("Criteria count before getting the unselected: %s \n", req.getFullCriterionList().size());

        CriteriaService criteriaService = new RetrieverCriteriaService(EDMVersion.V2);
        req.setCriterionList(criteriaService.getUnselectedCriteria(req.getFullCriterionList()));
        System.out.printf("Criteria count after getting the unselected: %s, should be: %s\n"
                ,req.getFullCriterionList().size()
                ,criteriaService.getCriteria().size());
        Assert.assertEquals(criteriaService.getCriteria(), req.getFullCriterionList());
    }

    @Test
    public void XMLStreamFromResponse() throws Exception{
        InputStream is = exportESPDService.exportESPDResponseAsInputStream(response);
        Assert.assertNotNull(is);
    }

    @Test
    public void XMLStringFromResponse() throws Exception{
        String is = exportESPDService.exportESPDResponseAsString(response);
        Assert.assertNotNull(is);
    }
}
