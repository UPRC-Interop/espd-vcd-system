/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.ESPDRequestImpl;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static eu.esens.espdvcd.schema.EDMVersion.V2;

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

        exportESPDService = RegulatedExportESPDV1Service.getInstance();
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

        CriteriaService criteriaService = RegulatedCriteriaService.getV2Instance();
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

    @Test
    public void testExportSFC() throws Exception{
        InputStream theJson = this.getClass().getClassLoader().getResourceAsStream("requestAfterResponseDataTypeChange.json");
        ObjectMapper MAPPER = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .registerModule(new SimpleModule().addDeserializer(Requirement.class, new RequirementDeserialiser(V2)));

        ESPDRequest theRequest = MAPPER.readValue(theJson, ESPDRequestImpl.class);

        for (SelectableCriterion selectableCriterion : theRequest.getFullCriterionList()) {
            assertNotNullReqInfo(selectableCriterion.getRequirementGroups());
        }

    }

    private void assertNotNullReqInfo(List<RequirementGroup> requirementGroups) throws Exception{
        for (RequirementGroup requirementGroup : requirementGroups) {
            for (Requirement requirement : requirementGroup.getRequirements()) {
                Assert.assertNotNull(requirement.getType());
                Assert.assertNotNull(requirement.getResponseDataType());
            }
            assertNotNullReqInfo(requirementGroup.getRequirementGroups());
        }
    }
}
