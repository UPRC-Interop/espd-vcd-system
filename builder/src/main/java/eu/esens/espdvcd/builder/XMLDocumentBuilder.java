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

public class XMLDocumentBuilder {
    
    private final String theXML;
    
    public XMLDocumentBuilder(ESPDRequest req) {
        theXML = createXMLasString(req);
    };
        
    public InputStream getAsInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream(theXML.getBytes(Charset.forName("UTF-8")));
        return bis;
    }
    
    public String getAsString() {   
        return theXML;
    }
    
     /**
     *
     * @param req The ESPDRequest Model instance to be transformed to XML
     * @return a JAXB ESPDRequestType instance from an ESPDRequest Model instance
     */
    private ESPDRequestType createXML(ESPDRequest req) {
        ESPDRequestType reqType = finalize(SchemaFactory.ESPD_REQUEST.extractESPDRequestType(req));
        return reqType;

    }
    /**
     *
     * @param res The ESPDResponse Model instance to be transformed to XML
     * @return a JAXB ESPDResponseType instance from an ESPDResponse Model instance
     */
    private ESPDResponseType createXML(ESPDResponse res) {
        ESPDResponseType resType = finalize(SchemaFactory.ESPD_RESPONSE.extractESPDResponseType(res));
        return resType;
    }

    
    /**
     * Finalizes an ESPDRequestType instance by adding the necessary data required 
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     * @param reqType The JAXB ESPDRequestType that will be finalized.
     * @return the Finalized ESPDRequestType Instance
     */
    private ESPDRequestType finalize(ESPDRequestType reqType) {

        // Finalizes the ESPDRequest Type, adding the Date and Time of Issue etc
        reqType.setUBLVersionID(SchemaFactory.ESPD_REQUEST.createUBL21VersionIdType());

        reqType.setCustomizationID(SchemaFactory.ESPD_REQUEST.createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns070:ver3.0"));
        reqType.setVersionID(SchemaFactory.ESPD_REQUEST.createVersionIDType("1"));

        reqType.setCopyIndicator(new CopyIndicatorType());
        reqType.getCopyIndicator().setValue(false);

        Date now = new Date();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);

        try {
            XMLGregorianCalendar xmlDate = createECCompliantDate(c);

            reqType.setIssueDate(new IssueDateType());
            reqType.getIssueDate().setValue(xmlDate);
            reqType.setIssueTime(new IssueTimeType());
            reqType.getIssueTime().setValue(xmlDate);
        } catch (DatatypeConfigurationException e) {
            System.out.println("ERROR in DATES!");
        }

        reqType.setID(SchemaFactory.ESPD_REQUEST.createISOIECIDType(UUID.randomUUID().toString()));
        return reqType;
    }
       
    /**
     * Finalizes an ESPDResponseType instance by adding the necessary data required 
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     * @param resType The JAXB ESPDResponseType that will be finalized.
     * @return the Finalized ESPDResponseType Instance
     */
    public ESPDResponseType finalize(ESPDResponseType resType) {

        // Finalizes the ESPDResponse Type, adding the Date and Time of Issue etc
        resType.setUBLVersionID(SchemaFactory.ESPD_RESPONSE.createUBL21VersionIdType());

        resType.setCustomizationID(SchemaFactory.ESPD_RESPONSE.createBIICustomizationIdType("urn:www.cenbii.eu:transaction:biitrns092:ver3.0"));
        resType.setVersionID(SchemaFactory.ESPD_RESPONSE.createVersionIDType("1"));

        resType.setCopyIndicator(new CopyIndicatorType());
        resType.getCopyIndicator().setValue(false);

        Date now = new Date();
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(now);

        try {
            XMLGregorianCalendar xmlDate = createECCompliantDate(c);

            resType.setIssueDate(new IssueDateType());
            resType.getIssueDate().setValue(xmlDate);
            resType.setIssueTime(new IssueTimeType());
            resType.getIssueTime().setValue(xmlDate);
        } catch (DatatypeConfigurationException e) {
            System.out.println("ERROR in DATES!");
        }

        resType.setID(SchemaFactory.ESPD_RESPONSE.createISOIECIDType(UUID.randomUUID().toString()));
        return resType;
    }

    
    /**
     * Created an XMLGregorianCalendar instance that is compliant with the format
     * required by ESPD Data model.
     * @param c The date (GregorianCalendar) to be converted to ESPD Compliant XMLGregorianCalendar
     * @return the XMLGregorianCalendar transformation of the input
     * @throws DatatypeConfigurationException when input is invalid
     */
    private XMLGregorianCalendar createECCompliantDate(GregorianCalendar c) throws DatatypeConfigurationException {
        // Creates the format according to the EC Application Requirement
        XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.HOUR),
                        c.get(Calendar.MINUTE),
                        c.get(Calendar.SECOND),
                        DatatypeConstants.FIELD_UNDEFINED,
                        DatatypeConstants.FIELD_UNDEFINED
                );
        return xmlDate;
    }
    
    /**
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     * @param theReq The ESPD Request that we want transformed to XML
     * @return the Finalized ESPDRequestType Instance
     */
    private String createXMLasString(ESPDRequest theReq) {
        StringWriter result = new StringWriter();

        //Return the Object
        try {
            if (theReq instanceof ESPDResponse) {
                grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory of = new grow.names.specification.ubl.schema.xsd.espdresponse_1.ObjectFactory();
                SchemaUtil.getMarshaller().marshal(of.createESPDResponse(createXML((ESPDResponse) theReq)), result);

            } else {
                grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory of = new grow.names.specification.ubl.schema.xsd.espdrequest_1.ObjectFactory();
                SchemaUtil.getMarshaller().marshal(of.createESPDRequest(createXML(theReq)), result);
            }
        } catch (JAXBException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.toString();
    }
    
           
}
