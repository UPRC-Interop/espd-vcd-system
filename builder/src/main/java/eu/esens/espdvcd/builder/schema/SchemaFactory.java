package eu.esens.espdvcd.builder.schema;

public abstract class SchemaFactory {

    public static SchemaFactoryV1 withSchemaVersion1() {
        return new SchemaFactoryV1();
    }

    public static SchemaFactoryV2 withSchemaVersion2() {
        return new SchemaFactoryV2();
    }

}
