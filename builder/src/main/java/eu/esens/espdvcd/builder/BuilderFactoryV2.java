package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

public class BuilderFactoryV2 {

    BuilderFactoryV2() {

    }

    /**
     * Creates a new instance of the {@link RegulatedModelBuilderV2}, which is used to
     * create ESPD and VCD model pojos from XML input.
     *
     * @return an instance of the {@link RegulatedModelBuilder}
     */
    public RegulatedModelBuilder getRegulatedModelBuilder() {
        return new RegulatedModelBuilderV2();
    }

    /**
     * Creates a new instance of the {@link XMLDocumentBuilderV2}, which is used to
     * create ESPD and VCD XML Documents from Model POJOs.
     *
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilderV2}
     */
    public XMLDocumentBuilderV2 getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilderV2(req);
    }

}
