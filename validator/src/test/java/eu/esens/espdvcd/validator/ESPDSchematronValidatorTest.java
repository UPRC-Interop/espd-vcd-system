package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ESPDSchematronValidatorTest {

    // EDM example artefacts
    InputStream regulatedRequestV2;
    InputStream regulatedResponseV2;
    InputStream selfContainedRequestV2;
    InputStream selfContainedResponseV2;

    InputStream invalidRegulatedRequestV1;

    @Before
    public void setUp() {
        regulatedRequestV2 = ESPDSchematronValidatorTest.class.getResourceAsStream("/edm/REGULATED-ESPD-Request_2.0.2.xml");
        Assert.assertNotNull(regulatedRequestV2);

        regulatedResponseV2 = ESPDSchematronValidatorTest.class.getResourceAsStream("/edm/REGULATED-ESPD-Response_2.0.2.xml");
        Assert.assertNotNull(regulatedResponseV2);

        selfContainedRequestV2 = ESPDSchematronValidatorTest.class.getResourceAsStream("/edm/SELFCONTAINED-ESPD-Request_2.0.2.xml");
        Assert.assertNotNull(selfContainedRequestV2);

        selfContainedResponseV2 = ESPDSchematronValidatorTest.class.getResourceAsStream("/edm/SELFCONTAINED_ESPD_Response_2.0.2.xml");
        Assert.assertNotNull(selfContainedResponseV2);

        invalidRegulatedRequestV1 = ESPDSchematronValidatorTest.class.getResourceAsStream("/espd-request-invalid.xml");
        Assert.assertNotNull(invalidRegulatedRequestV1);
    }

    @Test
    public void testCreateESPDSchematronValidator() throws IOException {

        ArtefactValidator v1 = Validators.createESPDSchematronValidator(regulatedRequestV2);
        Assert.assertNotNull(v1);
        Assert.assertTrue(v1.isValid());

//        ArtefactValidator v2 = Validators.createESPDSchematronValidator(regulatedResponseV2);
//        Assert.assertNotNull(v2);
//        Assert.assertTrue(v2.isValid());
//
//        ArtefactValidator v3 = Validators.createESPDSchematronValidator(selfContainedRequestV2);
//        Assert.assertNotNull(v3);
//        Assert.assertTrue(v3.isValid());
//
//        ArtefactValidator v4 = Validators.createESPDSchematronValidator(selfContainedResponseV2);
//        Assert.assertNotNull(v4);
//        Assert.assertTrue(v4.isValid());
//
//        ArtefactValidator v5 = Validators.createESPDSchematronValidator(invalidRegulatedRequestV1);
//        Assert.assertNotNull(v5);
//        Assert.assertFalse(v5.isValid());
    }

}
