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
