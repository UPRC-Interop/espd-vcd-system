/**
 * Copyright 2016-2019 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.codelist.enums.internal.DocumentType;
import eu.esens.espdvcd.model.ESPDRequest;

public class PDFDocumentBuilderV1 extends DocumentBuilderV1 {

    public PDFDocumentBuilderV1(ESPDRequest espdRequest) {
        super(espdRequest, DocumentType.PDF);
    }

    /**
     * Transforms the XML Representation of the data to a string.
     * @return the String representation of the XML Data
     */
    public String getAsString() {
        return theXML;
    }

}
