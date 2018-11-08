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
package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ESPDSchematronValidatorTest {

    private static final Logger LOGGER = Logger.getLogger(ESPDSchematronValidatorTest.class.getName());

    // EDM example artefacts
    private File regulatedRequestV2;
    private File regulatedResponseV2;
    private File selfContainedRequestV2;
    private File selfContainedResponseV2;

    private File validRegulatedRequestV1;
    private File invalidRegulatedRequestV1;

    private File invalidRegulatedResponseV2;
    private File invalidRegulatedResponseV2_12;
    private File invalidRegulatedResponseV2_31;
    private File invalidRegulatedResponseV2_37;
    private File invalidRegulatedResponseV2_38;
    private File invalidRegulatedResponseV2_60;

    private File invalidSelfContainedRequestV2;
    private File selfContainedRequestV2_full;

    private File invalidSelfContainedResponseV2;

    @Before
    public void setUp() {
        regulatedRequestV2 = new File(getClass().getClassLoader().getResource("REGULATED_ESPD_Request_2.0.2.xml").getFile());
        Assert.assertNotNull(regulatedRequestV2);

        regulatedResponseV2 = new File(getClass().getClassLoader().getResource("REGULATED_ESPD_Response_2.0.2.xml").getFile());
        Assert.assertNotNull(regulatedResponseV2);

        selfContainedRequestV2 = new File(getClass().getClassLoader().getResource("SELFCONTAINED-ESPD-Request_2.0.2.xml").getFile());
        Assert.assertNotNull(selfContainedRequestV2);

        selfContainedResponseV2 = new File(getClass().getClassLoader().getResource("SELFCONTAINED_ESPD_Response_2.0.2.xml").getFile());
        Assert.assertNotNull(selfContainedResponseV2);

        validRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request.xml").getFile());
        Assert.assertNotNull(validRegulatedRequestV1);

        invalidRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request-invalid.xml").getFile());
        Assert.assertNotNull(invalidRegulatedRequestV1);

        invalidRegulatedResponseV2 = new File(getClass().getClassLoader().getResource("espd-response-v2-11.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2);

        invalidRegulatedResponseV2_12 = new File(getClass().getClassLoader().getResource("espd-response-v2-12.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_12);

        invalidRegulatedResponseV2_31 = new File(getClass().getClassLoader().getResource("espd-response-v2-31.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_31);

        invalidRegulatedResponseV2_37 = new File(getClass().getClassLoader().getResource("espd-response-v2-37.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_37);

        invalidRegulatedResponseV2_38 = new File(getClass().getClassLoader().getResource("espd-response-v2-38.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_38);

        invalidRegulatedResponseV2_60 = new File(getClass().getClassLoader().getResource("espd-response-v2-60.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_60);

        invalidSelfContainedRequestV2 = new File(getClass().getClassLoader().getResource("SELFCONTAINED-ESPD-Request_2.0.2.xml").getFile());
        Assert.assertNotNull(invalidSelfContainedRequestV2);

        selfContainedRequestV2_full = new File(getClass().getClassLoader().getResource("espd-self-contained-request-full.xml").getFile());
        Assert.assertNotNull(selfContainedRequestV2_full);

        invalidSelfContainedResponseV2 = new File(getClass().getClassLoader().getResource("SELFCONTAINED_ESPD_Response_2.0.2.xml").getFile());
        Assert.assertNotNull(invalidSelfContainedResponseV2);
    }

    @Test
    public void testCreateESPDSchematronValidator() {

//        ArtefactValidator v1 = ValidatorFactory.createESPDSchematronValidator(regulatedRequestV2);
//        Assert.assertNotNull(v1);
//        printErrorsIfExist(v1);
//        Assert.assertTrue(v1.isValid());
//
//        ArtefactValidator v2 = ValidatorFactory.createESPDSchematronValidator(regulatedResponseV2);
//        Assert.assertNotNull(v2);
//        printErrorsIfExist(v2);
//        Assert.assertTrue(v2.isValid());
//
//        ArtefactValidator v3 = ValidatorFactory.createESPDSchematronValidator(selfContainedRequestV2);
//        Assert.assertNotNull(v3);
//        printErrorsIfExist(v3);
//        Assert.assertTrue(v3.isValid());
//
//        ArtefactValidator v4 = ValidatorFactory.createESPDSchematronValidator(selfContainedResponseV2);
//        Assert.assertNotNull(v4);
//        printErrorsIfExist(v4);
//        // after schematron files correction this is not valid anymore
//        Assert.assertFalse(v4.isValid());
//
//        ArtefactValidator v5 = ValidatorFactory.createESPDSchematronValidator(validRegulatedRequestV1);
//        Assert.assertNotNull(v5);
//        printErrorsIfExist(v5);
//        Assert.assertTrue(v5.isValid());
//
//        ArtefactValidator v6 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedRequestV1);
//        Assert.assertNotNull(v6);
//        printErrorsIfExist(v6);
//        Assert.assertFalse(v6.isValid());
//
//        ArtefactValidator v7 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2);
//        Assert.assertNotNull(v7);
//        printErrorsIfExist(v7);
//        Assert.assertFalse(v7.isValid());
//
//        ArtefactValidator v8 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2_12);
//        Assert.assertNotNull(v8);
//        printErrorsIfExist(v8);
//        Assert.assertFalse(v8.isValid());
//
//        ArtefactValidator v9 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2_31);
//        Assert.assertNotNull(v9);
//        printErrorsIfExist(v9);
//        Assert.assertFalse(v9.isValid());
//
//        ArtefactValidator v10 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2_37);
//        Assert.assertNotNull(v10);
//        printErrorsIfExist(v10);
//        Assert.assertFalse(v10.isValid());
//
//        ArtefactValidator v11 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2_38);
//        Assert.assertNotNull(v11);
//        printErrorsIfExist(v11);
//        Assert.assertFalse(v11.isValid());

        ArtefactValidator v12 = ValidatorFactory.createESPDSchematronValidator(invalidRegulatedResponseV2_60);
        Assert.assertNotNull(v12);
        printErrorsIfExist(v12);
        Assert.assertTrue(v12.isValid());

//        ArtefactValidator v13 = ValidatorFactory.createESPDSchematronValidator(invalidSelfContainedRequestV2);
//        Assert.assertNotNull(v13);
//        printErrorsIfExist(v13);
//        Assert.assertTrue(v13.isValid());
//
//        ArtefactValidator v14 = ValidatorFactory.createESPDSchematronValidator(invalidSelfContainedResponseV2);
//        Assert.assertNotNull(v14);
//        printErrorsIfExist(v14);
//        Assert.assertFalse(v14.isValid());
    }

    private void printErrorsIfExist(ArtefactValidator v) {
        if (!v.isValid()) {

            int index = 1;

            for (ValidationResult re : v.getValidationMessages()) {
                System.out.printf("%-3d: [%s] (%s) %s: %s => %s \n", index++, re.getFlag(), re.getId(), re.getLocation(), re.getTest(), re.getText());
            }
        }

        System.out.println("Total number of errors: " + v.getValidationMessages().stream()
                .filter(vr -> !"warning".equals(vr.getFlag()))
                .collect(Collectors.toList())
                .size());

        System.out.println("Total number of warnings: " + v.getValidationMessages().stream()
                .filter(vr -> "warning".equals(vr.getFlag()))
                .collect(Collectors.toList())
                .size());
    }

}
