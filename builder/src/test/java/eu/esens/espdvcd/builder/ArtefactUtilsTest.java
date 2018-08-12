package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.enums.ArtefactType;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.schema.SchemaVersion;
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
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_1_REGULATED, findEDMVersion(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findEDMVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMVersion(espdResponseRegulatedV2_0_2));
    }

    @Ignore
    @Test
    public void testFindSchemaVersion() {
        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdRequestRegulatedV2_0_1));
        Assert.assertEquals(SchemaVersion.V1, findSchemaVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(SchemaVersion.V2, findSchemaVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(SchemaVersion.V2, findSchemaVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(SchemaVersion.V1, findSchemaVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdResponseRegulatedV2_0_2));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdResponseRegulatedV2_0_2));
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
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findEDMVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(SchemaVersion.V1, findSchemaVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(espdRequestRegulatedV1_0_2));
    }

    @Ignore
    @Test
    public void testUseTwoArtefactUtilsMethodsForTheSameFileInputStreamRepeatedly() throws Exception {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMVersion(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(new FileInputStream(regulatedRequestV2)));
        Assert.assertEquals(ArtefactType.ESPD_REQUEST, findArtefactType(new FileInputStream(regulatedRequestV2)));
    }

}
