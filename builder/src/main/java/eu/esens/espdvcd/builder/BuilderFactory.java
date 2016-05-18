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
     * Creates a new instance of the {@link ModelBuilder}, which is used to 
     * create ESPD and VCD model pojos from XML input.
     * @return an instance of the {@link ModelBuilder} 
     */
    
    public static ModelBuilder getModelBuilder() {
        return new ModelBuilder();
    }
    
    /**
     * Creates a new instance of the {@link XMLDocumentBuilder}, which is used to 
     * create ESPD and VCD XML Documents from Model POJOs.
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilder}
     */
    public static XMLDocumentBuilder getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilder(req);
    }
    
}
