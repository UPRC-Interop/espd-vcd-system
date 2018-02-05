package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class ESPDSchematronValidatorTest {

    InputStream aValidESPDRequest;

    @Before
    public void setUp() {
        aValidESPDRequest = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(aValidESPDRequest);
    }

    @Test
    public void testIsValid() {
        // create ESPD request validator object for valid ESPD request and retrieve test results
        ArtifactValidator validator = ValidatorFactory.createESPDRequestSchematronValidator(aValidESPDRequest, ValidatorFactory.SchOrigin.EU);
        Assert.assertTrue(validator.isValid());
    }

}
