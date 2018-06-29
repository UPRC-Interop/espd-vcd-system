package eu.esens.espdvcd.designer.endpoint;

import com.google.common.io.Files;
import com.helger.asic.SignatureHelper;
import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.resourceprovider.DefaultResourceProvider;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.datetime.util.PDTXMLConverter;
import eu.esens.espdvcd.designer.util.Config;
import eu.esens.espdvcd.designer.model.ToopDataRequest;
import eu.esens.espdvcd.designer.util.HttpClientInvoker;

import eu.esens.espdvcd.model.EODetails;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;

import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDELegalEntityType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.commons.jaxb.ToopXSDHelper;

import java.io.File;
import java.io.IOException;

import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Service;

import static eu.esens.espdvcd.designer.Server.TOOPResponseQueue;

public class ToopDataRequestEndpoint extends Endpoint {

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                return "You need to POST a Toop data request.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST a Toop data request.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request request, Response response) throws IOException, InterruptedException {
        LOGGER.info("Got a TOOP data request.");
        ToopDataRequest toopDataRequest = MAPPER.readValue(request.body(), ToopDataRequest.class);
        createRequestAndSendToToopConnector(toopDataRequest);
        LOGGER.info("Sent TOOP Request to the Connector.");

        EODetails toopDetails = TOOPResponseQueue.take();
        LOGGER.info("Sending toop response.");

        return WRITER.writeValueAsString(toopDetails);
    }

    private static TDETOOPRequestType createRequest (
        final IdentifierType aSenderParticipantID,
        final String sCountryCode,
        final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
        final EPredefinedProcessIdentifier eProcessID) {

        ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
        ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
        ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
        ValueEnforcer.notNull (eProcessID, "ProcessID");

        //FIXME: HARDCODED CONCEPTVALUE LIST FOR NOW
        final Iterable<? extends ConceptValue> aValues = createConceptValues();

        final TDETOOPRequestType aRet = new TDETOOPRequestType ();
        aRet.setDocumentUniversalUniqueIdentifier (ToopXSDHelper
            .createIdentifier (UUID.randomUUID ().toString ()));
        aRet.setDocumentIssueDate (PDTXMLConverter.getXMLCalendarDateNow ());
        aRet.setDocumentIssueTime (PDTXMLConverter.getXMLCalendarTimeNow ());
        aRet.setCopyIndicator (ToopXSDHelper.createIndicator (false));
        // Document type ID
        aRet.setDocumentTypeIdentifier (ToopXSDHelper.createIdentifier (eDocumentTypeID.getScheme (),
            eDocumentTypeID.getID ()));
        aRet.setSpecificationIdentifier (ToopXSDHelper.createIdentifier ("bla"));
        // Process ID
        aRet.setProcessIdentifier (ToopXSDHelper.createIdentifier (eProcessID.getScheme (), eProcessID.getID ()));
        aRet.setDataConsumerDocumentIdentifier (ToopXSDHelper.createIdentifier ("DC-ID-17"));
        aRet.setDataRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));

        // Data Subject for which we make the request. Data comes from the UI (ID and country)
        {
            TDEDataRequestSubjectType aReqSub = new TDEDataRequestSubjectType();
            TDELegalEntityType aLegEnt = new TDELegalEntityType();
            aReqSub.setDataRequestSubjectTypeCode(ToopXSDHelper.createCode("LE"));

            // ID Goes here
            aLegEnt.setLegalPersonUniqueIdentifier(aSenderParticipantID);
            final TDEAddressType aAddress = new TDEAddressType ();

            // Destination country to use
            aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
            aLegEnt.setLegalEntityLegalAddress(aAddress);
            aLegEnt.setLegalName(ToopXSDHelper.createText("MHTSOS"));
            aReqSub.setLegalEntity(aLegEnt);
            aRet.setDataRequestSubject(aReqSub);
        }

        {
            final TDEDataConsumerType aDC = new TDEDataConsumerType ();
            aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("EL-GSCCP-ESPD"));
            aDC.setDCName (ToopXSDHelper.createText ("GSCCP ESPD System"));
            // Sender participant ID
            IdentifierType dcElectronicAddressIdentifier = ToopXSDHelper.createIdentifier("9999:ESPD-ESIDIS");
            dcElectronicAddressIdentifier.setSchemeID("iso6523-actorid-upis");
            aDC.setDCElectronicAddressIdentifier (dcElectronicAddressIdentifier);
            final TDEAddressType aAddress = new TDEAddressType ();
            aAddress.setCountryCode (ToopXSDHelper.createCode ("GR"));
            aDC.setDCLegalAddress (aAddress);
            aRet.setDataConsumer (aDC);
        }

//        {
//            aRet.setDataRequestSubject (aRequestSubject);
//        }

        for (final ConceptValue aCV : aValues) {
            final TDEDataElementRequestType aReq = new TDEDataElementRequestType ();
            aReq.setDataElementRequestIdentifier (ToopXSDHelper.createIdentifier ("bla"));
            {
                final TDEConceptRequestType aSrcConcept = new TDEConceptRequestType ();
                aSrcConcept.setConceptTypeCode (ToopXSDHelper.createCode ("TC"));
                aSrcConcept.setSemanticMappingExecutionIndicator (ToopXSDHelper.createIndicator (false));
                aSrcConcept.setConceptNamespace (ToopXSDHelper.createIdentifier (aCV.getNamespace ()));
                aSrcConcept.setConceptName (ToopXSDHelper.createText (aCV.getValue ()));
                aReq.setConceptRequest (aSrcConcept);
            }

            aRet.addDataElementRequest (aReq);
        }
        return aRet;
    }

    private static List<ConceptValue> createConceptValues(){
        List<ConceptValue> conceptValues = new ArrayList<>(4);
        final String NAMESPACE = "http://toop.eu/organization";

        conceptValues.add(new ConceptValue(NAMESPACE, "CompanyCode"));
        conceptValues.add(new ConceptValue(NAMESPACE, "companyName"));
        conceptValues.add(new ConceptValue(NAMESPACE, "companyType"));
        conceptValues.add(new ConceptValue(NAMESPACE, "Address"));

        return conceptValues;
    }

    public static void createRequestAndSendToToopConnector (ToopDataRequest toopDataRequest) throws IOException {

        final SignatureHelper aSH = new SignatureHelper (new DefaultResourceProvider().getInputStream (Config.getKeystorePath ()),
                Config.getKeystorePassword (),
                Config.getKeystoreKeyAlias (),
                Config.getKeystoreKeyPassword ());

        try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {

            TDETOOPRequestType aRequest = createRequest(ToopXSDHelper.createIdentifier(toopDataRequest.getCompanyID()), toopDataRequest.getCountryCode(), EPredefinedDocumentTypeIdentifier.REQUEST_REGISTEREDORGANIZATION, EPredefinedProcessIdentifier.DATAREQUESTRESPONSE);

            ToopMessageBuilder.createRequestMessage (aRequest, aBAOS, aSH);

            final String aFromDCUrl = Config.getToopConnectorDPUrl ();

            Files.write(aBAOS.toByteArray(), new File("request.asic"));

            HttpClientInvoker.httpClientCallNoResponse (aFromDCUrl, aBAOS.toByteArray ());
        }
    }
}