package eu.esens.espdvcd.builder.schema.v1;

import eu.esens.espdvcd.builder.EvidenceHelper;
import eu.esens.espdvcd.model.EvidenceIssuerDetails;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.evidence.VCDEvidenceResponse;
import eu.espd.schema.v1.cev_commonaggregatecomponents_1.EvidenceType;
import eu.espd.schema.v1.cev_commonbasiccomponents_1.IndicatorType;
import eu.espd.schema.v1.commonaggregatecomponents_2.*;
import eu.espd.schema.v1.commonbasiccomponents_2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.UUID;

public class VCDResponseSchemaExtractorV1 extends ESPDResponseSchemaExtractorV1 {

    private final static Logger log = LoggerFactory.getLogger(VCDResponseSchemaExtractorV1.class);

    @java.lang.Override
    protected EvidenceType extractEvidenceURLResponse(Response response) {

        VCDEvidenceResponse resp = (VCDEvidenceResponse) response;

        EvidenceType evType = new EvidenceType();
        DocumentReferenceType drt = new DocumentReferenceType();
        evType.getEvidenceDocumentReference().add(drt);

        if (resp.getEvidenceURL() != null) {
            drt.setID(new IDType());
            drt.getID().setValue(UUID.randomUUID().toString()); // FIXME: newly generated UUID, or use the ID from the model?
            // drt.getID().setSchemeAgencyID("EU-COM-GROW");
            drt.setAttachment(new AttachmentType());
            drt.getAttachment().setExternalReference(new ExternalReferenceType());
            drt.getAttachment().getExternalReference().setURI(new URIType());

            drt.getAttachment().getExternalReference().getURI().setValue(
                    EvidenceHelper.transformEvidenceURIFromLocalResourceToASiCResource(
                            resp.getEvidenceURL()
                    )
            );
        }

        if (resp.getDate() != null) {
            try {
                drt.setIssueDate(new IssueDateType());

                XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendarDate(
                                resp.getDate().getYear(),
                                resp.getDate().getMonthValue(),
                                resp.getDate().getDayOfMonth(),
                                DatatypeConstants.FIELD_UNDEFINED);
                drt.getIssueDate().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalDate());
            } catch (DatatypeConfigurationException ex) {
                log.error("Could not create XML Date Object", ex);
            }
        }

        if (resp.getTime() != null) {
            try {
                drt.setIssueTime(new IssueTimeType());

                XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendarTime(
                                resp.getTime().getHour(),
                                resp.getTime().getMinute(),
                                resp.getTime().getSecond(),
                                DatatypeConstants.FIELD_UNDEFINED);
                drt.getIssueTime().setValue(xcal.toGregorianCalendar().toZonedDateTime().toLocalTime());
            } catch (DatatypeConfigurationException ex) {
                log.error("Could not create XML Time Object", ex);
            }
        }

        if (resp.getTypeCode() != null) {
            drt.setDocumentTypeCode(new DocumentTypeCodeType());
            drt.getDocumentTypeCode().setValue(resp.getTypeCode());
        }

        if (resp.getDescription() != null) {
            DocumentDescriptionType dt = new DocumentDescriptionType();
            dt.setValue(resp.getDescription());
            drt.getDocumentDescription().add(dt);
        }

        if (resp.getName() != null) {
            evType.setEvidenceName(new NameType());
            evType.getEvidenceName().setValue(resp.getName());
        }

        evType.setEmbeddedEvidenceIndicator(new IndicatorType());
        evType.getEmbeddedEvidenceIndicator().setValue(resp.isEmbeddedEvidenceIndicator());

        if (resp.getEvidenceIssuer() != null) {
            EvidenceIssuerDetails ip = resp.getEvidenceIssuer();
            PartyType pt = new PartyType();
            evType.setEvidenceIssuerParty(pt);

            if (ip.getID() != null) {
                PartyIdentificationType pit = new PartyIdentificationType();
                pit.setID(new IDType());
                pit.getID().setValue(ip.getID());
                //pit.getID().setSchemeAgencyID("EU-COM-GROW");
                pt.getPartyIdentification().add(pit);
            }

            if (ip.getName() != null) {
                PartyNameType pnt = new PartyNameType();
                pnt.setName(new NameType());
                pnt.getName().setValue(ip.getName());
                pt.getPartyName().add(pnt);
            }

            if (ip.getWebsite() != null) {
                pt.setWebsiteURI(new WebsiteURIType());
                pt.getWebsiteURI().setValue(ip.getWebsite());
            }
        }

        return evType;
    }

}
