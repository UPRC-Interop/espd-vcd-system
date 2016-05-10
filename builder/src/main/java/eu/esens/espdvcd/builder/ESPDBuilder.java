package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.schema.SchemaUtil;

import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.CopyIndicatorType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;

public class ESPDBuilder {

    private boolean insertAllCriteria = true;

    /**
     * Parses the input stream and creates an ESPDRequest model instance.
     * @param xmlESPD The input stream of the XML document to be parsed 
     * @return a prefilled ESPDRequest based on the input data 
     * @throws BuilderException when the parsing from XML to ESPDRequest Model fails
     */
    public ESPDRequest createESPDRequestFromXML(InputStream xmlESPD) throws BuilderException {

        ESPDRequest req;

        try (InputStream bis = getBufferedInputStream(xmlESPD)) {
            // Check and read the file in the JAXB Object
            ESPDRequestType reqType = readESPDRequestFromStream(bis);
            // Create the Model Object
            req = ModelFactory.ESPD_REQUEST.extractESPDRequest(reqType);
            if (insertAllCriteria) {
                CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
                req.setCriterionList(cr.getFullList(req.getFullCriterionList()));
            }

            return req;

        } catch (IOException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading XML Input Stream", ex);
        }

    }
    /**
     * Parses the input stream and creates an ESPDResponse model instance.
     * @param xmlESPDRes The input stream of the XML document to be parsed 
     * @return a prefilled ESPDRequest based on the input data 
     * @throws BuilderException when the parsing from XML to ESPDResponse Model fails
     */
    public ESPDResponse createESPDResponseFromXML(InputStream xmlESPDRes) throws BuilderException {

        ESPDResponse res;
        // Check and read the file in the JAXB Object
        try (InputStream bis = getBufferedInputStream(xmlESPDRes)) {
            // Check and read the file in the JAXB Object
            ESPDResponseType resType = readESPDResponseFromStream(bis);
            // Create the Model Object
            res = ModelFactory.ESPD_RESPONSE.extractESPDResponse(resType);
        } catch (IOException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            throw new BuilderException("Error in Reading Input Stream for ESPD Response", ex);
        }

        return res;
    }

    /**
     *
     * @param req The ESPDRequest Model instance to be transformed to XML
     * @return a JAXB ESPDRequestType instance from an ESPDRequest Model instance
     */
    public ESPDRequestType createXML(ESPDRequest req) {
        ESPDRequestType reqType = finalize(SchemaFactory.ESPD_REQUEST.extractESPDRequestType(req));
        return reqType;

    }
    /**
     *
     * @param res The ESPDResponse Model instance to be transformed to XML
     * @return a JAXB ESPDResponseType instance from an ESPDResponse Model instance
     */
    public ESPDResponseType createXML(ESPDResponse res) {
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
    public ESPDRequestType finalize(ESPDRequestType reqType) {

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
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     * @param theReq The ESPD Request that we want transformed to XML
     * @return the Finalized ESPDRequestType Instance
     */
    public String createXMLasString(ESPDRequest theReq) {
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
    
    /**
     * @return the full predefined criteria list of the Standard ESPD Form
     */
    public List<SelectableCriterion> getCriteriaList() {
        CriteriaExtractor cr = new PredefinedESPDCriteriaExtractor();
        return cr.getFullList();
    }

    
    private ESPDRequestType readESPDRequestFromStream(InputStream is) {
        try {
            // Start with the convience methods provided by JAXB. If there are
            // perfomance issues we will swicth back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDRequestType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private ESPDResponseType readESPDResponseFromStream(InputStream is) {
        try {
            // Start with the convience methods provided by JAXB. If there are
            // perfomance issues we will swicth back to the JAXB API Usage
            return SchemaUtil.getUnmarshaller().unmarshal(new StreamSource(is), ESPDResponseType.class).getValue();
        } catch (JAXBException ex) {
            Logger.getLogger(ESPDBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *Sets the option to add all the criteria when creating an ESPD Request,
     * even when the ESPD Request creation is creating from parsing an existing 
     * ESPD Request. If set to true, the criteria not existing in the importing
     * ESPD Request will be added unselected.
     * @param insertAll True if builder will return all the criteria, false otherwise.
     */
    public void setInsertAllCriteria(boolean insertAll) {
        insertAllCriteria = insertAll;
    }

    /**
     * Gets the option to add all the criteria when creating an ESPD Request,
     * even when the ESPD Request creation is creating from parsing an existing 
     * ESPD Request. If true, the criteria not existing in the importing
     * ESPD Request will be added unselected.
     * @return true if builder will return all the criteria, false otherwise.
     */
    public boolean isInsertAllCriteria() {
        return insertAllCriteria;
    }

    private InputStream getBufferedInputStream(InputStream xmlESPD) {
        // We require a marked input stream
        InputStream bis;
        if (xmlESPD.markSupported()) {
            bis = xmlESPD;
        } else {
            bis = new BufferedInputStream(xmlESPD);
        }
        return bis;
    }

    /**
     * Created an XMLGregorianCalendar instance that is compliant with the format
     * required by ESPD Data model.
     * @param c The date (GregorianCalendar) to be converted to ESPD Compliant XMLGregorianCalendar
     * @return the XMLGregorianCalendar transformation of the input
     * @throws DatatypeConfigurationException when input is invalid
     */
    public XMLGregorianCalendar createECCompliantDate(GregorianCalendar c) throws DatatypeConfigurationException {
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
}
