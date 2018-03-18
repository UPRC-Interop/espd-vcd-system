package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.builder.util.SchemaUtil;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ProfileIDType;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ulf Lotzmann on 08/06/2016.
 */
public class DocumentBuilder {

    protected final String theXML;

    public DocumentBuilder(ESPDRequest req) {
        theXML = createXMLasString(req);
    }

    /**
     * Transforms the XML Representation of the data to an input stream.
     *
     * @return the String representation of the XML Data as an input stream
     */
    public InputStream getAsInputStream() {
        ByteArrayInputStream bis = new ByteArrayInputStream(theXML.getBytes(Charset.forName("UTF-8")));
        return bis;
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
   protected ESPDResponseType createXML(ESPDResponse res) {
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
        reqType.setIssueDate(new IssueDateType());
        reqType.getIssueDate().setValue(LocalDate.now());
        reqType.setIssueTime(new IssueTimeType());
        reqType.getIssueTime().setValue(LocalTime.now());

        reqType.setProfileID(createBIIProfileIdType(getProfileID()));

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
    protected ESPDResponseType finalize(ESPDResponseType resType) {

        // Finalizes the ESPDResponse Type, adding the Date and Time of Issue etc
        resType.setIssueDate(new IssueDateType());
        resType.getIssueDate().setValue(LocalDate.now());
        resType.setIssueTime(new IssueTimeType());
        resType.getIssueTime().setValue(LocalTime.now());

        resType.setProfileID(createBIIProfileIdType(getProfileID()));

        resType.setID(SchemaFactory.ESPD_RESPONSE.createISOIECIDType(UUID.randomUUID().toString()));
        return resType;
    }

    /**
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     *
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
            Logger.getLogger(XMLDocumentBuilderV1.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.toString();
    }

    /**
     * Creates a ProfileIDType object with the given id.
     *
     * @param id
     * @return
     */
    private ProfileIDType createBIIProfileIdType(String id) {

        ProfileIDType pid = new ProfileIDType();

        //pid.setSchemeName("CustomizationID");
        pid.setSchemeAgencyID("BII");
        //pid.setSchemeVersionID("3.0");
        pid.setValue(id);

        return pid;

    }

    protected String getProfileID() {
        return "undefined";
    }
}
