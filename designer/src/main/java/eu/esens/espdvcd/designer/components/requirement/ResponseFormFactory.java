package eu.esens.espdvcd.designer.components.requirement;

import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.*;

/**
 * Created by ixuz on 4/12/16.
 */
public class ResponseFormFactory {

    public static ResponseForm buildResponseForm(Requirement requirement) {

        Response requirementResponse = requirement.getResponse();
        String caption = requirement.getDescription();

        switch (requirement.getResponseDataType()) {
            case INDICATOR:
                IndicatorResponse indicatorResponse = (IndicatorResponse) requirementResponse;
                IndicatorResponseForm indicatorResponseForm = new IndicatorResponseForm(indicatorResponse, caption);
                return indicatorResponseForm;
            case DATE:
                DateResponse dateResponse = (DateResponse) requirementResponse;
                DateResponseForm dateResponseForm = new DateResponseForm(dateResponse, caption);
                return dateResponseForm;
            case DESCRIPTION:
                DescriptionResponse descriptionResponse = (DescriptionResponse) requirementResponse;
                DescriptionResponseForm descriptionResponseForm = new DescriptionResponseForm(descriptionResponse, caption);
                return descriptionResponseForm;
            case QUANTITY:
                QuantityResponse quantityResponse = (QuantityResponse) requirementResponse;
                QuantityResponseForm quantityResponseForm = new QuantityResponseForm(quantityResponse, caption);
                return quantityResponseForm;
            case QUANTITY_YEAR:
                QuantityYearResponse quantityYearResponse = (QuantityYearResponse) requirementResponse;
                QuantityYearResponseForm quantityYearResponseForm = new QuantityYearResponseForm(quantityYearResponse, caption);
                return quantityYearResponseForm;
            case QUANTITY_INTEGER:
                QuantityIntegerResponse quantityIntegerResponse = (QuantityIntegerResponse) requirementResponse;
                QuantityIntegerResponseForm quantityIntegerResponseForm = new QuantityIntegerResponseForm(quantityIntegerResponse, caption);
                return quantityIntegerResponseForm;
            case AMOUNT:
                AmountResponse amountResponse = (AmountResponse) requirementResponse;
                AmountResponseForm amountResponseForm = new AmountResponseForm(amountResponse, caption);
                return amountResponseForm;
            case CODE_COUNTRY:
                CountryCodeResponse countryCodeResponse = (CountryCodeResponse) requirementResponse;
                CountryCodeResponseForm countryCodeResponseForm = new CountryCodeResponseForm(countryCodeResponse, caption);
                return countryCodeResponseForm;
            case PERCENTAGE:
                PercentageResponse percentageResponse = (PercentageResponse) requirementResponse;
                PercentageResponseForm percentageResponseForm = new PercentageResponseForm(percentageResponse, caption);
                return percentageResponseForm;
            case PERIOD:
                PeriodResponse periodResponse = (PeriodResponse) requirementResponse;
                PeriodResponseForm periodResponseForm = new PeriodResponseForm(periodResponse, caption);
                return periodResponseForm;
            case EVIDENCE_URL:
                EvidenceURLResponse evidenceURLResponse = (EvidenceURLResponse) requirementResponse;
                EvidenceURLResponseForm evidenceURLResponseForm = new EvidenceURLResponseForm(evidenceURLResponse, caption);
                return evidenceURLResponseForm;
            case CODE:
                EvidenceURLCodeResponse evidenceURLCodeResponse = (EvidenceURLCodeResponse) requirementResponse;
                EvidenceURLCodeResponseForm evidenceURLCodeResponseForm = new EvidenceURLCodeResponseForm(evidenceURLCodeResponse, caption);
                return evidenceURLCodeResponseForm;
            default:
                return null;
        }
    }
}
