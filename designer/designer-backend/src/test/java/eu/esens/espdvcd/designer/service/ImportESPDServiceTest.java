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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ImportESPDServiceTest {
    File espdResponse;
    File espdRequestFile;
    File espdResponseFile;
    ESPDRequest request;
    ESPDResponse response;
    ESPDRequest selfContainedRequest;
    ExportESPDService exportESPDService;
    ImportESPDService importESPDService;
    ObjectWriter writer;

    @Before
    public void setUp() throws Exception {
        espdRequestFile = new File(ImportESPDServiceTest.class.getResource("/espd-request.xml").toURI());
        Assert.assertNotNull(espdRequestFile);

        espdResponseFile = new File(ImportESPDServiceTest.class.getResource("/espd-response.xml").toURI());
        Assert.assertNotNull(espdResponseFile);
        espdResponse = new File(this.getClass().getClassLoader().getResource("espd-response-v2-60.xml").toURI());

//        request = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom
//                (ImportESPDServiceTest.class.getResourceAsStream("/espd-request.xml")).createESPDRequest();
//        Assert.assertNotNull(request);

//        response = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom
//                (ImportESPDServiceTest.class.getResourceAsStream("/espd-response.xml")).createESPDResponse();
//        Assert.assertNotNull(response);

//        selfContainedRequest = BuilderFactory.EDM_V2.createSelfContainedModelBuilder().importFrom
//                (ImportESPDServiceTest.class.getResourceAsStream("SELFCONTAINED-ESPD-Request_2.0.2_weights.xml")).createESPDRequest();

        exportESPDService = RegulatedExportESPDV1Service.getInstance();
        importESPDService = ImportESPDResponseService.getInstance();
        writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @Test
    public void testQuantityIntegerImport() throws Exception {
        ImportESPDService service = ImportESPDResponseService.getInstance();
        Assert.assertNotNull(espdResponse);
        ESPDResponse theResponse = (ESPDResponse) service.importESPDFile(espdResponse);

        List<Requirement> requirementList = theResponse.getFullCriterionList().stream()
                .filter(cr -> cr.getID().equals("b16cb9fc-6cb7-4585-9302-9533b415cf48"))
                .findFirst()
                .get()
                .getRequirementGroups()
                .stream()
                .filter(rg -> rg.getID().equals("e1886054-ada4-473c-9afc-2fde82c24cf4"))
                .findFirst()
                .get()
                .getRequirements()
                .stream()
                .filter(rq -> rq.getID().equals("6c14dfbf-1450-4bd7-b324-5cc70acf4f34")).collect(Collectors.toList());

        Assert.assertNotNull(requirementList.get(0).getResponse());
    }

    @Test
    public void generateRequirementGroupStructure() throws Exception {
        CriteriaService service = RegulatedCriteriaService.getV1Instance();

        SelectableCriterion criterion = service.getCriteria().stream()
                .filter(cr -> cr.getID().equals("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab"))
                .findFirst()
                .get();

        criterion.getRequirementGroups().get(0).getRequirementGroups().add(criterion.getRequirementGroups().get(0).getRequirementGroups().get(0));
        System.out.println(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(criterion));
    }

    @Test
    public void responseJSONFromRequestTest() throws Exception {
        ESPDResponse response = (ESPDResponse) importESPDService.importESPDFile(espdRequestFile);
        Assert.assertNotNull(response);
        String exportedString = exportESPDService.exportESPDRequestAsString(response);
        Assert.assertNotNull(exportedString);
        System.out.println(exportedString);
    }

    @Test
    public void responseJSONFromResponseTest() throws Exception {
        ESPDResponse response = (ESPDResponse) importESPDService.importESPDFile(espdResponseFile);
        Assert.assertNotNull(response);
    }

    @Test
    public void requestSelfContainedImport() throws Exception {
        ESPDRequest request = ImportESPDRequestService.getInstance().importESPDFile(new File(this.getClass().getClassLoader().getResource("SELFCONTAINED-ESPD-Request_2.0.2_weights.xml").toURI()));

        System.out.println(writer.writeValueAsString(request));
    }
}
