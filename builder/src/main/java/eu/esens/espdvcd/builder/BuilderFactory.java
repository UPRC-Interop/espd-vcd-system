package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

public class BuilderFactory {
    
    public static ModelBuilder getModelBuilder() {
        return new ModelBuilder();
    }
    
    public static XMLDocumentBuilder getDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilder(req);
    }
    
}
