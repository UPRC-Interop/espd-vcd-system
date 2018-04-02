package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.schema.v1.ESPDRequestSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.ESPDResponseSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.VCDResponseSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v2.ESPDRequestSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.ESPDResponseSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.VCDResponseSchemaExtractorV2;

public class SchemaFactory {
    
    public static final ESPDRequestSchemaExtractor ESPD_REQUEST = new ESPDRequestSchemaExtractor();
    public static final ESPDResponseSchemaExtractor ESPD_RESPONSE = new ESPDResponseSchemaExtractor();
    public static final VCDResponseSchemaExtractor VCD_RESPONSE = new VCDResponseSchemaExtractor();

    public static final class V1 {

        private V1() {}

        public static final ESPDRequestSchemaExtractorV1 ESPD_REQUEST = new ESPDRequestSchemaExtractorV1();
        public static final ESPDResponseSchemaExtractorV1 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV1();
        public static final VCDResponseSchemaExtractorV1 VCD_RESPONSE = new VCDResponseSchemaExtractorV1();

    }

    public static final class V2 {

        private V2() {}

        public static final ESPDRequestSchemaExtractorV2 ESPD_REQUEST = new ESPDRequestSchemaExtractorV2();
        public static final ESPDResponseSchemaExtractorV2 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV2();
        public static final VCDResponseSchemaExtractorV2 VCD_RESPONSE = new VCDResponseSchemaExtractorV2();

    }

}
