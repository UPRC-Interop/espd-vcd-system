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
package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.schema.v2.ESPDRequestSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.ESPDResponseSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.PDFRequestSchemaExtractorV2;
import eu.esens.espdvcd.builder.schema.v2.PDFResponseSchemaExtractorV2;

public class SchemaFactoryV2 {

    public static final ESPDRequestSchemaExtractorV2 ESPD_REQUEST = new ESPDRequestSchemaExtractorV2();
    public static final ESPDResponseSchemaExtractorV2 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV2();
    public static final PDFRequestSchemaExtractorV2 ESPD_REQUEST_FOR_PDF = new PDFRequestSchemaExtractorV2();
    public static final PDFResponseSchemaExtractorV2 ESPD_RESPONSE_FOR_PDF = new PDFResponseSchemaExtractorV2();

}
