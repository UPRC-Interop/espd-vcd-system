package eu.esens.espdvcd.builder.schema;

public class SchemaFactory {
    
    public static final ESPDRequestSchemaExtractor ESPD_REQUEST = new ESPDRequestSchemaExtractor();
    public static final ESPDResponseSchemaExtractor ESPD_RESPONSE = new ESPDResponseSchemaExtractor();
    public static final VCDResponseSchemaExtractor VCD_RESPONSE = new VCDResponseSchemaExtractor();

}
