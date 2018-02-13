package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.validator.schematron.SchematronOrigin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ESPDSchematronValidatorTest {

    // ESPD Request
    File aValidESPDRequestFile;
    File anInvalidESPDRequestFile;
    InputStream aValidESPDRequest;
    InputStream anInvalidESPDRequest;
    // ESPD Response
    File aValidESPDResponseFile;
    InputStream aValidESPDResponse;
    InputStream anInvalidESPDResponse;

    @Before
    public void setUp() {
        aValidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(aValidESPDRequest);

        try {
            aValidESPDRequestFile = Paths.get(getClass().getClassLoader()
                    .getResource("espd-request.xml").toURI()).toFile();
            anInvalidESPDRequestFile = Paths.get(getClass().getClassLoader()
                    .getResource("espd-request-invalid.xml").toURI()).toFile();
            aValidESPDResponseFile = Paths.get(getClass().getClassLoader()
                    .getResource("espd-response.xml").toURI()).toFile();

        } catch (URISyntaxException e) {
            Logger.getLogger(ESPDSchematronValidatorTest.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

        Assert.assertNotNull(aValidESPDRequestFile);
        Assert.assertNotNull(aValidESPDResponseFile);

        anInvalidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(anInvalidESPDRequest);

        aValidESPDResponse = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(aValidESPDResponse);
    }

    @Test
    public void testIsESPDRequestValidForEHF1() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValidRequest = ValidatorFactory
                .createESPDArtifactSchematronValidator(aValidESPDRequest, "/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch");
        Assert.assertTrue(validatorForValidRequest.isValid());
    }

    @Test
    public void testIsESPDResponseValidForEHF1() {
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

    @Test
    public void testIsESPDRequestValidForEU() {
        ArtifactValidator validatorForValidRequest = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequestFile, SchematronOrigin.EU);
        Assert.assertTrue(validatorForValidRequest.isValid());
        Assert.assertTrue(validatorForValidRequest.getValidationMessagesFiltered("fatal").size() == 0);
        Assert.assertTrue(validatorForValidRequest.getValidationMessages().size() == 0);

        ArtifactValidator validatorForInvalidRequest = ValidatorFactory
                .createESPDRequestSchematronValidator(anInvalidESPDRequestFile, SchematronOrigin.EU);
        Assert.assertFalse(validatorForInvalidRequest.isValid());
        Assert.assertTrue(validatorForInvalidRequest.getValidationMessagesFiltered("fatal").size() > 0);
        Assert.assertTrue(validatorForInvalidRequest.getValidationMessages().size() > 0);
    }

    @Test
    public void testIsESPDRequestValidForEHF() {
        ArtifactValidator validatorForValidRequest = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequestFile, SchematronOrigin.EHF);
        Assert.assertTrue(validatorForValidRequest.isValid());
        Assert.assertTrue(validatorForValidRequest.getValidationMessagesFiltered("fatal").size() == 0);
        Assert.assertTrue(validatorForValidRequest.getValidationMessages().size() == 0);

        ArtifactValidator validatorForInvalidRequest = ValidatorFactory
                .createESPDRequestSchematronValidator(anInvalidESPDRequestFile, SchematronOrigin.EHF);
        Assert.assertFalse(validatorForInvalidRequest.isValid());
        Assert.assertTrue(validatorForInvalidRequest.getValidationMessagesFiltered("fatal").size() > 0);
        Assert.assertTrue(validatorForInvalidRequest.getValidationMessages().size() > 0);
    }

    @Test
    public void testIsESPDResponseValidForEU() {
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDResponseSchematronValidator(aValidESPDResponseFile, SchematronOrigin.EU);
        Assert.assertTrue(validatorForValidResponse.isValid());
        Assert.assertTrue(validatorForValidResponse.getValidationMessagesFiltered("fatal").size() == 0);
        Assert.assertTrue(validatorForValidResponse.getValidationMessages().size() == 0);
    }

    @Test
    public void testIsESPDResponseValidForEHF() {
        ArtifactValidator validatorForValidResponse = ValidatorFactory
                .createESPDResponseSchematronValidator(aValidESPDResponseFile, SchematronOrigin.EHF);
        Assert.assertTrue(validatorForValidResponse.isValid());
        Assert.assertTrue(validatorForValidResponse.getValidationMessagesFiltered("fatal").size() == 0);
        Assert.assertTrue(validatorForValidResponse.getValidationMessages().size() == 0);
    }

}
