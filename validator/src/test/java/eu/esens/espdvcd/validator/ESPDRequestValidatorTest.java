package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Ulf Lotzmann on 03/05/2016.
 */
public class ESPDRequestValidatorTest {

    InputStream isReqValid;
    InputStream isReqInvalid;
    InputStream isRes;

    @Before
    public void setUp() {
        //isReqValid = BuilderESPDTest.class.getResourceAsStream("/espd-request.xml");
        isReqValid = ESPDRequestValidatorTest.class.getResourceAsStream("/espd_template_ESENS-2.xml");
        Assert.assertNotNull(isReqValid);

        isRes = ESPDRequestValidatorTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);
    }

    @Test
    public void validateESPDRequest() throws Exception {
        ESPDRequestValidator validator = new ESPDRequestValidator(isReqValid);
        Assert.assertTrue(validator.isValid());
    }

}
