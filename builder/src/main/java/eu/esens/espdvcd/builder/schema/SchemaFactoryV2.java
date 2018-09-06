package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.schema.v2.ESPDRequestSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.ESPDResponseSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.VCDResponseSchemaExtractorV2;

public class SchemaFactoryV2 {

    public static final ESPDRequestSchemaExtractorV2 ESPD_REQUEST = new ESPDRequestSchemaExtractorV2();
    public static final ESPDResponseSchemaExtractorV2 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV2();
    public static final VCDResponseSchemaExtractorV2 VCD_RESPONSE = new VCDResponseSchemaExtractorV2();

}
