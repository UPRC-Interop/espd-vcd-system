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
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

public class BuilderFactoryV1 {

    BuilderFactoryV1() {

    }

    /**
     * Creates a new instance of the {@link RegulatedModelBuilderV1}, which is used to
     * create ESPD and VCD model pojos from XML input.
     *
     * @return an instance of the {@link RegulatedModelBuilderV1}
     */
    public RegulatedModelBuilderV1 createRegulatedModelBuilder() {
        return new RegulatedModelBuilderV1();
    }

    /**
     * Creates a new instance of the {@link XMLDocumentBuilderV1}, which is used to
     * create ESPD and VCD XML Documents from Model POJOs.
     *
     * @param req an {@link ESPDRequest} implementing class or one of its subclasses
     * @return an instance of the {@link XMLDocumentBuilderV1}
     */
    public XMLDocumentBuilderV1 createXMLDocumentBuilderFor(ESPDRequest req) {
        return new XMLDocumentBuilderV1(req);
    }

    public PDFDocumentBuilderV1 createPDFDocumentBuilderFor(ESPDRequest espdRequest) {
        return new PDFDocumentBuilderV1(espdRequest);
    }

}
