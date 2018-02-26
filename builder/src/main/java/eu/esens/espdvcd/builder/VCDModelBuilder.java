package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.ESPDResponse;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VCDModelBuilder extends ModelBuilder {

    @Override
    protected ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {
        ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readESPDResponseFromStream(bis);
            // Create the Model Object
            res = ModelFactory.VCD_RESPONSE.extractESPDResponse(resType);
        } catch (IOException ex) {
            Logger.getLogger(ModelBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }

        return res;
    }
}
