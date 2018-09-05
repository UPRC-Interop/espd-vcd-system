package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.schema.EDMVersion;
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
    // private File invalidRegulatedResponseV1;

    private File invalidRegulatedResponseV2_46;

    @Before
    public void setUp() {
        validRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("ESPDRequest_DA_Test.xml").getFile());
        Assert.assertNotNull(validRegulatedRequestV1);

        invalidRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request-invalid.xml").getFile());
        Assert.assertNotNull(invalidRegulatedRequestV1);

        validRegulatedResponseV1 = new File(getClass().getClassLoader().getResource("ESPDResponse_DA_Test.xml").getFile());
        Assert.assertNotNull(validRegulatedResponseV1);

        invalidRegulatedResponseV2_46 = new File(getClass().getClassLoader().getResource("espd-response-v2-46.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2_46);
    }

    @Test
    public void validateESPDRequest() throws Exception {
        // create ESPD request validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = Validators.createESPDRequestSchemaValidator(validRegulatedRequestV1, EDMVersion.V1);

        System.out.println("validateESPDRequest events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());

        // create ESPD request validator object for invalid ESPD request and retrieve test results
        validator = Validators.createESPDRequestSchemaValidator(invalidRegulatedRequestV1, EDMVersion.V1);
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
        ArtefactValidator validator = Validators.createESPDRequestSchemaValidator(validRegulatedResponseV1, EDMVersion.V1);

        System.out.println("checkESPDRequestValidationForESPDResponse events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 0);
        Assert.assertTrue(validator.getValidationMessagesFiltered("ESPDResponse").size() > 0);
    }

    @Test
    public void validateESPDResponse() throws Exception {
        // create ESPD response validator object for valid ESPD response and retrieve test results
        ArtefactValidator validator = Validators.createESPDResponseSchemaValidator(validRegulatedResponseV1, EDMVersion.V1);

        System.out.println("validateESPDResponse events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());
    }

    @Test
    public void checkESPDResponseValidationForESPDRequest() throws Exception {
        // create ESPD response validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = Validators.createESPDResponseSchemaValidator(validRegulatedRequestV1, EDMVersion.V1);

        System.out.println("checkESPDResponseValidationForESPDRequest events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 0);
    }

    @Test
    public void testCreateESPDSchemaValidator() throws Exception {

        ArtefactValidator validator = Validators.createESPDSchemaValidator(invalidRegulatedResponseV2_46);

        System.out.println("testCreateESPDSchemaValidator events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());
        Assert.assertFalse(validator.getValidationMessages().size() > 0);
    }

}
