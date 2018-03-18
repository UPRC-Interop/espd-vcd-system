package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import java.io.InputStream;

public class ModelBuilderV2 {

    /* package private constructor. Create only through factory */
    ModelBuilderV2() {}

    public ModelBuilderV2 importFrom(InputStream is) {
        // code goes here
        return this;
    }

    public ModelBuilderV2 withCADetailsFrom(InputStream is) throws BuilderException {
        // code goes here
        return this;
    }

    public ESPDRequest createRegulatedESPDRequest() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDRequest createSelfContainedESPDRequest() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createRegulatedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

    public ESPDResponse createSelfContainedESPDResponse() throws BuilderException {
        throw new UnsupportedOperationException();
    }

}
