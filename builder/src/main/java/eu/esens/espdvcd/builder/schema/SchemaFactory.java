package eu.esens.espdvcd.builder.schema;

public class SchemaFactory {

    private static final SchemaFactoryV1 schemaFactoryV1 = new SchemaFactoryV1();
    private static final SchemaFactoryV2 schemaFactoryV2 = new SchemaFactoryV2();

    public static SchemaFactoryV1 withEDM_V1() {
        return schemaFactoryV1;
    }

    public static SchemaFactoryV2 withEDM_V2() {
        return schemaFactoryV2;
    }

}
