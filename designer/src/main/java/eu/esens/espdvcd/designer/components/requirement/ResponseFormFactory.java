package eu.esens.espdvcd.designer.components.requirement;

import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.*;

/**
 * Created by ixuz on 4/12/16.
 */
public class ResponseFormFactory {

    public static ResponseForm buildResponseForm(Requirement requirement) {

        Response requirementResponse = requirement.getResponse();

        switch (requirement.getResponseDataType()) {
            case INDICATOR:
                IndicatorResponse indicatorResponse = (IndicatorResponse) requirementResponse;
                IndicatorResponseForm indicatorResponseForm = new IndicatorResponseForm(indicatorResponse, "Indicator Response Form");
                return indicatorResponseForm;
            case DATE:
                DateResponse dateResponse = (DateResponse) requirementResponse;
                DateResponseForm dateResponseForm = new DateResponseForm(dateResponse, "Date Response Form");
                return dateResponseForm;
            case DESCRIPTION:
                DescriptionResponse descriptionResponse = (DescriptionResponse) requirementResponse;
                DescriptionResponseForm descriptionResponseForm = new DescriptionResponseForm(descriptionResponse, "Description Response Form");
                return descriptionResponseForm;
            case QUANTITY:
                QuantityResponse quantityResponse = (QuantityResponse) requirementResponse;
                QuantityResponseForm quantityResponseForm = new QuantityResponseForm(quantityResponse, "Quantity Response Form");
                return quantityResponseForm;
            case QUANTITY_YEAR:
                QuantityYearResponse quantityYearResponse = (QuantityYearResponse) requirementResponse;
                QuantityYearResponseForm quantityYearResponseForm = new QuantityYearResponseForm(quantityYearResponse, "Quantity Year Response Form");
                return quantityYearResponseForm;
            case QUANTITY_INTEGER:
                QuantityIntegerResponse quantityIntegerResponse = (QuantityIntegerResponse) requirementResponse;
                QuantityIntegerResponseForm quantityIntegerResponseForm = new QuantityIntegerResponseForm(quantityIntegerResponse, "Quantity Integer Response Form");
                return quantityIntegerResponseForm;
            case AMOUNT:
                AmountResponse amountResponse = (AmountResponse) requirementResponse;
                AmountResponseForm amountResponseForm = new AmountResponseForm(amountResponse, "Amount Response Form");
                return amountResponseForm;
            case CODE_COUNTRY:
                CountryCodeResponse countryCodeResponse = (CountryCodeResponse) requirementResponse;
                CountryCodeResponseForm countryCodeResponseForm = new CountryCodeResponseForm(countryCodeResponse, "Country Code Response Form");
                return countryCodeResponseForm;
            case PERCENTAGE:
                PercentageResponse percentageResponse = (PercentageResponse) requirementResponse;
                PercentageResponseForm percentageResponseForm = new PercentageResponseForm(percentageResponse, "Percentage Response Form");
                return percentageResponseForm;
            case PERIOD:
                PeriodResponse periodResponse = (PeriodResponse) requirementResponse;
                PeriodResponseForm periodResponseForm = new PeriodResponseForm(periodResponse, "Period Response Form");
                return periodResponseForm;
            case EVIDENCE_URL:
                EvidenceURLResponse evidenceURLResponse = (EvidenceURLResponse) requirementResponse;
                EvidenceURLResponseForm evidenceURLResponseForm = new EvidenceURLResponseForm(evidenceURLResponse, "Evidence Url Response Form");
                return evidenceURLResponseForm;
            case CODE:
                EvidenceURLCodeResponse evidenceURLCodeResponse = (EvidenceURLCodeResponse) requirementResponse;
                EvidenceURLCodeResponseForm evidenceURLCodeResponseForm = new EvidenceURLCodeResponseForm(evidenceURLCodeResponse, "Evidence Url Response Form");
                return evidenceURLCodeResponseForm;
            default:
                return null;
        }
    }
}
