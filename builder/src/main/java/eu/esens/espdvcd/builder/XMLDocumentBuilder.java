package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.SchemaUtil;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;

/**
 * The XMLDocumentBuilder is a builder pattern implemented class that is used
 * for guided creation of XML Document Artefacts out of ESPD Model Objects.
 * 
 * @since 1.0
 */
public class XMLDocumentBuilder extends DocumentBuilder {

    /**
     * Creates an XMLDocumentBuilder based on {@link ESPDRequest} derived class input
     * 
     * @param req the {@link ESPDRequest} derived class that will be transformed
     * as an XML Document
     * 
     */
    public XMLDocumentBuilder(ESPDRequest req) {
        super(req);
    };

    /**
     * Transforms the XML Representation of the data to a string.
     * @return the String representation of the XML Data
     */
    public String getAsString() {   
        return theXML;
    }


    @java.lang.Override
    protected String getProfilID() {
        return "41";
    }


}
