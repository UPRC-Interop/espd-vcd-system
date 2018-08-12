package eu.esens.espdvcd.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.logging.Logger;

public class ESPDSchematronValidatorTest {

    private static final Logger LOGGER = Logger.getLogger(ESPDSchematronValidatorTest.class.getName());

    // EDM example artefacts
    private File regulatedRequestV2;
    private File regulatedResponseV2;
    private File selfContainedRequestV2;
    private File selfContainedResponseV2;

    private File validRegulatedRequestV1;
    private File invalidRegulatedRequestV1;

    private File invalidRegulatedResponseV2;

    @Before
    public void setUp() {
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
    public void testCreateESPDSchematronValidator() {

        ArtefactValidator v1 = Validators.createESPDSchematronValidator(regulatedRequestV2);
        Assert.assertNotNull(v1);
        printErrorsIfExist(v1);
        Assert.assertTrue(v1.isValid());

        ArtefactValidator v2 = Validators.createESPDSchematronValidator(regulatedResponseV2);
        Assert.assertNotNull(v2);
        printErrorsIfExist(v2);
        Assert.assertTrue(v2.isValid());

        ArtefactValidator v3 = Validators.createESPDSchematronValidator(selfContainedRequestV2);
        Assert.assertNotNull(v3);
        printErrorsIfExist(v3);
        Assert.assertTrue(v3.isValid());

        ArtefactValidator v4 = Validators.createESPDSchematronValidator(selfContainedResponseV2);
        Assert.assertNotNull(v4);
        printErrorsIfExist(v4);
        Assert.assertFalse(v4.isValid());

        ArtefactValidator v5 = Validators.createESPDSchematronValidator(validRegulatedRequestV1);
        Assert.assertNotNull(v5);
        printErrorsIfExist(v5);
        Assert.assertTrue(v5.isValid());

        ArtefactValidator v6 = Validators.createESPDSchematronValidator(invalidRegulatedRequestV1);
        Assert.assertNotNull(v6);
        printErrorsIfExist(v6);
        Assert.assertFalse(v6.isValid());

        ArtefactValidator v7 = Validators.createESPDSchematronValidator(invalidRegulatedResponseV2);
        Assert.assertNotNull(v7);
        printErrorsIfExist(v7);
        Assert.assertFalse(v7.isValid());
    }

    private void printErrorsIfExist(ArtefactValidator v) {
        if (!v.isValid()) {
            v.getValidationMessages().forEach(re -> System.out.printf("(%s) %s: %s => %s \n", re.getId(), re.getLocation(), re.getTest(), re.getText()));
        }
    }

}
