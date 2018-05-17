package eu.esens.espdvcd.builder.schema;

public abstract class SchemaFactory {

    /**
     * @deprecated as of release 2.0.2, replaced by {@link SchemaFactoryV1#ESPD_REQUEST}
     */
    public static final ESPDRequestSchemaExtractor ESPD_REQUEST = new ESPDRequestSchemaExtractor();
    /**
     * @deprecated as of release 2.0.2, replaced by {@link SchemaFactoryV1#ESPD_RESPONSE}
     */
    public static final ESPDResponseSchemaExtractor ESPD_RESPONSE = new ESPDResponseSchemaExtractor();
    /**
     * @deprecated as of release 2.0.2, replaced by {@link SchemaFactoryV1#VCD_RESPONSE}
     */
    public static final VCDResponseSchemaExtractor VCD_RESPONSE = new VCDResponseSchemaExtractor();

    public static SchemaFactoryV1 withSchemaVersion1() {
        return new SchemaFactoryV1();
    }

    public static SchemaFactoryV2 withSchemaVersion2() {
        return new SchemaFactoryV2();
    }

}
