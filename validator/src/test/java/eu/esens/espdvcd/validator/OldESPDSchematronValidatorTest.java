/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.validator;

//import eu.esens.espdvcd.validator.enums.SchematronOrigin;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.InputStream;
//import java.net.URISyntaxException;
//import java.nio.file.Paths;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class OldESPDSchematronValidatorTest {
//
//    // ESPD Request
//    File aValidESPDRequestFile;
//    File anInvalidESPDRequestFile;
//    InputStream aValidESPDRequest;
//    InputStream anInvalidESPDRequest;
//    // ESPD Response
//    File aValidESPDResponseFile;
//    InputStream aValidESPDResponse;
//    InputStream anInvalidESPDResponse;
//
//    @Before
//    public void setUp() {
//        aValidESPDRequest = OldESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request.xml");
//        Assert.assertNotNull(aValidESPDRequest);
//
//        try {
//            aValidESPDRequestFile = Paths.get(getClass().getClassLoader()
//                    .getResource("espd-request.xml").toURI()).toFile();
//            anInvalidESPDRequestFile = Paths.get(getClass().getClassLoader()
//                    .getResource("ESPDRequest_DA_Test.xml").toURI()).toFile();
//             aValidESPDResponseFile = Paths.get(getClass().getClassLoader()
//                     .getResource("espd-response.xml").toURI()).toFile();
//
//        } catch (URISyntaxException e) {
//            Logger.getLogger(OldESPDSchematronValidatorTest.class.getName()).log(Level.SEVERE, e.getMessage(), e);
//        }
//
//        Assert.assertNotNull(aValidESPDRequestFile);
//        Assert.assertNotNull(aValidESPDResponseFile);
//
//        anInvalidESPDRequest = OldESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
//        Assert.assertNotNull(anInvalidESPDRequest);
//
//        aValidESPDResponse = OldESPDSchematronValidatorTest.class.getResourceAsStream("/espd-response.xml");
//        Assert.assertNotNull(aValidESPDResponse);
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEHF1() {
//        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
//        ArtefactValidator validatorForValidRequest = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDRequest, "/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch");
//        if (!validatorForValidRequest.isValid()){
//            validatorForValidRequest.getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        //EHF Schematrons break the validation because of criteria taxonomy issues so we comment the assertion
//        //Assert.assertTrue(validatorForValidRequest.isValid());
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEHF1() {
//        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDResponse, "/rules/v1/ehf/ESPDResponse/EHF-ESPD-RESPONSE.sch");
//        if (!validatorForValidResponse.isValid()){
//            validatorForValidResponse.getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//      //EHF Schematrons break the validation because of criteria taxonomy issues so we comment the assertion
//      //Assert.assertTrue(validatorForValidResponse.isValid());
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEU1() {
//        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
//        ArtefactValidator validatorForValidRequest = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch");
//        if (!validatorForValidRequest.isValid()){
//            validatorForValidRequest.getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValidRequest.isValid());
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEU1() {
//        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/02-ESPD-CL-attrb-rules.sch");
//        if (!validatorForValidResponse.isValid()){
//            validatorForValidResponse
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValidResponse.isValid());
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEU2() {
//        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
//        ArtefactValidator validatorForValid2 = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch");
//        if (!validatorForValid2.isValid()){
//            validatorForValid2
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValid2.isValid());
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEU2() {
//        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/03-ESPD-ID-attrb-rules.sch");
//        if (!validatorForValidResponse.isValid()){
//            validatorForValidResponse
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValidResponse.isValid());
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEU3() {
//        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
//        ArtefactValidator validatorForValid3 = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common-BR-rules.sch");
//        if (!validatorForValid3.isValid()){
//            validatorForValid3
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValid3.isValid());
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEU3() {
//        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/04-ESPD-Common-BR-rules.sch");
//        if (!validatorForValidResponse.isValid()){
//            validatorForValidResponse
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValidResponse.isValid());
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEU4() {
//        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDArtefactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/05-ESPD-Spec-BR-rules.sch");
//        if (!validatorForValidResponse.isValid()){
//            validatorForValidResponse
//                    .getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n",re.getId(), re.getLocation(), re.getTest(), re.getText()));
//        }
//        Assert.assertTrue(validatorForValidResponse.isValid());
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEU() {
//        ArtefactValidator validatorForValidRequest = ValidatorFactory
//                .createESPDRequestSchematronValidator(aValidESPDRequestFile, SchematronOrigin.EU);
//        Assert.assertTrue(validatorForValidRequest.isValid());
//  //      Assert.assertTrue(validatorForValidRequest.getValidationMessagesFiltered("fatal").size() == 0);
//        Assert.assertTrue(validatorForValidRequest.getValidationMessages().size() == 0);
//
//        ArtefactValidator validatorForInvalidRequest = ValidatorFactory
//                .createESPDRequestSchematronValidator(anInvalidESPDRequestFile, SchematronOrigin.EU);
//        Assert.assertFalse(validatorForInvalidRequest.isValid());
//
////        Assert.assertTrue(validatorForInvalidRequest.getValidationMessagesFiltered("fatal").size() > 0);
//        Assert.assertTrue(validatorForInvalidRequest.getValidationMessages().size() > 0);
//    }
//
//    @Test
//    public void testIsESPDRequestValidForEHF() {
//        ArtefactValidator validatorForValidRequest = ValidatorFactory
//                .createESPDRequestSchematronValidator(aValidESPDRequestFile, SchematronOrigin.EHF);
//      //EHF Schematrons break the validation because of criteria taxonomy issues so we comment the assertion
////        Assert.assertTrue(validatorForValidRequest.isValid());
////        Assert.assertTrue(validatorForValidRequest.getValidationMessagesFiltered("fatal").size() == 0);
////        Assert.assertTrue(validatorForValidRequest.getValidationMessages().size() == 0);
//
//        ArtefactValidator validatorForInvalidRequest = ValidatorFactory
//                .createESPDRequestSchematronValidator(anInvalidESPDRequestFile, SchematronOrigin.EHF);
//      //EHF Schematrons break the validation because of criteria taxonomy issues so we comment the assertion
////        Assert.assertFalse(validatorForInvalidRequest.isValid());
////        Assert.assertTrue(validatorForInvalidRequest.getValidationMessagesFiltered("fatal").size() > 0);
////        Assert.assertTrue(validatorForInvalidRequest.getValidationMessages().size() > 0);
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEU() {
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDResponseSchematronValidator(aValidESPDResponseFile, SchematronOrigin.EU);
//        Assert.assertTrue(validatorForValidResponse.isValid());
//    //    Assert.assertTrue(validatorForValidResponse.getValidationMessagesFiltered("fatal").size() == 0);
//        Assert.assertTrue(validatorForValidResponse.getValidationMessages().size() == 0);
//    }
//
//    @Test
//    public void testIsESPDResponseValidForEHF() {
//        ArtefactValidator validatorForValidResponse = ValidatorFactory
//                .createESPDResponseSchematronValidator(aValidESPDResponseFile, SchematronOrigin.EHF);
//      //EHF Schematrons break the validation because of criteria taxonomy issues so we comment the assertion
////        Assert.assertTrue(validatorForValidResponse.isValid());
////        Assert.assertTrue(validatorForValidResponse.getValidationMessagesFiltered("fatal").size() == 0);
////        Assert.assertTrue(validatorForValidResponse.getValidationMessages().size() == 0);
//    }
//
//}
