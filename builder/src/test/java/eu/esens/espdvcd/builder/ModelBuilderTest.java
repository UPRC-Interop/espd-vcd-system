package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

public class ModelBuilderTest implements ModelBuilder {

    private InputStream espdRequestRegulatedV1_0_2 = ModelBuilderTest.class.getResourceAsStream("/espd-request.xml");
    private InputStream espdRequestRegulatedV2_0_1 = ModelBuilderTest.class.getResourceAsStream("/espd-request-v2_2018-05-30a.xml");

    private InputStream espdResponseRegulatedV1_0_2 = ModelBuilderTest.class.getResourceAsStream("/espd-response.xml");
    private InputStream espdResponseRegulatedV2_0_2 = ModelBuilderTest.class.getResourceAsStream("/REGULATED_ESPD-Response_2.0.2.xml");

    @Before
    public void setUp() {

    }

    @Ignore
    @Test
    public void testFindArtefactVersion() throws BuilderException {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_1_REGULATED, findEDMArtefactVersion(espdRequestRegulatedV2_0_1));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findEDMArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findEDMArtefactVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findEDMArtefactVersion(espdResponseRegulatedV2_0_2));
    }

}
