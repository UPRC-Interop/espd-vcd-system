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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetriever;
import eu.esens.espdvcd.retriever.criteria.CriteriaDataRetrieverBuilder;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetFromECertisTask;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURIBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 13/10/2020.
 * <p>
 * MD stands for Multi Domain e-Certis version.
 */
public class ECertisMDResourceTest {

    private static final Logger LOGGER = Logger.getLogger(ECertisMDResourceTest.class.getName());

    private ECertisResource eCertisResource;
    private CriteriaDataRetriever retriever;

    @Before
    public void setUp() {
        eCertisResource = new ECertisResource();
        retriever = new CriteriaDataRetrieverBuilder().build();
        Assert.assertNotNull(eCertisResource);
    }

    /**
     * Print all European criteria ID
     *
     * @throws Exception
     */
    @Test
    public void testGetAllCriteriaID() throws Exception {
        List<String> idList = eCertisResource.getAllCriteriaID();
        idList.forEach(System.out::println);
        System.out.println(idList.size());
    }

    @Test
    public void testGetEvidencesForCriterion() throws Exception {
        eCertisResource.getEvidencesForNationalCriterion("9b29b151-9102-40bd-b01c-11949abbb17e", EULanguageCodeEnum.EN)
                .forEach(e -> SelectableCriterionPrinter.printEvidence(e));

//        eCertisResource.getEvidencesForNationalCriterion("9b29b151-9102-40bd-b01c-11949abbb17e", null ,EULanguageCodeEnum.EN)
//                .forEach(e -> SelectableCriterionPrinter.printEvidence(e));

    }

    @Test
    public void testGetCriteriaNames() throws Exception {
        eCertisResource.getAllCriteriaBasicInfo().forEach(sc -> System.out.println(sc.getName()));
    }

    @Ignore
    @Test
    public void testECertisURIBuilderAPI() throws Exception {
        System.out.println(new ECertisURIBuilder()
                .lang(ECertisLanguageCodeEnum.EL)
                .buildCriteriaURI());
    }

    @Ignore
    @Test
    public void testExportECertisEUCriteriaLocally() throws Exception {
        // Creates eu criteria directory
        Files.createDirectories(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-eu"));

        // Export all European Criteria for all languages
        for (ECertisLanguageCodeEnum lang : ECertisLanguageCodeEnum.values()) {

            // if (lang == ECertisLanguageCodeEnum.NO) {

            // Create the current language directory
            Files.createDirectories(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-eu/lang/" + lang.name().toLowerCase()));

            eCertisResource.getAllCriteriaID().forEach(id -> {

                // Create the uri
                try {
                    // Create the ecertis task
                    GetFromECertisTask getFromECertisTask = new GetFromECertisTask(
                            new ECertisURIBuilder()
                                    .lang(lang)
                                    .buildCriterionURI(id));

                    ObjectMapper mapper = new ObjectMapper();
                    Object jsonObject = mapper.readValue(getFromECertisTask.call(), Object.class);
                    mapper
                            .writerWithDefaultPrettyPrinter()
//        String prettyJson = mapper.writerWithDefaultPrettyPrinter()
//                .writeValueAsString(jsonObject);
//        System.out.println(prettyJson);
                            .writeValue(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-eu/lang/" + lang.name().toLowerCase() + "/" + id + ".json").toFile(), jsonObject);

                } catch (URISyntaxException e) {
                    System.err.println(e.getMessage());
                } catch (JsonGenerationException e) {
                    System.err.println(e.getMessage());
                } catch (JsonMappingException e) {
                    System.err.println(e.getMessage());
                } catch (JsonProcessingException e) {
                    System.err.println(e.getMessage());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

            });
        }

        // }

    }

    @Ignore
    @Test
    public void testExportECertisNationalCriteriaLocally() throws Exception {

        // Creates national criteria directory
        Files.createDirectories(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-na"));

        // for all e-Certis national entities
        for (ECertisNationalEntityEnum nationalEntity : ECertisNationalEntityEnum.values()) {

            if (nationalEntity != ECertisNationalEntityEnum.GR
                    && nationalEntity != ECertisNationalEntityEnum.GB
                    && nationalEntity != ECertisNationalEntityEnum.LI
                    && nationalEntity != ECertisNationalEntityEnum.FR
                    && nationalEntity != ECertisNationalEntityEnum.ES) {

                // Create the currents national entity directory
                Files.createDirectories(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-na/nationalentity/" + nationalEntity.name().toLowerCase()));

                // for all e-Certis languages
                for (ECertisLanguageCodeEnum lang : ECertisLanguageCodeEnum.values()) {

                    // Create the currents lang directory inside the current national entity directory
                    Files.createDirectories(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-na/nationalentity/"
                            + nationalEntity.name().toLowerCase() + "/lang/" + lang.name().toLowerCase()));

                    for (String euCriterionId : eCertisResource.getAllCriteriaID()) {

                        retriever.getNationalCriterionMapping(euCriterionId, nationalEntity.name().toLowerCase()).forEach(nationalCriterion -> {


                            // Create the uri
                            try {
                                // Create the ecertis task
                                GetFromECertisTask getFromECertisTask = new GetFromECertisTask(
                                        new ECertisURIBuilder()
                                                .lang(lang)
                                                .buildCriterionURI(nationalCriterion.getID()));

                                ObjectMapper mapper = new ObjectMapper();
                                Object jsonObject = mapper.readValue(getFromECertisTask.call(), Object.class);
                                mapper
                                        .writerWithDefaultPrettyPrinter()
//        String prettyJson = mapper.writerWithDefaultPrettyPrinter()
//                .writeValueAsString(jsonObject);
//        System.out.println(prettyJson);
                                        .writeValue(Paths.get(System.getProperty("user.home") + "/ecertis/criteria-na/nationalentity/"
                                                + nationalEntity.name().toLowerCase() + "/lang/" + lang.name().toLowerCase() + "/"
                                                + nationalCriterion.getID() + ".json").toFile(), jsonObject);

                            } catch (URISyntaxException e) {
                                System.err.println(e.getMessage());
                            } catch (JsonGenerationException e) {
                                System.err.println(e.getMessage());
                            } catch (JsonMappingException e) {
                                System.err.println(e.getMessage());
                            } catch (JsonProcessingException e) {
                                System.err.println(e.getMessage());
                            } catch (IOException e) {
                                System.err.println(e.getMessage());
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                            }


                        });

                    }

                }
            }

        }

    }

}
