package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.schema.SchemaVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class ModelBuilderTest implements ModelBuilder {

    private InputStream espdRequestRegulatedV1 = ModelBuilderTest.class.getResourceAsStream("/espd-request.xml");
    private InputStream espdRequestRegulatedV2 = ModelBuilderTest.class.getResourceAsStream("/REGULATED-ESPDRequest-02.00.02.xml");
    private InputStream espdRequestSelfContained = ModelBuilderTest.class.getResourceAsStream("/SELFCONTAINED-ESPDRequest-02.00.01.xml");

    private InputStream espdResponseRegulatedV1 = ModelBuilderTest.class.getResourceAsStream("/espd-response.xml");
    private InputStream espdResponseRegulatedV2 = ModelBuilderTest.class.getResourceAsStream("/REGULATED-ESPDResponse-02.00.01.xml");
    private InputStream espdResponseSelfContained = ModelBuilderTest.class.getResourceAsStream("/SELFCONTAINED-ESPDResponse-02.00.01.xml");

    @Before
    public void setUp() {

    }

    @Test
    public void testFindSchemaVersion() {
        Assert.assertEquals(SchemaVersion.V1, findSchemaVersion(espdRequestRegulatedV1));
        Assert.assertNotEquals(SchemaVersion.V2, findSchemaVersion(espdRequestRegulatedV1));

        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdRequestRegulatedV2));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdRequestRegulatedV2));

        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdRequestSelfContained));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdRequestSelfContained));

        Assert.assertEquals(SchemaVersion.V1, findSchemaVersion(espdResponseRegulatedV1));
        Assert.assertNotEquals(SchemaVersion.V2, findSchemaVersion(espdResponseRegulatedV1));

        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdResponseRegulatedV2));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdResponseRegulatedV2));

        Assert.assertEquals(SchemaVersion.V2, findSchemaVersion(espdResponseSelfContained));
        Assert.assertNotEquals(SchemaVersion.V1, findSchemaVersion(espdResponseSelfContained));
    }

}
