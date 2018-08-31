package eu.esens.espdvcd.builder;

/**
 * Factory class that can be used to create the available Builder
 * Factories for both exchange data model (EDM) versions.
 *
 * @since 2.0.2
 */
public class BuilderFactory {

    public static final BuilderFactoryV1 EDM_V1 = new BuilderFactoryV1();

    public static final BuilderFactoryV2 EDM_V2 = new BuilderFactoryV2();

}
