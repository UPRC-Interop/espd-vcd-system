package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * Factory class that can be used to create the available Builders both for the
 * domain model and XML Document model.
 *
 * @since 1.0
 */
public abstract class BuilderFactory {

    /**
     * Creates a new instance of the {@link OldModelBuilder}, which is used to
     * create ESPD and VCD model POJOs from XML input.
     *
     * @return an instance of the {@link OldModelBuilder}
     * @deprecated as of release 2.0.2, replaced by {@link #getRegulatedModelBuilder()}
     */
    @Deprecated
    public static OldModelBuilder getOldModelBuilder() {
        return new OldModelBuilder();
    }

    /**
     * Creates a new instance of the {@link XMLDocumentBuilderV1}, which is used to
     * create ESPD and VCD XML Documents from Model POJOs.
     *
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilderV1}
     * @deprecated as of release 2.0.2, replaced by {@link BuilderFactoryV1#getDocumentBuilderFor(ESPDRequest)}
     * and can can be called from {@link #withSchemaVersion1()}
     */
    @Deprecated
    public static XMLDocumentBuilderV1 getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilderV1(req);
    }

    /**
     * Creates a new instance of the {@link RegulatedModelBuilder}, which is used to
     * create ESPD and VCD model pojos from XML input.
     *
     * @return an instance of the {@link RegulatedModelBuilder}
     */
    public static RegulatedModelBuilder getRegulatedModelBuilder() {
        return new RegulatedModelBuilder();
    }

    public static BuilderFactoryV1 withSchemaVersion1() {
        return new BuilderFactoryV1();
    }

    public static BuilderFactoryV2 withSchemaVersion2() {
        return new BuilderFactoryV2();
    }

}
