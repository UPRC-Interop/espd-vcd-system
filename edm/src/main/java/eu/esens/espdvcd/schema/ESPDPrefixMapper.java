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
package eu.esens.espdvcd.schema;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import eu.esens.espdvcd.schema.enums.EDMSubVersion;

/**
 * Prefix Mapper for the JAXB Classes. Used in {@link SchemaUtil} marshallers.
 * <br>
 * Uses the {@link XSD} enumeration to provide the mapping.
 */
public class ESPDPrefixMapper extends NamespacePrefixMapper {

    private EDMSubVersion version;

    public ESPDPrefixMapper(EDMSubVersion version) {
        this.version = version;
    }

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        try {
            return XSD.fromString(namespaceUri, version.getTag()).namespacePrefix();
        } catch (IllegalArgumentException ex) {
            return suggestion;
        }

    }
}
