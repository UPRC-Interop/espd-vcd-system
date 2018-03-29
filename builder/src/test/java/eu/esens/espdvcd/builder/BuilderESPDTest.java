
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.io.InputStream;

import eu.esens.espdvcd.model.SimpleESPDRequest;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
        ESPDRequest espd = new ModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
    //    System.out.println(XML);
    }

    @Ignore    
    @Test
    public void createESPDResponseFromFile() throws Exception {
        
      
        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = new ModelBuilder().importFrom(isRes).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }
    
    @Ignore    
    @Test
    public void createESPDResponseFromESPDRequestFile() throws Exception {
        // Here we load an ESPD Request generated by the EU Service
        ESPDResponse espd = new ModelBuilder().importFrom(isReq).createESPDResponse();
        Assert.assertNotNull(espd);

        // We create an XML from our simple model 
        String XML = new XMLDocumentBuilder(espd).getAsString();
        Assert.assertNotNull(XML);
//        System.out.println(XML);
    }
    
    @Ignore
    @Test
    public void deselectExclusionCriterion() throws Exception {

        // Here we load an ESPD Request generated by the EU Service
      
        ESPDRequest originalEspd = new ModelBuilder().importFrom(isReq).createESPDRequest();
        Assert.assertNotNull(originalEspd);

        // Deselect the first exclusion criterion
        originalEspd.getExclusionCriteriaList().get(0).setSelected(false);
        
        Assert.assertEquals(62,originalEspd.getFullCriterionList().size());

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
            importedEspd = new ModelBuilder()
                    .importFrom(new XMLDocumentBuilder(originalEspd).getAsInputStream())
                    .addDefaultESPDCriteriaList()
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
    public void createSimpleESPDRequestAndApplyEcertisData() throws BuilderException, RetrieverException {

        CriteriaExtractor extractor = new ECertisCriteriaExtractor();
        ESPDRequest req = BuilderFactory.getModelBuilder().createESPDRequest();
        req.setCriterionList(extractor.getFullList());
        System.out.println(BuilderFactory.getDocumentBuilderFor(req).theXML);
    }

    @Test
    public void createESPDResponseWithAnEmptyResponse() throws Exception {
        ESPDResponse resp = BuilderFactory.getModelBuilder().createESPDResponse();
        SelectableCriterion cri = new SelectableCriterion();
        cri.setSelected(true);
        cri.setDescription("Test Empty Description");

        RequirementGroup rg = new RequirementGroup("testReq");
        cri.getRequirementGroups().add(rg);

        Requirement req = new ResponseRequirement("the ID",ResponseTypeEnum.DESCRIPTION,"Test Description");
        rg.getRequirements().add(req);
        resp.getFullCriterionList().add(cri);

        ESPDResponseType et = BuilderFactory.getDocumentBuilderFor(resp).createXML(resp);
        Assert.assertNotNull(et.getCriterion().get(0).getRequirementGroup().get(0).getRequirement().get(0).getResponse().get(0));
        Assert.assertNull(et.getCriterion().get(0).getRequirementGroup().get(0).getRequirement().get(0).getResponse().get(0).getID());
        Assert.assertNull(et.getCriterion().get(0).getRequirementGroup().get(0).getRequirement().get(0).getResponse().get(0).getDescription());
    }

}
