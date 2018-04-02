package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * Factory class that can be used to create the available Builders both for the
 * domain model and XML Document model.
 *
 * @since 1.0
 */
public class BuilderFactory {

    /**
     * Creates a new instance of the {@link ModelBuilderV1}, which is used to
     * create ESPD and VCD model POJOs from XML input.
     *
     * @return an instance of the {@link ModelBuilderV1}
     * @deprecated as of release 2.0.2, replaced by {@link V1#getModelBuilder()}
     */
    @Deprecated
    public static ModelBuilderV1 getModelBuilder() {
        return new ModelBuilderV1();
    }

    /**
     * Creates a new instance of the {@link XMLDocumentBuilderV1}, which is used to
     * create ESPD and VCD XML Documents from Model POJOs.
     *
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilderV1}
     * @deprecated as of release 2.0.2, replaced by {@link V1#getDocumentBuilderFor(ESPDRequest)}
     */
    @Deprecated
    public static XMLDocumentBuilderV1 getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilderV1(req);
    }

    public static final class V1 {

        private V1() {}

        /**
         * Creates a new instance of the {@link ModelBuilderV1}, which is used to
         * create ESPD and VCD model pojos from XML input.
         *
         * @return an instance of the {@link ModelBuilderV1}
         */
        public static ModelBuilderV1 getModelBuilder() {
            return new ModelBuilderV1();
        }

        /**
         * Creates a new instance of the {@link XMLDocumentBuilderV1}, which is used to
         * create ESPD and VCD XML Documents from Model POJOs.
         *
         * @param req an {@link ESPDRequest} implementing class or one of its subclasses
         * @return an instance of the {@link XMLDocumentBuilderV1}
         */
        public static XMLDocumentBuilderV1 getDocumentBuilderFor(ESPDRequest req) {
            return new XMLDocumentBuilderV1(req);
        }

    }

    public static final class V2 {

        private V2() {}

        /**
         * Creates a new instance of the {@link ModelBuilderV2}, which is used to
         * create ESPD and VCD model pojos from XML input.
         *
         * @return an instance of the {@link ModelBuilderV2}
         */
        public static ModelBuilderV2 getModelBuilder() {
            return new ModelBuilderV2();
        }

        /**
         * Creates a new instance of the {@link XMLDocumentBuilderV2}, which is used to
         * create ESPD and VCD XML Documents from Model POJOs.
         *
         * @param req an {@link ESPDRequest} implementing class or one of its subclasses
         * @return an instance of the {@link XMLDocumentBuilderV2}
         */
        public static XMLDocumentBuilderV2 getDocumentBuilderFor(ESPDRequest req) {
            // return new XMLDocumentBuilderV2(req);
            throw new UnsupportedOperationException("Error... XML document builder v2 not yet implemented...");
        }

    }

}
