package eu.esens.espdvcd.validator;

import jdk.nashorn.internal.objects.NativeArray;
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
        isReqValid = ESPDRequestValidatorTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(isReqValid);

        isReqInvalid = ESPDRequestValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(isReqInvalid);

        isRes = ESPDRequestValidatorTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(isRes);
    }

    @Test
    public void validateESPDRequest() throws Exception {
        ESPDRequestValidator validator = new ESPDRequestValidator(isReqValid);
        Assert.assertTrue(validator.isValid());

        validator = new ESPDRequestValidator(isReqInvalid);
        Assert.assertFalse(validator.isValid());
        Assert.assertTrue(validator.getValidationMessages().size() > 1);
        for (String event: validator.getValidationMessages()
             ) {
            System.out.println(event);
        }
    }

}
