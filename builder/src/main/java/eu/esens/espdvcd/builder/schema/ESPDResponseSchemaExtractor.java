/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.esens.espdvcd.builder.schema;

import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;
import eu.esens.espdvcd.model.requirement.response.CountryCodeResponse;
import eu.esens.espdvcd.model.requirement.response.DateResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;
import eu.esens.espdvcd.model.requirement.response.PeriodResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityYearResponse;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.requirement.response.Responses;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonbasiccomponents_1.IndicatorType;
import isa.names.specification.ubl.schema.xsd.cev_commonaggregatecomponents_1.EvidenceType;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ContractFolderIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PercentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.QuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.TypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.URIType;

public class ESPDResponseSchemaExtractor implements SchemaExtractor {

    public ESPDResponseType extractESPDResponseType(ESPDResponse res) {

        ESPDResponseType resType = new ESPDResponseType();

        if (res.getCADetails().getProcurementProcedureFileReferenceNo() != null) {
            resType.setContractFolderID(new ContractFolderIDType());
            resType.getContractFolderID().setSchemeAgencyID("TeD");
            resType.getContractFolderID().setValue(res.getCADetails().getProcurementProcedureFileReferenceNo());
        }

        resType.getAdditionalDocumentReference().add(extractCADetailsDocumentReferece(res.getCADetails()));
        resType.setContractingParty(extractContractingPartyType(res.getCADetails()));
        resType.getProcurementProjectLot().add(extractProcurementProjectLot(res.getCADetails()));
        resType.getCriterion().addAll(res.getFullCriterionList().stream()
                .filter(cr -> cr.isSelected())
                .map(cr -> extractCriterion(cr))
                .collect(Collectors.toList()));

        // TODO: ADD the EO Details
        return resType;
    }

    @Override
    public RequirementType extractRequirementType(Requirement r) {

        RequirementType req = new RequirementType();

        req.setResponseDataType(r.getResponseDataType().name());
        req.setDescription(new DescriptionType());
        req.getDescription().setValue(r.getDescription());
        req.setID(createCriterionRelatedIDType(r.getID()));
        req.getResponse().add(extractResponse(r.getResponse(), r.getResponseDataType()));

        return req;
    }

    private ResponseType extractResponse(Response response, Responses.Type respType) {

        ResponseType rType = new ResponseType();
        if (response == null)
            return rType;

        switch (respType) {

            case DESCRIPTION:
                if (((DescriptionResponse) response).getDescription() != null) {
                    rType.setDescription(new DescriptionType());
                    rType.getDescription().setValue(((DescriptionResponse) response).getDescription());
                }
                return rType;

            case QUANTITY_YEAR:
                if (((QuantityYearResponse) response).getYear() != 0) {
                    rType.setQuantity(new QuantityType());
                    rType.getQuantity().setUnitCode("YEAR");
                    rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityYearResponse) response).getYear()));
                }
                return rType;

            case QUANTITY:
                rType.setQuantity(new QuantityType());
                rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityResponse) response).getQuantity()));
                return rType;

            case QUANTITY_INTEGER:
                rType.setQuantity(new QuantityType());
                rType.getQuantity().setValue(BigDecimal.valueOf(((QuantityIntegerResponse) response).getQuantity()));
                return rType;

            case AMOUNT:
                rType.setAmount(new AmountType());
                rType.getAmount().setValue(BigDecimal.valueOf(((AmountResponse) response).getAmount()));
                rType.getAmount().setCurrencyID(((AmountResponse) response).getCurrency());
                return rType;

            case INDICATOR:
                rType.setIndicator(new IndicatorType());
                rType.getIndicator().setValue(((IndicatorResponse) response).isIndicator());
                return rType;

            case PERIOD:
                rType.setPeriod(new PeriodType());
                DescriptionType dt = new DescriptionType();
                dt.setValue(((PeriodResponse) response).getDescription());
                rType.getPeriod().getDescription().add(dt);
                return rType;

            case PERCENTAGE:
                rType.setPercent(new PercentType());
                rType.getPercent().setValue(BigDecimal.valueOf(((PercentageResponse) response).getPercentage()));
                return rType;

            case DATE:
                if (((DateResponse) response).getDate() != null) {
                    Date respDate = ((DateResponse) response).getDate();
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(respDate);
                    try {
                        XMLGregorianCalendar xcal = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendarDate(
                                        cal.get(Calendar.YEAR),
                                        cal.get(Calendar.MONTH) + 1,
                                        cal.get(Calendar.DAY_OF_MONTH),
                                        DatatypeConstants.FIELD_UNDEFINED);
                        rType.setDate(new DateType());
                        rType.getDate().setValue(xcal);

                    } catch (DatatypeConfigurationException ex) {
                        Logger.getLogger(ESPDResponseSchemaExtractor.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                }
                return rType;

            case CODE:
                rType.setCode(new TypeCodeType());
                rType.getCode().setValue(((EvidenceURLCodeResponse) response).getEvidenceURLCode());
                return rType;

            case EVIDENCE_URL:
                if (((EvidenceURLResponse) response).getEvidenceURL() != null) { 
                    EvidenceType evType = new EvidenceType();
                    DocumentReferenceType drt = new DocumentReferenceType();
                    drt.setID(new IDType());
                    drt.getID().setValue(UUID.randomUUID().toString());
                    drt.setAttachment(new AttachmentType());
                    drt.getAttachment().setExternalReference(new ExternalReferenceType());
                    drt.getAttachment().getExternalReference().setURI(new URIType());
                    drt.getAttachment().getExternalReference().getURI().setValue(((EvidenceURLResponse) response).getEvidenceURL());
                    evType.getEvidenceDocumentReference().add(drt);
                    rType.getEvidence().add(evType);
                }
                return rType;

            case CODE_COUNTRY:
                rType.setCode(new TypeCodeType());
                rType.getCode().setListAgencyID("ISO");
                rType.getCode().setListID("ISO 3166-2");
                rType.getCode().setListVersionID("1.0");
                rType.getCode().setValue(((CountryCodeResponse) response).getCountryCode());
                return rType;

            default:
                return null;
        }

    }
}
