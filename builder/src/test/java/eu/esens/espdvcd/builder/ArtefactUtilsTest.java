package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static eu.esens.espdvcd.builder.util.ArtefactUtils.*;

public class ArtefactUtilsTest {

    private InputStream espdRequestRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/espd-request.xml");
    private InputStream espdRequestRegulatedV2_0_1 = ArtefactUtilsTest.class.getResourceAsStream("/espd-request-v2_2018-05-30a.xml");

    private InputStream espdResponseRegulatedV1_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/espd-response.xml");
    private InputStream espdResponseRegulatedV2_0_2 = ArtefactUtilsTest.class.getResourceAsStream("/REGULATED_ESPD-Response_2.0.2.xml");

    @Before
    public void setUp() {

    }

    @Ignore
    @Test
    public void testFindEDMtVersion() {
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

}
