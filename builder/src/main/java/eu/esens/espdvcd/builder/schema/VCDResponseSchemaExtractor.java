package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.builder.EvidenceHelper;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.evidence.EvidenceIssuerParty;
import eu.esens.espdvcd.model.requirement.response.evidence.VCDEvidenceResponse;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.EconomicOperatorPartyType;
import grow.names.specification.ubl.schema.xsd.espd_commonaggregatecomponents_1.NaturalPersonType;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import isa.names.specification.ubl.schema.xsd.cev_commonbasiccomponents_1.IndicatorType;
import isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ContactType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PowerOfAttorneyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NameType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DocumentDescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.WebsiteURIType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ulf Lotzmann on 04/05/2017.
 */
public class VCDResponseSchemaExtractor extends ESPDResponseSchemaExtractor {

    private final static Logger log = LoggerFactory.getLogger(VCDResponseSchemaExtractor.class);

    @java.lang.Override
    protected EvidenceType extractEvidenceURLResponse(Response response) {

        VCDEvidenceResponse resp = (VCDEvidenceResponse) response;

        EvidenceType evType = new EvidenceType();
        DocumentReferenceType drt = new DocumentReferenceType();
        evType.getEvidenceDocumentReference().add(drt);

        if (resp.getEvidenceURL() != null) {
            drt.setID(new IDType());
            drt.getID().setValue(UUID.randomUUID().toString()); // FIXME: newly generated UUID, or use the ID from the model?
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
                drt.getIssueDate().setValue(xcal);
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
                drt.getIssueTime().setValue(xcal);
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

        if (resp.getIssuerParty() != null) {
            EvidenceIssuerParty ip = resp.getIssuerParty();
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
