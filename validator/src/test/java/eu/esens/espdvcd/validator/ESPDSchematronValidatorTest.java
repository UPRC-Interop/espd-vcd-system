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

    private File validRegulatedRequestV1;
    private File invalidRegulatedRequestV1;

    private File selfContainedRequest_UPRC_DA_V210;
    private File selfContainedResponse_UPRC_DA_210;

    private File regulatedResponse_DA_210;

    public ESPDSchematronValidatorTest() {
    }

    @Before
    public void setUp() throws Exception {
        regulatedRequestV2 = new File(getClass().getClassLoader().getResource("REGULATED_ESPD_Request_2.0.2.xml").toURI());
        Assert.assertNotNull(regulatedRequestV2);

        regulatedResponseV2 = new File(getClass().getClassLoader().getResource("REGULATED_ESPD_Response_2.0.2.xml").toURI());
        Assert.assertNotNull(regulatedResponseV2);

        validRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request.xml").toURI());
        Assert.assertNotNull(validRegulatedRequestV1);

        invalidRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request-invalid.xml").toURI());
        Assert.assertNotNull(invalidRegulatedRequestV1);

        selfContainedRequest_UPRC_DA_V210 = new File(getClass().getClassLoader().getResource("xml/v2/self-contained/2.1.0/UPRC-ESPD-Self-Contained-Request-2.1.0-DA-Artefact.xml").toURI());
        Assert.assertNotNull(selfContainedRequest_UPRC_DA_V210);

        selfContainedResponse_UPRC_DA_210 = new File(getClass().getClassLoader().getResource("xml/v2/self-contained/2.1.0/UPRC-ESPD-Self-Contained-Response-2.1.0-DA-Artefact.xml").toURI());
        Assert.assertNotNull(selfContainedResponse_UPRC_DA_210);

        regulatedResponse_DA_210 = new File(getClass().getClassLoader().getResource("xml/v2/regulated/2.1.0/ESPDResponse_DA_Test-2.1.0-v0.5.xml").toURI());
        Assert.assertNotNull(regulatedResponse_DA_210);
    }

    @Test
    public void testRegulatedResponse() {
        ArtefactValidator v = ValidatorFactory.createESPDSchematronValidator(regulatedResponse_DA_210);
        Assert.assertNotNull(v);
        printErrorsIfExist(v);
        Assert.assertTrue(v.isValid());
    }

    @Test
    public void testSelfContainedDARequest() {
        ArtefactValidator v = ValidatorFactory.createESPDSchematronValidator(selfContainedRequest_UPRC_DA_V210);
        Assert.assertNotNull(v);
        printErrorsIfExist(v);
        Assert.assertTrue(v.isValid());
    }

    @Test
    public void testSelfContainedDAResponse() {
        ArtefactValidator v = ValidatorFactory.createESPDSchematronValidator(selfContainedResponse_UPRC_DA_210);
        Assert.assertNotNull(v);
        printErrorsIfExist(v);
        Assert.assertTrue(v.isValid());
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
