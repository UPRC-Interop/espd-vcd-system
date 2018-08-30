package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDSchemaValidatorTest {

    InputStream isReqValid;
    InputStream isReqInvalid;
    InputStream isResValid;
    InputStream isResInvalid;

    private InputStream invalidRegulatedResponseV2_46;

    @Before
    public void setUp() {
        //isReqValid = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        // isReqValid = ESPDSchemaValidatorTest.class.getResourceAsStream("/espd-request.xml");
        isReqValid = ESPDSchemaValidatorTest.class.getResourceAsStream("/ESPDRequest_DA_Test.xml");
        Assert.assertNotNull(isReqValid);

        isReqInvalid = ESPDSchemaValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(isReqInvalid);

        // isResValid = ESPDSchemaValidatorTest.class.getResourceAsStream("/espd-response.xml");
        isResValid = ESPDSchemaValidatorTest.class.getResourceAsStream("/ESPDResponse_DA_Test.xml");
        Assert.assertNotNull(isResValid);

        invalidRegulatedResponseV2_46 = ESPDSchemaValidatorTest.class.getResourceAsStream("/espd-response-v2-46.xml");
        Assert.assertNotNull(invalidRegulatedResponseV2_46);
    }

    @Test
    public void validateESPDRequest() throws Exception {
        // create ESPD request validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = Validators.createESPDRequestSchemaValidator(isReqValid, EDMVersion.V1);

        System.out.println("validateESPDRequest events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());

        // create ESPD request validator object for invalid ESPD request and retrieve test results
        validator = Validators.createESPDRequestSchemaValidator(isReqInvalid, EDMVersion.V1);
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
        ArtefactValidator validator = Validators.createESPDRequestSchemaValidator(isResValid, EDMVersion.V1);

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
        ArtefactValidator validator = Validators.createESPDResponseSchemaValidator(isResValid, EDMVersion.V1);

        System.out.println("validateESPDResponse events:");
        for (ValidationResult event: validator.getValidationMessages()) {
            System.out.println(event);
        }

        Assert.assertTrue(validator.isValid());
    }

    @Test
    public void checkESPDResponseValidationForESPDRequest() throws Exception {
        // create ESPD response validator object for valid ESPD request and retrieve test results
        ArtefactValidator validator = Validators.createESPDResponseSchemaValidator(isReqValid, EDMVersion.V1);

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

        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 0);
    }

}
