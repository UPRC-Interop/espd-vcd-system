/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
