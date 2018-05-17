package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.schema.v1.ESPDRequestSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.ESPDResponseSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.VCDResponseSchemaExtractorV1;

public class SchemaFactoryV1 {

    SchemaFactoryV1() {

    }

    public static final ESPDRequestSchemaExtractorV1 ESPD_REQUEST = new ESPDRequestSchemaExtractorV1();
    public static final ESPDResponseSchemaExtractorV1 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV1();
    public static final VCDResponseSchemaExtractorV1 VCD_RESPONSE = new VCDResponseSchemaExtractorV1();

}
