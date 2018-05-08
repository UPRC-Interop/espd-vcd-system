package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.SchemaUtil;
import test.x.ubl.pre_award.commonbasic.IssueDateType;
import test.x.ubl.pre_award.commonbasic.IssueTimeType;
import test.x.ubl.pre_award.commonbasic.ProfileIDType;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;
import test.x.ubl.pre_award.qualificationapplicationresponse.QualificationApplicationResponseType;

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

public class DocumentBuilderV2 {

    protected final String theXML;

    public DocumentBuilderV2(ESPDRequest req) {
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
     * @param req The ESPDRequest Model instance to be transformed to XML
     * @return a JAXB QualificationApplicationRequestType instance from an ESPDRequest Model instance
     */
    private QualificationApplicationRequestType createXML(ESPDRequest req) {
        QualificationApplicationRequestType reqType = finalize(SchemaFactory.V2.ESPD_REQUEST
                .extractQualificationApplicationRequestType(req));
        return reqType;
    }

    /**
     * @param res The ESPDResponse Model instance to be transformed to XML
     * @return a JAXB QualificationApplicationResponseType instance from an ESPDResponse Model instance
     */
    protected QualificationApplicationResponseType createXML(ESPDResponse res) {
        QualificationApplicationResponseType resType =
                finalize(SchemaFactory.V2.ESPD_RESPONSE.extractQualificationApplicationResponseType(res));
        return resType;
    }

    /**
     * Finalizes an QualificationApplicationRequestType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
     * @param reqType The JAXB QualificationApplicationRequestType that will be finalized.
     * @return the Finalized QualificationApplicationRequestType Instance
     */
    private QualificationApplicationRequestType finalize(QualificationApplicationRequestType reqType) {

        // Finalizes the QualificationApplicationRequestType Type, adding the Date and Time of Issue etc
        reqType.setIssueDate(new IssueDateType());
        reqType.getIssueDate().setValue(LocalDate.now());
        reqType.setIssueTime(new IssueTimeType());
        reqType.getIssueTime().setValue(LocalTime.now());


        reqType.setProfileID(createCENBIIProfileIdType(getProfileID()));
        // FIXME local id value here may have to be changed (temporary value has been applied)
        reqType.setID(SchemaFactory.V2.ESPD_REQUEST.createLocalIDType("ESPDREQ-DGPE-ad63141780"));
        reqType.setUUID(SchemaFactory.V2.ESPD_REQUEST.createISOIECUUIDType(UUID.randomUUID().toString()));

        return reqType;
    }

    /**
     * Finalizes an QualificationApplicationResponseType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
     * @param resType The JAXB QualificationApplicationResponseType that will be finalized.
     * @return the Finalized QualificationApplicationResponseType Instance
     */
    protected QualificationApplicationResponseType finalize(QualificationApplicationResponseType resType) {

        // Finalizes the QualificationApplicationResponseType, adding the Date and Time of Issue etc
        resType.setIssueDate(new IssueDateType());
        resType.getIssueDate().setValue(LocalDate.now());
        resType.setIssueTime(new IssueTimeType());
        resType.getIssueTime().setValue(LocalTime.now());

        resType.setProfileID(createCENBIIProfileIdType(getProfileID()));
        // FIXME local id value here may have to be changed (temporary value has been applied)
        resType.setID(SchemaFactory.V2.ESPD_REQUEST.createLocalIDType("ESPDREQ-DGPE-ad63141780"));
        resType.setUUID(SchemaFactory.V2.ESPD_RESPONSE.createISOIECUUIDType(UUID.randomUUID().toString()));

        return resType;
    }

    /**
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     *
     * @param theReq The ESPD Request that we want transformed to XML
     * @return the Finalized QualificationApplicationRequestType Instance
     */
    private String createXMLasString(ESPDRequest theReq) {
        StringWriter result = new StringWriter();

        //Return the Object
        try {
            if (theReq instanceof ESPDResponse) {
                test.x.ubl.pre_award.qualificationapplicationresponse.ObjectFactory of = new test.x.ubl.pre_award.qualificationapplicationresponse.ObjectFactory();
                SchemaUtil.getMarshaller().marshal(of.createQualificationApplicationResponse(createXML((ESPDResponse) theReq)), result);

            } else {
                test.x.ubl.pre_award.qualificationapplicationrequest.ObjectFactory of = new test.x.ubl.pre_award.qualificationapplicationrequest.ObjectFactory();
                SchemaUtil.getMarshaller().marshal(of.createQualificationApplicationRequest(createXML(theReq)), result);
            }
        } catch (JAXBException ex) {
            Logger.getLogger(XMLDocumentBuilderV2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result.toString();
    }

    /**
     * Creates a ProfileIDType object with the given id.
     *
     * @param id
     * @return
     */
    private ProfileIDType createCENBIIProfileIdType(String id) {
        ProfileIDType pid = new ProfileIDType();
        pid.setSchemeAgencyID("CEN-BII");
        pid.setSchemeVersionID("1.0");
        pid.setValue(id);
        return pid;
    }

    protected String getProfileID() {
        return "41";
    }

}
