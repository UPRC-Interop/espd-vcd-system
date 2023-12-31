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
import eu.espd.schema.v1.commonbasiccomponents_2.IssueDateType;
import eu.espd.schema.v1.commonbasiccomponents_2.IssueTimeType;
import eu.espd.schema.v1.commonbasiccomponents_2.ProfileIDType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;

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
public class DocumentBuilderV1 {

    protected final String theXML;

    public DocumentBuilderV1(ESPDRequest req) {
        theXML = createXMLasString(req);
    }

    public DocumentBuilderV1(ESPDRequest request, DocumentType type) {
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
     * @return a JAXB ESPDRequestType instance from an ESPDRequest Model instance
     */
    private ESPDRequestType createXML(ESPDRequest req) {
        return createXML(req, DocumentType.XML);
    }

    private ESPDRequestType createXML(ESPDRequest req, DocumentType type) {
        ESPDRequestType reqType;

        switch (type) {

            case XML:
                reqType = finalize(SchemaFactory.EDM_V1
                        .ESPD_REQUEST.extractESPDRequestType(req));
                return reqType;

            case PDF:
                reqType = finalize(SchemaFactory.EDM_V1
                        .ESPD_REQUEST_FOR_PDF.extractESPDRequestType(req));
                return reqType;

            default:
                throw new IllegalStateException("Supported document types are: XML and PDF");
        }

    }

    /**
     * @param res The ESPDResponse Model instance to be transformed to XML
     * @return a JAXB ESPDResponseType instance from an ESPDResponse Model instance
     */
    protected ESPDResponseType createXML(ESPDResponse res) {
        ESPDResponseType resType = finalize(SchemaFactory.EDM_V1
                .ESPD_RESPONSE.extractESPDResponseType(res));
        return resType;
    }

    protected ESPDResponseType createXML(ESPDResponse res, DocumentType type) {
        ESPDResponseType resType;

        switch (type) {

            case XML:
                resType = finalize(SchemaFactory.EDM_V1
                        .ESPD_RESPONSE.extractESPDResponseType(res));
                return resType;

            case PDF:
                resType = finalize(SchemaFactory.EDM_V1
                        .ESPD_RESPONSE_FOR_PDF.extractESPDResponseType(res));
                return resType;

            default:
                throw new IllegalStateException("Supported document types are: XML and PDF");
        }
    }

    /**
     * Finalizes an ESPDRequestType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
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

        reqType.setID(SchemaFactory.EDM_V1
                .ESPD_REQUEST.createISOIECIDType(UUID.randomUUID().toString()));
        return reqType;
    }

    /**
     * Finalizes an ESPDResponseType instance by adding the necessary data required
     * for publication. These include the UBL constants, transactions, issue date
     * and issue time.
     *
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

        resType.setID(SchemaFactory.EDM_V1
                .ESPD_RESPONSE.createISOIECIDType(UUID.randomUUID().toString()));
        return resType;
    }

    /**
     * Transforms the ESPDRequest model instance to an XML ESPD Request String
     *
     * @param theReq The ESPD Request that we want transformed to XML
     * @return the Finalized ESPDRequestType Instance
     */
    private String createXMLasString(ESPDRequest theReq) {
        return createXMLasString(theReq, DocumentType.XML);
    }

    private String createXMLasString(ESPDRequest theReq, DocumentType type) {
        StringWriter result = new StringWriter();

        //Return the Object
        try {
            if (theReq instanceof ESPDResponse) {
                eu.espd.schema.v1.espdresponse_1.ObjectFactory of = new eu.espd.schema.v1.espdresponse_1.ObjectFactory();
                SchemaUtil.getMarshaller(EDMVersion.V1).marshal(of.createESPDResponse(createXML((ESPDResponse) theReq, type)), result);

            } else {
                eu.espd.schema.v1.espdrequest_1.ObjectFactory of = new eu.espd.schema.v1.espdrequest_1.ObjectFactory();
                SchemaUtil.getMarshaller(EDMVersion.V1).marshal(of.createESPDRequest(createXML(theReq, type)), result);
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
