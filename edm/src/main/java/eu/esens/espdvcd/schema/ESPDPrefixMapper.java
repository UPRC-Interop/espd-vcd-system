/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
package eu.esens.espdvcd.schema;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Prefix Mapper for the JAXB Classes. Used in {@link SchemaUtil} marshallers.
 * <br>
 * Uses the {@link XSD} enumeration to provide the mapping.
 */
public class ESPDPrefixMapper extends NamespacePrefixMapper {

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        try {
            return XSD.fromString(namespaceUri).namespacePrefix();
        } catch (IllegalArgumentException ex) {
            return suggestion;
        }

    }
}
