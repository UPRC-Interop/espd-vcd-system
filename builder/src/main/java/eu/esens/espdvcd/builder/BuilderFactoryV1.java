package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

public class BuilderFactoryV1 {

    BuilderFactoryV1() {

    }

    /**
     * Creates a new instance of the {@link RegulatedModelBuilderV1}, which is used to
     * create ESPD and VCD model pojos from XML input.
     *
     * @return an instance of the {@link RegulatedModelBuilderV1}
     */
    public RegulatedModelBuilderV1 getRegulatedModelBuilder() {
        return new RegulatedModelBuilderV1();
    }

    /**
     * Creates a new instance of the {@link XMLDocumentBuilderV1}, which is used to
     * create ESPD and VCD XML Documents from Model POJOs.
     *
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilderV1}
     */
    public XMLDocumentBuilderV1 getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilderV1(req);
    }

}
