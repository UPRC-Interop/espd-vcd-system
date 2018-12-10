/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.schema.enums.EDMSubVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDSchemaValidatorTest {

    private File validRegulatedRequestV1;
    private File invalidRegulatedRequestV1;
    private File validRegulatedResponseV1;

    private File validSelfContainedRequestV210;

    @Before
    public void setUp() throws Exception {
        validRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("ESPDRequest_DA_Test.xml").toURI());
        Assert.assertNotNull(validRegulatedRequestV1);

        invalidRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request-invalid.xml").toURI());
        Assert.assertNotNull(invalidRegulatedRequestV1);

        validRegulatedResponseV1 = new File(getClass().getClassLoader().getResource("ESPDResponse_DA_Test.xml").toURI());
        Assert.assertNotNull(validRegulatedResponseV1);

        validSelfContainedRequestV210 = new File(getClass().getClassLoader()
                .getResource("xml/v2/self-contained/2.1.0/UPRC-ESPD-Self-Contained-Request-2.1.0-DA-Artefact-5-12-2018.xml")
                .toURI());
        Assert.assertNotNull(validSelfContainedRequestV210);
    }

    @Test
    public void validateESPDRequest() throws Exception {
        // create ESPD request validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = ValidatorFactory.createESPDRequestSchemaValidator(validRegulatedRequestV1, EDMSubVersion.V102);

        System.out.println("validateESPDRequest events:");
        for (ValidationResult event : validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());

        // create ESPD request validator object for invalid ESPD request and retrieve test results
        validator = ValidatorFactory.createESPDRequestSchemaValidator(invalidRegulatedRequestV1, EDMSubVersion.V102);
        Assert.assertFalse(validator.isValid());

        // as there are two errors introduced in the invalid espd request example xml,
        // there should be more than one messages
        Assert.assertTrue(validator.getValidationMessages().size() > 1);
        Assert.assertTrue(validator.getValidationMessagesFiltered("ContractingParty").size() > 0);
        Assert.assertTrue(validator.getValidationMessagesFiltered("RequirementGroups").size() > 0);
    }

    @Test
    public void checkESPDRequestValidationForESPDResponse() throws Exception {
        // create ESPD request validator object for valid ESPD response and retrieve test results
        ArtefactValidator validator = ValidatorFactory.createESPDRequestSchemaValidator(validRegulatedResponseV1, EDMSubVersion.V102);

        System.out.println("checkESPDRequestValidationForESPDResponse events:");
        for (ValidationResult event : validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 0);
        Assert.assertTrue(validator.getValidationMessagesFiltered("ESPDResponse").size() > 0);
    }

    @Test
    public void validateESPDResponse() throws Exception {
        // create ESPD response validator object for valid ESPD response and retrieve test results
        ArtefactValidator validator = ValidatorFactory.createESPDResponseSchemaValidator(validRegulatedResponseV1, EDMSubVersion.V102);

        System.out.println("validateESPDResponse events:");
        for (ValidationResult event : validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());
    }

    @Test
    public void checkESPDResponseValidationForESPDRequest() throws Exception {
        // create ESPD response validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = ValidatorFactory.createESPDResponseSchemaValidator(validRegulatedRequestV1, EDMSubVersion.V102);

        System.out.println("checkESPDResponseValidationForESPDRequest events:");
        for (ValidationResult event : validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 0);
    }

    @Test
    public void testCreateESPDSchemaValidator() throws Exception {

        ArtefactValidator validator = ValidatorFactory.createESPDSchemaValidator(validSelfContainedRequestV210);

        System.out.println("testCreateESPDSchemaValidator events:");
        for (ValidationResult event : validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());
        Assert.assertFalse(validator.getValidationMessages().size() > 0);
    }

}
