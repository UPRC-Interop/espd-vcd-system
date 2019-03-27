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

import eu.esens.espdvcd.builder.schema.v1.ESPDRequestSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.ESPDResponseSchemaExtractorV1;
import eu.esens.espdvcd.builder.schema.v1.VCDResponseSchemaExtractorV1;

public class SchemaFactoryV1 {

    public static final ESPDRequestSchemaExtractorV1 ESPD_REQUEST = new ESPDRequestSchemaExtractorV1();
    public static final ESPDResponseSchemaExtractorV1 ESPD_RESPONSE = new ESPDResponseSchemaExtractorV1();
    public static final VCDResponseSchemaExtractorV1 VCD_RESPONSE = new VCDResponseSchemaExtractorV1();

}
