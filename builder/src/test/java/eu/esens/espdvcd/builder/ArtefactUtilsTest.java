package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.schema.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static eu.esens.espdvcd.builder.util.ArtefactUtils.*;

public class ArtefactUtilsTest {

    private InputStream espdRequestRegulatedV1_0_2;
    private InputStream espdRequestRegulatedV2_0_1;
    private InputStream espdResponseRegulatedV1_0_2;
    private InputStream espdResponseRegulatedV2_0_2;

    private File regulatedRequestV2;

    @Before
    public void setUp() throws Exception {
        espdRequestRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/espd-request.xml");
        Assert.assertNotNull(espdRequestRegulatedV1_0_2);
        espdRequestRegulatedV2_0_1 = ArtefactUtilsTest.class.getResourceAsStream("/espd-request-v2_2018-05-30a.xml");
        Assert.assertNotNull(espdRequestRegulatedV2_0_1);
        espdResponseRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/espd-response.xml");
        Assert.assertNotNull(espdResponseRegulatedV1_0_2);
        espdResponseRegulatedV2_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/REGULATED-ESPD-Response_2.0.2.xml");
        Assert.assertNotNull(espdResponseRegulatedV2_0_2);

        regulatedRequestV2 = new File(getClass().getClassLoader().getResource("REGULATED-ESPD-Request_2.0.2.xml").getFile());
        Assert.assertNotNull(regulatedRequestV2);
    }

    @Ignore
    @Test
    public void testFindEDMVersion() {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_1_REGULATED, findProfileExecutionID(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findProfileExecutionID(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findProfileExecutionID(espdResponseRegulatedV2_0_2));
    }

    @Ignore
    @Test
    public void testFindSchemaVersion() {
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(EDMVersion.V2, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(EDMVersion.V2, findEDMVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(espdResponseRegulatedV2_0_2));
        Assert.assertNotEquals(EDMVersion.V1, findEDMVersion(espdResponseRegulatedV2_0_2));
    }

    @Ignore
    @Test
    public void testFindArtefactType() {
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(ArtefactType.ESPD_RESPONSE, findArtefactType(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_RESPONSE, findArtefactType(espdResponseRegulatedV2_0_2));
    }

    @Ignore
    @Test
    public void testUseTwoArtefactUtilsMethodsForTheSameStreamRepeatedly() {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findProfileExecutionID(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(EDMVersion.V1, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV1_0_2));
    }

    @Ignore
    @Test
    public void testUseTwoArtefactUtilsMethodsForTheSameFileInputStreamRepeatedly() throws Exception {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findProfileExecutionID(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(EDMVersion.V2, findEDMVersion(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(new FileInputStream(regulatedRequestV2)));
    }

    @Ignore
    @Test
    public void testFindQualificationApplicationType() {
        Assert.assertEquals(QualificationApplicationTypeEnum.REGULATED, ArtefactUtils.findQualificationApplicationType(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(QualificationApplicationTypeEnum.REGULATED, ArtefactUtils.findQualificationApplicationType(regulatedRequestV2));
    }

}
