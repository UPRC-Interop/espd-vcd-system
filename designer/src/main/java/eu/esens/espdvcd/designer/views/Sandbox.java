package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.designer.components.EODetailsForm;
import eu.esens.espdvcd.model.ContactingDetails;
import eu.esens.espdvcd.model.EODetails;
import eu.esens.espdvcd.model.NaturalPerson;
import eu.esens.espdvcd.model.PostalAddress;
import eu.esens.espdvcd.model.requirement.response.Responses;
import eu.esens.espdvcd.designer.components.requirement.*;
import eu.esens.espdvcd.model.requirement.response.AmountResponse;
import eu.esens.espdvcd.model.requirement.response.CountryCodeResponse;
import eu.esens.espdvcd.model.requirement.response.DateResponse;
import eu.esens.espdvcd.model.requirement.response.DescriptionResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;
import eu.esens.espdvcd.model.requirement.response.PeriodResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityYearResponse;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master {

    private static final long serialVersionUID = -3712031177853996322L;

    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();

    public Sandbox(Navigator navigator) {
        super(navigator, false);

        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);

        pageContent.setMargin(true);
        pageContent.setSpacing(true);

        { // IndicatorResponse
            IndicatorResponse indicatorResponse = (IndicatorResponse) Responses.createResponse(Responses.Type.INDICATOR);
            IndicatorResponseForm indicatorResponseForm = new IndicatorResponseForm(indicatorResponse, "Indicator test case");
            pageContent.addComponent(indicatorResponseForm);

            Button testButton = new Button("Test IndicatorResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- IndicatorResponse.indicator: " + indicatorResponse.isIndicator());
            });
            indicatorResponseForm.addComponent(testButton);
        }

        { // DescriptionResponse
            DescriptionResponse descriptionResponse = (DescriptionResponse) Responses.createResponse(Responses.Type.DESCRIPTION);
            DescriptionResponseForm descriptionResponseForm = new DescriptionResponseForm(descriptionResponse, "DescriptionResponse test case");
            pageContent.addComponent(descriptionResponseForm);

            Button testButton = new Button("Test DescriptionResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DescriptionResponse.description: " + descriptionResponse.getDescription());
            });
            descriptionResponseForm.addComponent(testButton);
        }

        { // DateResponseForm
            DateResponse dateResponse = (DateResponse) Responses.createResponse(Responses.Type.DATE);
            DateResponseForm dateResponseForm = new DateResponseForm(dateResponse, "DateResponse test case");
            pageContent.addComponent(dateResponseForm);

            Button testButton = new Button("Test DateResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DateResponse.date: " + dateResponse.getDate());
            });
            dateResponseForm.addComponent(testButton);
        }

        { // QuantityResponseForm
            QuantityResponse quantityResponse = (QuantityResponse) Responses.createResponse(Responses.Type.QUANTITY);
            QuantityResponseForm quantityResponseForm = new QuantityResponseForm(quantityResponse, "QuantityResponse test case");
            pageContent.addComponent(quantityResponseForm);

            Button testButton = new Button("Test QuantityResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityResponse.quantity: " + quantityResponse.getQuantity());
            });
            quantityResponseForm.addComponent(testButton);
        }

        { // QuantityYearResponseForm
            QuantityYearResponse quantityYearResponse = (QuantityYearResponse) Responses.createResponse(Responses.Type.QUANTITY_YEAR);
            QuantityYearResponseForm quantityYearResponseForm = new QuantityYearResponseForm(quantityYearResponse, "QuantityYearResponse test case");
            pageContent.addComponent(quantityYearResponseForm);

            Button testButton = new Button("Test QuantityYearResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityYearResponse.year: " + quantityYearResponse.getYear());
            });
            quantityYearResponseForm.addComponent(testButton);
        }

        { // QuantityIntegerResponseForm
            QuantityIntegerResponse quantityIntegerResponse = (QuantityIntegerResponse) Responses.createResponse(Responses.Type.QUANTITY_INTEGER);
            QuantityIntegerResponseForm quantityIntegerResponseForm = new QuantityIntegerResponseForm(quantityIntegerResponse, "QuantityIntegerResponse test case");
            pageContent.addComponent(quantityIntegerResponseForm);

            Button testButton = new Button("Test QuantityIntegerResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityIntegerResponse.quantity: " + quantityIntegerResponse.getQuantity());
            });
            quantityIntegerResponseForm.addComponent(testButton);
        }

        { // AmountResponseForm
            AmountResponse amountResponse = (AmountResponse) Responses.createResponse(Responses.Type.AMOUNT);
            AmountResponseForm amountResponseForm = new AmountResponseForm(amountResponse, "AmonuntResponse test case");
            pageContent.addComponent(amountResponseForm);

            Button testButton = new Button("Test AmountResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- AmountResponse.amount: " + amountResponse.getAmount());
                System.out.println("- AmountResponse.currency: " + amountResponse.getCurrency());
            });
            amountResponseForm.addComponent(testButton);
        }

        { // CountryCodeResponseForm
            CountryCodeResponse countryCodeResponse = (CountryCodeResponse) Responses.createResponse(Responses.Type.CODE_COUNTRY);
            CountryCodeResponseForm countryCodeResponseForm = new CountryCodeResponseForm(countryCodeResponse, "CountryCodeResponse test case");
            pageContent.addComponent(countryCodeResponseForm);

            Button testButton = new Button("Test CountryCodeResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- CountryCodeResponse.countryCode: " + countryCodeResponse.getCountryCode());
            });
            countryCodeResponseForm.addComponent(testButton);
        }

        { // PercentageResponseForm
            PercentageResponse percentageResponse = (PercentageResponse) Responses.createResponse(Responses.Type.PERCENTAGE);
            PercentageResponseForm percentageResponseForm = new PercentageResponseForm(percentageResponse, "PercentageResponse test case");
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PercentageResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PercentageResponse.percentage: " + percentageResponse.getPercentage());
            });
            percentageResponseForm.addComponent(testButton);
        }

        { // PeriodResponseForm
            PeriodResponse periodResponse = (PeriodResponse) Responses.createResponse(Responses.Type.PERIOD);
            PeriodResponseForm percentageResponseForm = new PeriodResponseForm(periodResponse, "PeriodResponse test case");
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PeriodResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PeriodResponse.description: " + periodResponse.getDescription());
            });
            percentageResponseForm.addComponent(testButton);
        }

        { // EvidenceURLResponseForm
            EvidenceURLResponse evidenceURLResponse = (EvidenceURLResponse) Responses.createResponse(Responses.Type.EVIDENCE_URL);
            EvidenceURLResponseForm evidenceURLResponseForm = new EvidenceURLResponseForm(evidenceURLResponse, "EvidenceURLResponse test case");
            pageContent.addComponent(evidenceURLResponseForm);

            Button testButton = new Button("Test EvidenceURLResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- EvidenceURLResponse.evidenceURL: " + evidenceURLResponse.getEvidenceURL());
            });
            evidenceURLResponseForm.addComponent(testButton);
        }

        { // EvidenceURLCodeResponseForm
            EvidenceURLCodeResponse evidenceURLCodeResponse = (EvidenceURLCodeResponse) Responses.createResponse(Responses.Type.CODE);
            EvidenceURLCodeResponseForm evidenceURLCodeResponseForm = new EvidenceURLCodeResponseForm(evidenceURLCodeResponse, "EvidenceURLCodeResponse test case");
            pageContent.addComponent(evidenceURLCodeResponseForm);

            Button testButton = new Button("Test EvidenceURLCodeResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- EvidenceURLCodeResponse.evidenceURLCode: " + evidenceURLCodeResponse.getEvidenceURLCode());
            });
            evidenceURLCodeResponseForm.addComponent(testButton);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    private void onPrint(Button.ClickEvent event) {

    }
}
