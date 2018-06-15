package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * Factory class that can be used to create the available Builders both for the
 * domain model and XML Document model.
 *
 * @since 1.0
 */
public abstract class BuilderFactory {

    public static BuilderFactoryV1 withSchemaVersion1() {
        return new BuilderFactoryV1();
    }

    public static BuilderFactoryV2 withSchemaVersion2() {
        return new BuilderFactoryV2();
    }

}
