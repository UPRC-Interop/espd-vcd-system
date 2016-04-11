package eu.esens.espdvcd.designer.views;

import com.ibm.icu.impl.duration.Period;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Arrays;

import eu.esens.espdvcd.builder.Responses;
import eu.esens.espdvcd.designer.components.CountryComboBox;
import eu.esens.espdvcd.designer.components.requirement.*;
import eu.esens.espdvcd.model.requirement.*;

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

        // INDICATOR
        pageContent.addComponent(new Label("INDICATOR"));
        pageContent.addComponent(new CheckBox("Yes/No"));

        // DESCRIPTION/PERIOD_DESCRIPTION/EVIDENCE_URL/CODE
        pageContent.addComponent(new Label("DESCRIPTION/PERIOD_DESCRIPTION/EVIDENCE_URL/CODE"));
        pageContent.addComponent(new TextField("Text"));

        // AMOUNT
        pageContent.addComponent(new Label("AMOUNT"));
        pageContent.addComponent(new ComboBox("Select currency", Arrays.asList("EUR", "SEK")));
        pageContent.addComponent(new TextField("Amount"));

        // COUNTRY
        pageContent.addComponent(new Label("COUNTRY"));
        pageContent.addComponent(new CountryComboBox("Select country"));

        // DATE
        pageContent.addComponent(new Label("DATE"));

        // Just a button that prints stuff
        Button printButton = new Button("Print stuff");
        printButton.addClickListener(this::onPrint);
        pageContent.addComponent(printButton);

        Label testLabel = new Label("Test Labels");
        testLabel.addStyleName("testingstyle");
        testLabel.removeStyleName("testingstyle");
        pageContent.addComponent(testLabel);

        // Requirement response forms

        { // DescriptionResponse
            DescriptionResponse descriptionResponse = (DescriptionResponse) Responses.createResponse(Responses.Type.DESCRIPTION);
            DescriptionResponseForm descriptionResponseForm = new DescriptionResponseForm(descriptionResponse);
            pageContent.addComponent(descriptionResponseForm);

            Button testButton = new Button("Test DescriptionResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DescriptionResponse.description: " + descriptionResponse.getDescription());
            });
            pageContent.addComponent(testButton);
        }

        { // DateResponseForm
            DateResponse dateResponse = (DateResponse) Responses.createResponse(Responses.Type.DATE);
            DateResponseForm dateResponseForm = new DateResponseForm(dateResponse);
            pageContent.addComponent(dateResponseForm);

            Button testButton = new Button("Test DateResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- DateResponse.date: " + dateResponse.getDate());
            });
            pageContent.addComponent(testButton);
        }

        { // QuantityResponseForm
            QuantityResponse quantityResponse = (QuantityResponse) Responses.createResponse(Responses.Type.QUANTITY);
            QuantityResponseForm quantityResponseForm = new QuantityResponseForm(quantityResponse);
            pageContent.addComponent(quantityResponseForm);

            Button testButton = new Button("Test QuantityResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityResponse.quantity: " + quantityResponse.getQuantity());
            });
            pageContent.addComponent(testButton);
        }

        { // QuantityYearResponseForm
            QuantityYearResponse quantityYearResponse = (QuantityYearResponse) Responses.createResponse(Responses.Type.QUANTITY_YEAR);
            QuantityYearResponseForm quantityYearResponseForm = new QuantityYearResponseForm(quantityYearResponse);
            pageContent.addComponent(quantityYearResponseForm);

            Button testButton = new Button("Test QuantityYearResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityYearResponse.year: " + quantityYearResponse.getYear());
            });
            pageContent.addComponent(testButton);
        }

        { // QuantityIntegerResponseForm
            QuantityIntegerResponse quantityIntegerResponse = (QuantityIntegerResponse) Responses.createResponse(Responses.Type.QUANTITY_INTEGER);
            QuantityIntegerResponseForm quantityIntegerResponseForm = new QuantityIntegerResponseForm(quantityIntegerResponse);
            pageContent.addComponent(quantityIntegerResponseForm);

            Button testButton = new Button("Test QuantityIntegerResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- QuantityIntegerResponse.quantity: " + quantityIntegerResponse.getQuantity());
            });
            pageContent.addComponent(testButton);
        }

        { // AmountResponseForm
            AmountResponse amountResponse = (AmountResponse) Responses.createResponse(Responses.Type.AMOUNT);
            AmountResponseForm amountResponseForm = new AmountResponseForm(amountResponse);
            pageContent.addComponent(amountResponseForm);

            Button testButton = new Button("Test QuantityIntegerResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- AmountResponse.amount: " + amountResponse.getAmount());
                System.out.println("- AmountResponse.currency: " + amountResponse.getCurrency());
            });
            pageContent.addComponent(testButton);
        }

        { // CountryCodeResponseForm
            CountryCodeResponse countryCodeResponse = (CountryCodeResponse) Responses.createResponse(Responses.Type.CODE_COUNTRY);
            CountryCodeResponseForm countryCodeResponseForm = new CountryCodeResponseForm(countryCodeResponse);
            pageContent.addComponent(countryCodeResponseForm);

            Button testButton = new Button("Test CountryCodeResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- CountryCodeResponse.countryCode: " + countryCodeResponse.getCountryCode());
            });
            pageContent.addComponent(testButton);
        }

        { // PercentageResponseForm
            PercentageResponse percentageResponse = (PercentageResponse) Responses.createResponse(Responses.Type.PERCENTAGE);
            PercentageResponseForm percentageResponseForm = new PercentageResponseForm(percentageResponse);
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PercentageResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PercentageResponse.percentage: " + percentageResponse.getPercentage());
            });
            pageContent.addComponent(testButton);
        }

        { // PeriodResponseForm
            PeriodResponse periodResponse = (PeriodResponse) Responses.createResponse(Responses.Type.PERIOD);
            PeriodResponseForm percentageResponseForm = new PeriodResponseForm(periodResponse);
            pageContent.addComponent(percentageResponseForm);

            Button testButton = new Button("Test PeriodResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- PeriodResponse.description: " + periodResponse.getDescription());
            });
            pageContent.addComponent(testButton);
        }

        { // EvidenceURLResponseForm
            EvidenceURLResponse evidenceURLResponse = (EvidenceURLResponse) Responses.createResponse(Responses.Type.EVIDENCE_URL);
            EvidenceURLResponseForm evidenceURLResponseForm = new EvidenceURLResponseForm(evidenceURLResponse);
            pageContent.addComponent(evidenceURLResponseForm);

            Button testButton = new Button("Test EvidenceURLResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- EvidenceURLResponse.evidenceURL: " + evidenceURLResponse.getEvidenceURL());
            });
            pageContent.addComponent(testButton);
        }

        { // EvidenceURLCodeResponseForm
            EvidenceURLCodeResponse evidenceURLCodeResponse = (EvidenceURLCodeResponse) Responses.createResponse(Responses.Type.CODE);
            EvidenceURLCodeResponseForm evidenceURLCodeResponseForm = new EvidenceURLCodeResponseForm(evidenceURLCodeResponse);
            pageContent.addComponent(evidenceURLCodeResponseForm);

            Button testButton = new Button("Test EvidenceURLCodeResponse");
            testButton.addClickListener((clickEvent) -> {
                System.out.println("- EvidenceURLCodeResponse.evidenceURLCode: " + evidenceURLCodeResponse.getEvidenceURLCode());
            });
            pageContent.addComponent(testButton);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }

    private void onPrint(Button.ClickEvent event) {

    }
}
