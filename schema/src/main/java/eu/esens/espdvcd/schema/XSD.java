package eu.esens.espdvcd.schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XSD enum used by ESPD and VCD models.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
public enum XSD {
    ESPD_REQUEST("grow.names.specification.ubl.schema.xsd.espdrequest_1", "xsd/maindoc/ESPDRequest-1.0.xsd"),
    ESPD_RESPONSE("grow.names.specification.ubl.schema.xsd.espdresponse_1", "xsd/maindoc/ESPDResponse-1.0.xsd");

        private static final Logger LOG = LoggerFactory.getLogger(XSD.class);
    private final String javaPackage;
    private final String path;

    XSD(String javaPackage, String path) {
        this.javaPackage = javaPackage;
        this.path = path;
    }

    public String javaPackage() {
        return javaPackage;
    }

    public String path() {
        return path;
    }

    public static void main(String argv[]) {
        LOG.info("Testing Log4J2 logger with SLF4J facade.");
        LOG.info(ESPD_REQUEST.path());
    }
}


