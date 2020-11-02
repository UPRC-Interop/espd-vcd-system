/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import javax.xml.transform.stream.StreamSource;

public class TransformationHelper {

    /**
     * Creates an ESPD Request model version 2 from an XML document
     * @param xmlSource is the ESPD Request version 2 XML document
     * @return eu.esens.espdvcd.model.ESPDRequest
     * @throws BuilderException
     */
    public ESPDRequest getESPDRequestV2(StreamSource xmlSource) throws BuilderException {
        return BuilderFactory.EDM_V2.
                createRegulatedModelBuilder().
                importFrom(xmlSource.getInputStream()).
                createESPDRequest();
    }

    /**
     * Creates an ESPD Response model version 2 from an XML document
     * @param xmlSource is the ESPD Response version 2 XML document
     * @return eu.esens.espdvcd.model.ESPDResponse
     * @throws BuilderException
     */
    public ESPDResponse getESPDResponseV2(StreamSource xmlSource) throws BuilderException {
        return BuilderFactory.EDM_V2.
                createRegulatedModelBuilder().
                importFrom(xmlSource.getInputStream()).
                createESPDResponse();
    }

    /**
     * Creates an ESPD Response model version 1 from an XML document
     * @param xmlSource is the ESPD Response version 1 XML document
     * @return eu.esens.espdvcd.model.ESPDResponse
     * @throws BuilderException
     */
    public ESPDResponse getESPDResponseV1(StreamSource xmlSource) throws BuilderException {
        return BuilderFactory.EDM_V1.
                createRegulatedModelBuilder().
                importFrom(xmlSource.getInputStream()).
                createESPDResponse();
    }

    /**
     * Creates an ESPD Request model version 1 from an XML document
     * @param xmlSource is the ESPD Request version 1 XML document
     * @return eu.esens.espdvcd.model.ESPDRequest
     * @throws BuilderException
     */
    public ESPDRequest getESPDRequestV1(StreamSource xmlSource) throws BuilderException {
        return BuilderFactory.EDM_V1.
                createRegulatedModelBuilder().
                importFrom(xmlSource.getInputStream()).
                createESPDRequest();
    }

}