
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.builder.Responses;
import eu.esens.espdvcd.model.SimpleESPDResponse;
import eu.esens.espdvcd.model.requirement.AmountResponse;
import eu.esens.espdvcd.model.requirement.CountryCodeResponse;
import eu.esens.espdvcd.model.requirement.DateResponse;
import eu.esens.espdvcd.model.requirement.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.EvidenceURLCodeResponse;
import eu.esens.espdvcd.model.requirement.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.PercentageResponse;
import eu.esens.espdvcd.model.requirement.PeriodResponse;
import eu.esens.espdvcd.model.requirement.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.QuantityResponse;
import eu.esens.espdvcd.model.requirement.QuantityYearResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.Response;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import grow.names.specification.ubl.schema.xsd.espdresponse_1.ESPDResponseType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.RequirementType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.ResponseType;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;
import sun.util.BuddhistCalendar;

public class ESPDResponseModelFactory implements ModelExtractor {

    ESPDResponseModelFactory() {
    }

    public SimpleESPDResponse extractESPDResponse(ESPDResponseType resType) {

        SimpleESPDResponse res = new SimpleESPDResponse();

        res.getFullCriterionList().addAll(resType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));
        
        res.setCADetails(extractCADetails(resType.getContractingParty(),
                resType.getContractFolderID(),
                resType.getProcurementProjectLot().get(0),
                resType.getAdditionalDocumentReference()));

        return res;
    }

    @Override
    public Requirement extractRequirement(RequirementType rt) {
        Requirement r = new ResponseRequirement(
                rt.getID().getValue(),
                rt.getResponseDataType(),
                rt.getDescription().getValue());

        if (!rt.getResponse().isEmpty()) {
            r.setResponse(extractResponse(rt.getResponse().get(0), Responses.Type.valueOf(rt.getResponseDataType())));
        }

        return r;
    }
    
    public Response extractResponse(ResponseType res, Responses.Type theType) {
     
       switch(theType) {

            case INDICATOR:
                IndicatorResponse resp = new IndicatorResponse();
                resp.setIndicator(res.getIndicator().isValue());
                return resp;

            case DATE:
                DateResponse dResp = new DateResponse();
                dResp.setDate(res.getDate().getValue().toGregorianCalendar().getTime());
                return dResp;

            case DESCRIPTION:
                DescriptionResponse deResp = new DescriptionResponse();
                deResp.setDescription(res.getDescription().getValue());
                return deResp;
                
            case QUANTITY:
                QuantityResponse qResp = new QuantityResponse();
                qResp.setQuantity(res.getQuantity().getValue().floatValue());
                return qResp;

            case QUANTITY_YEAR:
                QuantityYearResponse qyResp = new QuantityYearResponse();
                qyResp.setYear(res.getQuantity().getValue().intValueExact());
                return qyResp;
                
            case QUANTITY_INTEGER:
                QuantityIntegerResponse qiResp = new QuantityIntegerResponse();
                qiResp.setQuantity(res.getQuantity().getValue().intValueExact());
                return qiResp;

            case AMOUNT:
                AmountResponse aResp = new AmountResponse();
                aResp.setAmount(res.getAmount().getValue().floatValue());
                aResp.setCurrency(res.getAmount().getCurrencyID());
                return aResp;
                
                
            case CODE_COUNTRY:
                CountryCodeResponse cResp = new CountryCodeResponse();
                cResp.setCountryCode(res.getCode().getValue());
                return cResp;
                
            case PERCENTAGE:
                PercentageResponse pResp = new PercentageResponse();
                pResp.setPercentage(res.getPercent().getValue().floatValue());
                return pResp;
                
            case PERIOD:
                PeriodResponse perResp = new PeriodResponse();
                 //TODO: NULL Checks and empty list checks
                perResp.setDescription(res.getPeriod().getDescription().get(0).getValue());
                return perResp;
                
            case EVIDENCE_URL:
                EvidenceURLResponse eResp = new EvidenceURLResponse();
                //TODO: NULL Checks and empty list checks
                eResp.setEvidenceURL(res.getEvidence()
                        .get(0)
                        .getEvidenceDocumentReference()
                        .get(0)
                        .getAttachment()
                        .getExternalReference()
                        .getURI().getValue());
                
                return eResp;

            case CODE:
                EvidenceURLCodeResponse ecResp = new EvidenceURLCodeResponse();
                ecResp.setEvidenceURLCode(res.getCode().getValue());
                return ecResp;

            default:
                return null;
        }
       
    }

}
