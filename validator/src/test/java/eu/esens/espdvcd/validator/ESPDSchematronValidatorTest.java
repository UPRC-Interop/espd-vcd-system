package eu.esens.espdvcd.validator;

import eu.esens.espdvcd.validator.schematron.SchOrigin;
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
    public void testIsValidForEHF() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequest, "/rules/v1/ehf/ESPDRequest/EHF-ESPD-REQUEST.sch", SchOrigin.EHF);
        Assert.assertTrue(validatorForValid.isValid());
    }

    @Test
    public void testIsValidForEU1() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/02-ESPD-CL-attrb-rules.sch", SchOrigin.EU);
        Assert.assertTrue(validatorForValid.isValid());
    }

    @Test
    public void testIsValidForEU2() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid2 = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/03-ESPD-ID-attrb-rules.sch", SchOrigin.EU);
        Assert.assertTrue(validatorForValid2.isValid());
    }

    @Test
    public void testIsValidForEU3() {
        // create ESPD request validator object for valid ESPD request and check if ESPD Request artifact is valid
        ArtifactValidator validatorForValid3 = ValidatorFactory
                .createESPDRequestSchematronValidator(aValidESPDRequest, "/rules/v1/eu/ESPDRequest/sch/04-ESPD-Common BR-rules.sch", SchOrigin.EU);
        Assert.assertTrue(validatorForValid3.isValid());
    }

}
