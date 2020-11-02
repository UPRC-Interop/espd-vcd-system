/**
 * Copyright 2016-2019 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.codelist.enums.internal.DocumentType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import eu.espd.schema.v2.v210.commonbasic.IssueDateType;
import eu.espd.schema.v2.v210.commonbasic.IssueTimeType;
import eu.espd.schema.v2.v210.commonbasic.ProfileIDType;
import eu.espd.schema.v2.v210.qualificationapplicationrequest.QualificationApplicationRequestType;
import eu.espd.schema.v2.v210.qualificationapplicationresponse.QualificationApplicationResponseType;

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

    public DocumentBuilderV2(ESPDRequest request) {
        theXML = createXMLasString(request);
    }

    public DocumentBuilderV2(ESPDRequest request, DocumentType type) {
        theXML = createXMLasString(request, type);
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
        return createXML(req, DocumentType.XML);
    }

    /**
     * @param req  The ESPDRequest Model instance to be transformed to XML
     * @param type The Document type e.g. PDF, XML
     * @return a JAXB QualificationApplicationRequestType instance from an ESPDRequest Model instance
     */
    private QualificationApplicationRequestType createXML(ESPDRequest req, DocumentType type) {

        QualificationApplicationRequestType reqType;

        switch (type) {

            case XML:
                reqType = finalize(SchemaFactory.EDM_V2
                        .ESPD_REQUEST.extractQualificationApplicationRequestType(req));
                return reqType;

            case PDF:
                reqType = finalize(SchemaFactory.EDM_V2
                        .ESPD_REQUEST_FOR_PDF.extractQualificationApplicationRequestType(req));
                return reqType;

            default:
                throw new IllegalStateException("Supported document types are: XML and PDF");
        }

    }

    /**
     * @param res The ESPDResponse Model instance to be transformed to XML
     * @return a JAXB QualificationApplicationResponseType instance from an ESPDResponse Model instance
     */
    protected QualificationApplicationResponseType createXML(ESPDResponse res) {
        return createXML(res, DocumentType.XML);
    }

    /**
     * @param res  The ESPDResponse Model instance to be transformed to XML
     * @param type The Document type e.g. PDF, XML
     * @return a JAXB QualificationApplicationResponseType instance from an ESPDResponse Model instance
     */
    protected QualificationApplicationResponseType createXML(ESPDResponse res, DocumentType type) {

        QualificationApplicationResponseType responseType;

        switch (type) {

            case XML:
                responseType = finalize(SchemaFactory.EDM_V2
                        .ESPD_RESPONSE.extractQualificationApplicationResponseType(res));
                return responseType;

            case PDF:
                responseType = finalize(SchemaFactory.EDM_V2
                        .ESPD_RESPONSE_FOR_PDF.extractQualificationApplicationResponseType(res));
                return responseType;

            default:
                throw new IllegalStateException("Supported document types are: XML and PDF");
        }

    }

    /**
     * Finalizes an QualificationApplicationRequestType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
     * @param requestType The JAXB QualificationApplicationRequestType that will be finalized.
     * @return the Finalized QualificationApplicationRequestType Instance
     */
    private QualificationApplicationRequestType finalize(QualificationApplicationRequestType requestType) {

        // Finalizes the QualificationApplicationRequestType Type, adding the Date and Time of Issue etc
        requestType.setIssueDate(new IssueDateType());
        requestType.getIssueDate().setValue(LocalDate.now());
        requestType.setIssueTime(new IssueTimeType());
        requestType.getIssueTime().setValue(LocalTime.now());


        requestType.setProfileID(createCENBIIProfileIdType(getProfileID()));
        requestType.setID(SchemaFactory.EDM_V2
                .ESPD_REQUEST.createISOIECIDType(UUID.randomUUID().toString()));
        requestType.setUUID(SchemaFactory.EDM_V2
                .ESPD_REQUEST.createISOIECUUIDType(UUID.randomUUID().toString()));

        return requestType;
    }

    /**
     * Finalizes an QualificationApplicationResponseType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
     * @param responseType The JAXB QualificationApplicationResponseType that will be finalized.
     * @return the Finalized QualificationApplicationResponseType Instance
     */
    protected QualificationApplicationResponseType finalize(QualificationApplicationResponseType responseType) {

        // Finalizes the QualificationApplicationResponseType, adding the Date and Time of Issue etc
        responseType.setIssueDate(new IssueDateType());
        responseType.getIssueDate().setValue(LocalDate.now());
        responseType.setIssueTime(new IssueTimeType());
        responseType.getIssueTime().setValue(LocalTime.now());

        responseType.setProfileID(createCENBIIProfileIdType(getProfileID()));
        responseType.setID(SchemaFactory.EDM_V2
                .ESPD_RESPONSE.createISOIECIDType(UUID.randomUUID().toString()));
        responseType.setUUID(SchemaFactory.EDM_V2
                .ESPD_RESPONSE.createISOIECUUIDType(UUID.randomUUID().toString()));

        return responseType;
    }

    /**
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     *
     * @param theReq The ESPD Request that we want transformed to XML
     * @return the Finalized QualificationApplicationRequestType Instance
     */
    private String createXMLasString(ESPDRequest theReq) {
        return createXMLasString(theReq, DocumentType.XML);
    }

    private String createXMLasString(ESPDRequest theReq, DocumentType type) {
        StringWriter result = new StringWriter();

        //Return the Object
        try {
            if (theReq instanceof ESPDResponse) {
                eu.espd.schema.v2.v210.qualificationapplicationresponse.ObjectFactory of = new eu.espd.schema.v2.v210.qualificationapplicationresponse.ObjectFactory();
                SchemaUtil.getMarshaller(EDMVersion.V2).marshal(of.createQualificationApplicationResponse(createXML((ESPDResponse) theReq, type)), result);

            } else {
                eu.espd.schema.v2.v210.qualificationapplicationrequest.ObjectFactory of = new eu.espd.schema.v2.v210.qualificationapplicationrequest.ObjectFactory();
                SchemaUtil.getMarshaller(EDMVersion.V2).marshal(of.createQualificationApplicationRequest(createXML(theReq, type)), result);
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
        ProfileIDType pIdType = new ProfileIDType();
        pIdType.setSchemeAgencyID("CEN-BII");
        pIdType.setSchemeVersionID("2.0");
        pIdType.setValue(id);
        return pIdType;
    }

    protected String getProfileID() {
        return "41";
    }

}
