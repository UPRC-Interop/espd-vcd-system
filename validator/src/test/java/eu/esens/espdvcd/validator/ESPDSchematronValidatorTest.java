package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class ESPDSchematronValidatorTest {

    InputStream aValidESPDRequest;
    InputStream anInvalidESPDRequest;

    @Before
    public void setUp() {
        aValidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(aValidESPDRequest);

        anInvalidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(anInvalidESPDRequest);
    }

    @Test
    public void testIsValid() {
        // create ESPD request validator object for valid ESPD request and retrieve test results
        ArtifactValidator validatorForValid = ValidatorFactory.createESPDRequestSchematronValidator(aValidESPDRequest, ValidatorFactory.SchOrigin.EHF);
        Assert.assertTrue(validatorForValid.isValid());
    }

}
