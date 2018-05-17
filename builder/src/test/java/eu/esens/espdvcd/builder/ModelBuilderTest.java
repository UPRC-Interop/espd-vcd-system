package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class ModelBuilderTest implements ModelBuilder {

    private InputStream espdRequestRegulatedV1_0_2 = ModelBuilderTest.class.getResourceAsStream("/espd-request.xml");
    private InputStream espdRequestRegulatedV2_0_2 = ModelBuilderTest.class.getResourceAsStream("/REGULATED-ESPDRequest-02.00.02.xml");
    private InputStream espdRequestSelfContainedV2_0_2 = ModelBuilderTest.class.getResourceAsStream("/SELFCONTAINED-ESPDRequest-02.00.02.xml");

    private InputStream espdResponseRegulatedV1_0_2 = ModelBuilderTest.class.getResourceAsStream("/espd-response.xml");
    private InputStream espdResponseRegulatedV2_0_2 = ModelBuilderTest.class.getResourceAsStream("/REGULATED-ESPDResponse-2.0.2.xml");

    @Before
    public void setUp() {

    }

    @Test
    public void testFindArtefactVersion() throws BuilderException {
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findArtefactVersion(espdRequestRegulatedV2_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertNotEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findArtefactVersion(espdRequestRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V1_0_2, findArtefactVersion(espdResponseRegulatedV1_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_REGULATED, findArtefactVersion(espdResponseRegulatedV2_0_2));
        Assert.assertEquals(ProfileExecutionIDEnum.ESPD_EDM_V2_0_2_SELFCONTAINED, findArtefactVersion(espdRequestSelfContainedV2_0_2));
    }

}
