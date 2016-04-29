package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;

import eu.esens.espdvcd.model.requirement.response.ResponseFactory;
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
            IndicatorResponse indicatorResponse = (IndicatorResponse) ResponseFactory.createResponse(ResponseTypeEnum.INDICATOR);
            IndicatorResponseForm indicatorResponseForm = new IndicatorResponseForm(indicatorResponse, "Indicator test case", true);
            pageContent.addComponent(indicatorResponseForm);

            Button testButton = new Button("Test IndicatorResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- IndicatorResponse.indicator: " + indicatorResponse.isIndicator());
            });
            indicatorResponseForm.addComponent(testButton);
        }

        { // DescriptionResponse
            DescriptionResponse descriptionResponse = (DescriptionResponse) ResponseFactory.createResponse(ResponseTypeEnum.DESCRIPTION);
            DescriptionResponseForm descriptionResponseForm = new DescriptionResponseForm(descriptionResponse, "DescriptionResponse test case", true);
            pageContent.addComponent(descriptionResponseForm);

            Button testButton = new Button("Test DescriptionResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DescriptionResponse.description: " + descriptionResponse.getDescription());
            });
            descriptionResponseForm.addComponent(testButton);
        }

        { // DateResponseForm
            DateResponse dateResponse = (DateResponse) ResponseFactory.createResponse(ResponseTypeEnum.DATE);
            DateResponseForm dateResponseForm = new DateResponseForm(dateResponse, "DateResponse test case", true);
            pageContent.addComponent(dateResponseForm);

            Button testButton = new Button("Test DateResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DateResponse.date: " + dateResponse.getDate());
            });
            dateResponseForm.addComponent(testButton);
        }

        { // QuantityResponseForm
            QuantityResponse quantityResponse = (QuantityResponse) ResponseFactory.createResponse(ResponseTypeEnum.QUANTITY);
            QuantityResponseForm quantityResponseForm = new QuantityResponseForm(quantityResponse, "QuantityResponse test case", true);
            pageContent.addComponent(quantityResponseForm);

            Button testButton = new Button("Test QuantityResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityResponse.quantity: " + quantityResponse.getQuantity());
            });
            quantityResponseForm.addComponent(testButton);
        }

        { // QuantityYearResponseForm
            QuantityYearResponse quantityYearResponse = (QuantityYearResponse) ResponseFactory.createResponse(ResponseTypeEnum.QUANTITY_YEAR);
            QuantityYearResponseForm quantityYearResponseForm = new QuantityYearResponseForm(quantityYearResponse, "QuantityYearResponse test case", true);
            pageContent.addComponent(quantityYearResponseForm);

            Button testButton = new Button("Test QuantityYearResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityYearResponse.year: " + quantityYearResponse.getYear());
            });
            quantityYearResponseForm.addComponent(testButton);
        }

        { // QuantityIntegerResponseForm
            QuantityIntegerResponse quantityIntegerResponse = (QuantityIntegerResponse) ResponseFactory.createResponse(ResponseTypeEnum.QUANTITY_INTEGER);
            QuantityIntegerResponseForm quantityIntegerResponseForm = new QuantityIntegerResponseForm(quantityIntegerResponse, "QuantityIntegerResponse test case", true);
            pageContent.addComponent(quantityIntegerResponseForm);

            Button testButton = new Button("Test QuantityIntegerResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityIntegerResponse.quantity: " + quantityIntegerResponse.getQuantity());
            });
            quantityIntegerResponseForm.addComponent(testButton);
        }

        { // AmountResponseForm
            AmountResponse amountResponse = (AmountResponse) ResponseFactory.createResponse(ResponseTypeEnum.AMOUNT);
            AmountResponseForm amountResponseForm = new AmountResponseForm(amountResponse, "AmonuntResponse test case", true);
            pageContent.addComponent(amountResponseForm);

            Button testButton = new Button("Test AmountResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- AmountResponse.amount: " + amountResponse.getAmount());
                System.out.println("- AmountResponse.currency: " + amountResponse.getCurrency());
            });
            amountResponseForm.addComponent(testButton);
        }

        { // CountryCodeResponseForm
            CountryCodeResponse countryCodeResponse = (CountryCodeResponse) ResponseFactory.createResponse(ResponseTypeEnum.CODE_COUNTRY);
            CountryCodeResponseForm countryCodeResponseForm = new CountryCodeResponseForm(countryCodeResponse, "CountryCodeResponse test case", true);
            pageContent.addComponent(countryCodeResponseForm);

            Button testButton = new Button("Test CountryCodeResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- CountryCodeResponse.countryCode: " + countryCodeResponse.getCountryCode());
            });
            countryCodeResponseForm.addComponent(testButton);
        }

        { // PercentageResponseForm
            PercentageResponse percentageResponse = (PercentageResponse) ResponseFactory.createResponse(ResponseTypeEnum.PERCENTAGE);
            PercentageResponseForm percentageResponseForm = new PercentageResponseForm(percentageResponse, "PercentageResponse test case", true);
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PercentageResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PercentageResponse.percentage: " + percentageResponse.getPercentage());
            });
            percentageResponseForm.addComponent(testButton);
        }

        { // PeriodResponseForm
            PeriodResponse periodResponse = (PeriodResponse) ResponseFactory.createResponse(ResponseTypeEnum.PERIOD);
            PeriodResponseForm percentageResponseForm = new PeriodResponseForm(periodResponse, "PeriodResponse test case", true);
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PeriodResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PeriodResponse.description: " + periodResponse.getDescription());
            });
            percentageResponseForm.addComponent(testButton);
        }

        { // EvidenceURLResponseForm
            EvidenceURLResponse evidenceURLResponse = (EvidenceURLResponse) ResponseFactory.createResponse(ResponseTypeEnum.EVIDENCE_URL);
            EvidenceURLResponseForm evidenceURLResponseForm = new EvidenceURLResponseForm(evidenceURLResponse, "EvidenceURLResponse test case", true);
            pageContent.addComponent(evidenceURLResponseForm);

            Button testButton = new Button("Test EvidenceURLResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- EvidenceURLResponse.evidenceURL: " + evidenceURLResponse.getEvidenceURL());
            });
            evidenceURLResponseForm.addComponent(testButton);
        }

        { // EvidenceURLCodeResponseForm
            EvidenceURLCodeResponse evidenceURLCodeResponse = (EvidenceURLCodeResponse) ResponseFactory.createResponse(ResponseTypeEnum.CODE);
            EvidenceURLCodeResponseForm evidenceURLCodeResponseForm = new EvidenceURLCodeResponseForm(evidenceURLCodeResponse, "EvidenceURLCodeResponse test case", true);
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
