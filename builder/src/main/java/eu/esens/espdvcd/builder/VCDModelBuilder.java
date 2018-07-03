package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VCDModelBuilder extends RegulatedModelBuilderV1 {

    private static final Logger LOGGER = Logger.getLogger(VCDModelBuilder.class.getName());

    @Override
    protected ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {
        ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = ArtefactUtils.getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readESPDResponseFromStream(bis);
            // Create the Model Object
            // res = ModelFactory.VCD_RESPONSE.extractESPDResponse(resType);
            res = ModelFactory.VCD_RESPONSE.extractESPDResponse(resType);
        } catch (IOException | JAXBException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }

        return res;
    }

}
