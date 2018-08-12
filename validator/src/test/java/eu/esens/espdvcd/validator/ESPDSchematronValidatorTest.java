package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ESPDSchematronValidatorTest {

    // EDM example artefacts
    private File regulatedRequestV2;
    private File regulatedResponseV2;
    private File selfContainedRequestV2;
    private File selfContainedResponseV2;

    private File validRegulatedRequestV1;
    private File invalidRegulatedRequestV1;

    private File invalidRegulatedResponseV2;

    @Before
    public void setUp() throws Exception {
        regulatedRequestV2 = new File(getClass().getClassLoader().getResource("edm/REGULATED_ESPD_Request_2.0.2.xml").getFile());
        Assert.assertNotNull(regulatedRequestV2);

        regulatedResponseV2 = new File(getClass().getClassLoader().getResource("edm/REGULATED_ESPD_Response_2.0.2.xml").getFile());
        Assert.assertNotNull(regulatedResponseV2);

        selfContainedRequestV2 = new File(getClass().getClassLoader().getResource("edm/SELFCONTAINED-ESPD-Request_2.0.2.xml").getFile());
        Assert.assertNotNull(selfContainedRequestV2);

        selfContainedResponseV2 = new File(getClass().getClassLoader().getResource("edm/SELFCONTAINED_ESPD_Response_2.0.2.xml").getFile());
        Assert.assertNotNull(selfContainedResponseV2);

        validRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request.xml").getFile());
        Assert.assertNotNull(validRegulatedRequestV1);

        invalidRegulatedRequestV1 = new File(getClass().getClassLoader().getResource("espd-request-invalid.xml").getFile());
        Assert.assertNotNull(invalidRegulatedRequestV1);

        invalidRegulatedResponseV2 = new File(getClass().getClassLoader().getResource("espd-response-v2-11.xml").getFile());
        Assert.assertNotNull(invalidRegulatedResponseV2);
    }

    @Test
    public void testCreateESPDSchematronValidator() throws IOException {

        ArtefactValidator v1 = Validators.createESPDSchematronValidator(invalidRegulatedResponseV2);
        Assert.assertNotNull(v1);

        if (!v1.isValid()) {
            v1.getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n", re.getId(), re.getLocation(), re.getTest(), re.getText()));
        }

        Assert.assertFalse(v1.isValid());

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
