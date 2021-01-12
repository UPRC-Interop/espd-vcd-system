/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.filters.CriterionFilters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ECertisResourceTest {

    private static final Logger LOGGER = Logger.getLogger(ECertisResourceTest.class.getName());

    private ECertisResource eCertisResource;

    @Before
    public void setUp() {
        eCertisResource = new ECertisResource();
        Assert.assertNotNull(eCertisResource);
    }

    /**
     * Print all European criteria ID
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCriteriaID() throws Exception {
        List<String> euCriteriaIDList = eCertisResource.getAllCriteriaID();
        Assert.assertFalse(euCriteriaIDList.isEmpty());
        // Print
        // euCriteriaIDList.forEach(System.out::println);
        // System.out.println(euCriteriaIDList.size());
    }

    /**
     * Print all European criteria ID, Name, Description
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCriteriaBasicInfo() throws Exception {
        List<SelectableCriterion> basicInfoCriteriaList = eCertisResource.getAllCriteriaBasicInfo();
        Assert.assertFalse(basicInfoCriteriaList.isEmpty());

        // Check and print null values
        basicInfoCriteriaList.forEach(sc -> {
            Assert.assertNotNull(sc.getID());
            Assert.assertNotNull(sc.getName());
            Assert.assertNotNull(sc.getDescription());

            if (sc.getID().isEmpty()) {
                System.out.println("Empty ID: " + sc.getID());
            }

            if (sc.getName().isEmpty()) {
                System.out.println("Empty Name for ID: " + sc.getID());
            }

            if (sc.getDescription().isEmpty()) {
                System.out.println("Empty Description for ID: " + sc.getID());
            }

            Assert.assertFalse(sc.getID().isEmpty());
            Assert.assertFalse(sc.getName().isEmpty());
            Assert.assertFalse(sc.getDescription().isEmpty());
        });

        // Print
        // basicInfoCriteriaList.forEach(SelectableCriterionPrinter::print);
    }

    @Test
    public void testGetFullList() throws Exception {
        List<SelectableCriterion> euCriteriaList = eCertisResource.getCriterionList();
        Assert.assertFalse(euCriteriaList.isEmpty());

        // Print
        // SelectableCriterionPrinter.print(euCriteriaList);
    }

    @Test
    public void testGetEvidencesForCriterion() throws Exception {
        // r.getEvidencesForCriterion("14df34e8-15a9-411c-8c05-8c051693e277").forEach(evidence -> SelectableCriterionPrinter.printEvidence(evidence));
        Assert.assertFalse(eCertisResource.getEvidencesForNationalCriterion("a205fa3b-0719-4c8a-b09d-8f6b2cbf8bd2").isEmpty());
    }

    @Test
    public void testGetECertisCriterion() throws Exception {
        // ECertisCriterion ec = r.getECertisCriterion("7c351fc0-1fd0-4bad-bfd8-1717a9dcf9d1"); // gr national
        ECertisCriterion ec = eCertisResource.getECertisCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab");
        // ECertisCriterion ec = r.getECertisCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab");

        Assert.assertNotNull(ec);
        Assert.assertFalse(ec.getSubCriterions().isEmpty());

//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        // Print JSON String
//        String prettyCt = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ec);
//        System.out.println(prettyCt);
    }

    // @Ignore
    @Test
    public void testECertisCriterionResourceWithInitialIDList() throws Exception {

        CriteriaTaxonomyResource taxonomyResourceV2 = new RegulatedCriteriaTaxonomyResource();


        List<String> IdInitialList = taxonomyResourceV2.getCriterionList()
            .stream()
            // Remove Criteria that are not provided by e-Certis service
            .filter(CriterionFilters::isProvidedByECertis)
            .map(Criterion::getID)
            .collect(Collectors.toList());

        // the initial list
        final int initialSize = 56;
        Assert.assertEquals(initialSize, IdInitialList.size());

        ECertisResource eCertisResource = new ECertisResource(IdInitialList);

        Assert.assertTrue(eCertisResource.getCriterionList().size() > initialSize);

        // SelectableCriterionPrinter.print(eCertisResource.getCriterionList());
    }

    @Ignore
    @Test
    public void testECertisCriterionResourceWithInitialIDListIssue() throws Exception {

        CriteriaTaxonomyResource taxonomyResourceV2 = new RegulatedCriteriaTaxonomyResource();

        List<String> initialIdList = taxonomyResourceV2.getCriterionList()
            .stream()
            // Remove Criteria that are not provided by e-Certis service
            .filter(CriterionFilters::isProvidedByECertis)
            .map(Criterion::getID)
            .collect(Collectors.toList());

        // the initial list
        final int initialSize = 56;
        Assert.assertEquals(initialSize, initialIdList.size());

        ECertisResource eCertisResource = new ECertisResource(initialIdList);
        eCertisResource.getCriterionList();

        final int resultSize = 56;
        Assert.assertFalse(eCertisResource.getCriterionList().isEmpty());
        // what e-Certis actually provides
        Assert.assertEquals(resultSize, eCertisResource.getAllCriteriaID().size());
        // the result
        Assert.assertEquals(resultSize, eCertisResource.getCriterionList().size());
        //SelectableCriterionPrinter.print(eCertisResource.getCriterionList());

        Set<String> idSet = eCertisResource.getCriterionList().stream()
            .map(Criterion::getID)
            .collect(Collectors.toSet());

        Assert.assertEquals(resultSize, idSet.size());
    }

}
