package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

public class BuilderESPDTest {

    InputStream isReq;
    InputStream isRes;

    public BuilderESPDTest() {
    }

    @Before
    public void setUp() {
        isReq = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(isReq);

        isRes = BuilderESPDTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);
    }

    @Ignore
    @Test
    public void createESPDRequestFromFile() throws Exception {

        // Here we load an ESPD Request generated by the EU Service
        ESPDRequest espd = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilderV1(espd).getAsString();
        Assert.assertNotNull(XML);
        //    System.out.println(XML);
    }

    @Ignore
    @Test
    public void createESPDResponseFromFile() throws Exception {


        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().importFrom(isRes).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilderV1(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }

    @Ignore
    @Test
    public void createESPDResponseFromESPDRequestFile() throws Exception {
        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().importFrom(isReq).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilderV1(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }

    @Ignore
    @Test
    public void deselectExclusionCriterion() throws Exception {

        // Here we load an ESPD Request generated by the EU Service

        ESPDRequest originalEspd = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(originalEspd);

        // Deselect the first exclusion criterion
        originalEspd.getExclusionCriteriaList().get(0).setSelected(false);

        Assert.assertEquals(62, originalEspd.getFullCriterionList().size());

        // Count the number of selected exclusion criteria and the total number of exclusion criteria in the original espd request
        int originalEspdExclusionCriteriaCount = originalEspd.getExclusionCriteriaList().size();
        long originalCriteriaSelectedCount = originalEspd.getFullCriterionList().stream()
                .filter(c -> c.isSelected())
                .count();

        long originalCriteriaNotSelectedCount = originalEspd.getFullCriterionList().stream()
                .filter(c -> !c.isSelected())
                .count();

        Assert.assertEquals(62, originalCriteriaNotSelectedCount + originalCriteriaSelectedCount);
        Assert.assertEquals(1, originalCriteriaNotSelectedCount);
        Assert.assertEquals(61, originalCriteriaSelectedCount);

        // Export the ESPD Request to Xml string

        ESPDRequest importedEspd;
        importedEspd = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder()
                .importFrom(new XMLDocumentBuilderV1(originalEspd).getAsInputStream())
                // .addDefaultESPDCriteriaList()
                .createESPDRequest();

        Assert.assertNotNull(importedEspd);

        // Count the number of selected exclusion criteria and the total number of exclusion criteria in the imported espd request
        long importedEspdExclusionCriteriaCount = importedEspd.getExclusionCriteriaList().size();
        long importedEspdExclusionCriteriaSelectedCount = importedEspd
                .getExclusionCriteriaList()
                .stream()
                .filter(c -> c.isSelected())
                .count();

        // Compare the number of selected exclusion criteria in espd and importedEspd
        Assert.assertEquals(originalEspdExclusionCriteriaCount, importedEspdExclusionCriteriaCount);
        Assert.assertNotEquals(originalCriteriaSelectedCount, importedEspdExclusionCriteriaSelectedCount);
    }

    @Test
    public void createESPDResponseWithAnEmptyResponse() throws Exception {
        ESPDResponse resp = BuilderFactory.withEDMVersion1().getRegulatedModelBuilder().createESPDResponse();
        SelectableCriterion cri = new SelectableCriterion();
        cri.setSelected(true);
        cri.setDescription("Test Empty Description");

        RequirementGroup rg = new RequirementGroup("testReq");
        cri.getRequirementGroups().add(rg);

        Requirement req = new ResponseRequirement("the ID", ResponseTypeEnum.DESCRIPTION, "Test Description");
        rg.getRequirements().add(req);
        resp.getFullCriterionList().add(cri);

        Requirement req2 = new ResponseRequirement("the ID", ResponseTypeEnum.PERIOD, "Test Period");
        rg.getRequirements().add(req2);

        Requirement req3 = new ResponseRequirement("the ID", ResponseTypeEnum.EVIDENCE_URL, "Test Evidence");
        rg.getRequirements().add(req3);

        Requirement req4 = new ResponseRequirement("the ID", ResponseTypeEnum.CODE, "Test Code");
        rg.getRequirements().add(req4);

        Requirement req5 = new ResponseRequirement("the ID", ResponseTypeEnum.AMOUNT, "Test Amount");
        rg.getRequirements().add(req5);

        Requirement req6 = new ResponseRequirement("the ID", ResponseTypeEnum.QUANTITY_YEAR, "Test Year");
        rg.getRequirements().add(req6);

        Requirement req7 = new ResponseRequirement("the ID", ResponseTypeEnum.DATE, "Test Date");
        rg.getRequirements().add(req7);

        Requirement req8 = new ResponseRequirement("the ID", ResponseTypeEnum.CODE_COUNTRY, "Test Country");
        rg.getRequirements().add(req8);

        resp.getFullCriterionList().add(cri);

        ESPDResponseType et = BuilderFactory.withEDMVersion1().getDocumentBuilderFor(resp).createXML(resp);
        et.getCriterion().get(0).getRequirementGroup().get(0).getRequirement().forEach(r -> {
            Assert.assertNotNull(r.getResponse().get(0));
            Assert.assertNull(r.getResponse().get(0).getID());
            Assert.assertNull(r.getResponse().get(0).getDescription());
        });
    }

    @Test
    public void createRegulatedResponseV2FromAnImportedV2Response() throws Exception {

        ESPDResponse espdResponse = BuilderFactory.withEDMVersion2()
                .getRegulatedModelBuilder()
                .importFrom(BuilderESPDTest.class.getResourceAsStream("/REGULATED-ESPD-Response_2.0.2.xml"))
                .createESPDResponse();

        XMLDocumentBuilderV2 xmlDocumentBuilderV2 = BuilderFactory.withEDMVersion2().getDocumentBuilderFor(espdResponse);
        System.out.println(xmlDocumentBuilderV2.theXML);
    }

}
