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

import eu.esens.espdvcd.model.ESPDRequest;

/**
 * The XMLDocumentBuilder is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 *
 * @since 2.0.2
 */
public class XMLDocumentBuilderV2 extends DocumentBuilderV2 {

    ESPDRequest request;

    /**
     * Creates an XMLDocumentBuilderV2 based on {@link ESPDRequest} derived class input
     *
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     *
     */
    public XMLDocumentBuilderV2(ESPDRequest req) {
        super(req);
        this.request = req;
    }

    /**
     * Transforms the XML Representation of the data to a string.
     * @return the String representation of the XML Data
     */
    public String getAsString() {
        return theXML;
    }

    /**
     * 41 is the default value according to ESPD-EDM v2.0.1 implementation guide
     *
     * @return
     */
    @Override
    protected String getProfileID() {
        return "4.1";
    } //Changed to the ESPD validator required value

}
