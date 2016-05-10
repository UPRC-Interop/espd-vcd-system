package eu.esens.espdvcd.schema;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 *
 * @author jerouris
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
