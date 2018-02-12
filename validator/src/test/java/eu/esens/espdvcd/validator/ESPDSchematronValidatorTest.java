package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class ESPDSchematronValidatorTest {

    // ESPD Request
    InputStream aValidESPDRequest;
    InputStream anInvalidESPDRequest;
    // ESPD Response
    InputStream aValidESPDResponse;
    InputStream anInvalidESPDResponse;

    @Before
    public void setUp() {
        aValidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(aValidESPDRequest);

        anInvalidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(anInvalidESPDRequest);

        aValidESPDResponse = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(aValidESPDResponse);
    }

    @Test
    public void testIsESPDRequestValidForEHF() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValidRequest = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDRequest, "/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch");
        Assert.assertTrue(validatorForValidRequest.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEHF() {
        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDResponse, "/rules/v1/ehf/ESPDResponse/EHF-ESPD-RESPONSE.sch");
        Assert.assertTrue(validatorForValidResponse.isValid());
    }

    @Test
    public void testIsESPDRequestValidForEU1() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValidRequest = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch");
        Assert.assertTrue(validatorForValidRequest.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEU1() {
        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/02-ESPD-CL-attrb-rules.sch");
        Assert.assertTrue(validatorForValidResponse.isValid());
    }

    @Test
    public void testIsESPDRequestValidForEU2() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid2 = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch");
        Assert.assertTrue(validatorForValid2.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEU2() {
        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/03-ESPD-ID-attrb-rules.sch");
        Assert.assertTrue(validatorForValidResponse.isValid());
    }

    @Test
    public void testIsESPDRequestValidForEU3() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid3 = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common-BR-rules.sch");
        Assert.assertTrue(validatorForValid3.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEU3() {
        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/04-ESPD-Common-BR-rules.sch");
        Assert.assertTrue(validatorForValidResponse.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEU4() {
        // Create ESPD response validator object for a valid ESPD response and check if ESPD response artifact is valid
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDResponse, "/rules/v1/eu/ESPDResponse/sch/05-ESPD-Spec-BR-rules.sch");
        Assert.assertTrue(validatorForValidResponse.isValid());
    }

}
