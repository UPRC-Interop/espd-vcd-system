package eu.esens.espdvcd.designer.endpoint;

import com.helger.commons.ValueEnforcer;
import com.helger.datetime.util.PDTXMLConverter;
import eu.esens.espdvcd.designer.service.ToopService;
import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;

import eu.toop.commons.dataexchange.TDEAddressType;
import eu.toop.commons.dataexchange.TDEConceptRequestType;
import eu.toop.commons.dataexchange.TDEDataConsumerType;
import eu.toop.commons.dataexchange.TDEDataElementRequestType;
import eu.toop.commons.dataexchange.TDEDataRequestAuthorizationType;
import eu.toop.commons.dataexchange.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.TDELegalEntityType;
import eu.toop.commons.dataexchange.TDETOOPRequestType;
import eu.toop.commons.jaxb.ToopXSDHelper;
import java.nio.charset.StandardCharsets;

import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;
import java.util.UUID;
import spark.Request;
import spark.Response;
import spark.Service;

public class ToopDataRequestEndpoint extends Endpoint {

    ToopService service;

    public ToopDataRequestEndpoint(ToopService service){
        this.service = service;
    }
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

    private Object postRequest(Request request, Response response) {

        //TODO PROCESS UI REQUEST
        return null;

    }

    private static TDETOOPRequestType createRequest (
        final TDEDataRequestSubjectType aRequestSubject,
        final IdentifierType aSenderParticipantID,
        final String sCountryCode,
        final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
        final EPredefinedProcessIdentifier eProcessID,
        final Iterable<? extends ConceptValue> aValues) {

        ValueEnforcer.notNull (aRequestSubject, "RequestSubject");
        ValueEnforcer.notNull (aSenderParticipantID, "SenderParticipantID");
        ValueEnforcer.notEmpty (sCountryCode, "CountryCode");
        ValueEnforcer.notNull (eDocumentTypeID, "DocumentTypeID");
        ValueEnforcer.notNull (eProcessID, "ProcessID");

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

            // ID Goes here
            aLegEnt.setLegalEntityIdentifier(aSenderParticipantID);
            final TDEAddressType aAddress = new TDEAddressType ();

            // Destination country to use
            aAddress.setCountryCode (ToopXSDHelper.createCode (sCountryCode));
            aLegEnt.setLegalEntityLegalAddress(aAddress);
            aReqSub.setLegalEntity(aLegEnt);
            aRet.setDataRequestSubject(aReqSub);
        }

        {
            final TDEDataConsumerType aDC = new TDEDataConsumerType ();
            aDC.setDCUniqueIdentifier (ToopXSDHelper.createIdentifier ("EL-GSCCP-ESPD"));
            aDC.setDCName (ToopXSDHelper.createText ("GSCCP ESPD System"));
            // Sender participant ID
            aDC.setDCElectronicAddressIdentifier (aSenderParticipantID.clone ());
            final TDEAddressType aAddress = new TDEAddressType ();
            aAddress.setCountryCode (ToopXSDHelper.createCode ("GR"));
            aDC.setDCLegalAddress (aAddress);
            aRet.setDataConsumer (aDC);
        }

        {
            aRet.setDataRequestSubject (aRequestSubject);
        }

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
}