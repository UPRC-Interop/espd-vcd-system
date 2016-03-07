package eu.esens.espdvcd.designer.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.builder.ESPDBuilder;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.Requirement;
import eu.esens.espdvcd.model.RequirementGroup;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ixuz on 3/7/16.
 */
public class ESPDRequestForm extends VerticalLayout {

    private ESPDRequest espdRequest = null;
    private VerticalLayout page1 = new VerticalLayout();
    private VerticalLayout page2 = new VerticalLayout();
    private VerticalLayout page3 = new VerticalLayout();
    private List<VerticalLayout> pages = new ArrayList<>();
    private HorizontalLayout buttonList = new HorizontalLayout();
    private Button previous = new Button("Previous", FontAwesome.ARROW_LEFT);
    private Button cancel = new Button("Cancel", FontAwesome.REMOVE);
    private Button next = new Button("Next", FontAwesome.ARROW_RIGHT);
    private Button finish = new Button("Export", FontAwesome.DOWNLOAD);
    private Button printFullModelData = new Button("Print full model data", FontAwesome.DOWNLOAD);
    private int currentPageIndex = 0;

    public ESPDRequestForm(ESPDRequest espdRequest) {
        this.espdRequest = espdRequest;

        setWidth("100%");
        setStyleName("espdRequestForm-layout");

        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        for (VerticalLayout page : pages) { addComponent(page); }
        addComponent(buttonList);
        buttonList.addComponent(previous);
        buttonList.addComponent(cancel);
        buttonList.addComponent(next);
        buttonList.addComponent(finish);
        buttonList.addComponent(printFullModelData);

        previous.addClickListener(this::onPrevious);
        cancel.addClickListener(this::onCancel);
        next.addClickListener(this::onNext);
        finish.addClickListener(this::onExport);
        printFullModelData.addClickListener(this::onPrintFullModelData);

        previous.setStyleName("espdRequestForm-previous");
        cancel.setStyleName("espdRequestForm-cancel");
        next.setStyleName("espdRequestForm-next");
        finish.setStyleName("espdRequestForm-finish");
        printFullModelData.setStyleName("espdRequestForm-printFullModelData");

        buttonList.setMargin(true);
        buttonList.setSpacing(true);

        page1.addComponent(new CADetailsForm(espdRequest));

        for (SelectableCriterion criterion : espdRequest.getExclusionCriteriaList()) {
            page2.addComponent(new CriterionForm(criterion));
        }
        for (SelectableCriterion criterion : espdRequest.getSelectionCriteriaList()) {
            page3.addComponent(new CriterionForm(criterion));
        }

        showPage(currentPageIndex);
    }

    private void showPage(int pageIndex) {
        for (int i=0; i<pages.size(); i++) {
            VerticalLayout page = pages.get(i);
            if (i == pageIndex) {
                page.setVisible(true);
            } else {
                page.setVisible(false);
            }
        }
        updateButtonList();
    }

    private void updateButtonList() {
        next.setEnabled((currentPageIndex+1 <= pages.size()-1));
        next.setVisible((currentPageIndex + 1 <= pages.size() - 1));
        previous.setEnabled((currentPageIndex-1 >= 0));
        previous.setVisible((currentPageIndex-1 >= 0));
        finish.setEnabled(!(currentPageIndex+1 <= pages.size()-1));
        finish.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
        printFullModelData.setEnabled(!(currentPageIndex+1 <= pages.size()-1));
        printFullModelData.setVisible(!(currentPageIndex + 1 <= pages.size() - 1));
    }

    /**
     * Displays the previous page of the form.
     * If there is no previous page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrevious(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex-1 >= 0 ? currentPageIndex-1 : currentPageIndex);
        showPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Cancel button, this method is invoked.
     * TODO: Implement logic for restarting/leaving the form page.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onCancel(Button.ClickEvent event) {
        System.out.println("Cancel process");
    }

    /**
     * Displays the next page of the form.
     * If there is no next page, this method will have no effect.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onNext(Button.ClickEvent event) {
        currentPageIndex = (currentPageIndex+1 < pages.size() ? currentPageIndex+1 : currentPageIndex);
        showPage(currentPageIndex);
    }

    /**
     * When the user have clicked the Export button, this method is invoked.
     * TODO: Implement logic for exporting all form data to xml.
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onExport(Button.ClickEvent event) {
        // Display espd request xml button
        ESPDBuilder espdBuilder = new ESPDBuilder();
        String xml = espdBuilder.createXMLasString(espdRequest);
        System.out.println("Xml: " + xml);
    }

    /**
     * Prints all data contained in this espd model(temporary usage because at the time the xml export is not full implemented)
     *
     * @param event Vaadin7 Button click event
     * @see com.vaadin.ui.Button.ClickEvent
     */
    private void onPrintFullModelData(Button.ClickEvent event) {
        System.out.println("EspdRequest().CADetails().CACountry: " + espdRequest.getCADetails().getCACountry());
        System.out.println("EspdRequest().CADetails().CAOfficialName: " + espdRequest.getCADetails().getCAOfficialName());
        System.out.println("EspdRequest().CADetails().getProcurementProcedureTitle: " + espdRequest.getCADetails().getProcurementProcedureTitle());
        System.out.println("EspdRequest().CADetails().getProcurementProcedureDesc: " + espdRequest.getCADetails().getProcurementProcedureDesc());
        System.out.println("EspdRequest().CADetails().getProcurementProcedureFileReferenceNo: " + espdRequest.getCADetails().getProcurementProcedureFileReferenceNo());
        for (SelectableCriterion criterion : espdRequest.getFullCriterionList()) {
            System.out.println("EspdRequest().Criterion().ID: " + criterion.getID());
            System.out.println("EspdRequest().Criterion().TypeCode: " + criterion.getTypeCode());
            System.out.println("EspdRequest().Criterion().Name: " + criterion.getName());
            System.out.println("EspdRequest().Criterion().Description: " + criterion.getDescription());
            System.out.println("EspdRequest().Criterion().LegislationReference().Title: " + criterion.getLegislationReference().getTitle());
            System.out.println("EspdRequest().Criterion().LegislationReference().Description: " + criterion.getLegislationReference().getDescription());
            System.out.println("EspdRequest().Criterion().LegislationReference().JurisdictionLevelCode: " + criterion.getLegislationReference().getJurisdictionLevelCode());
            System.out.println("EspdRequest().Criterion().LegislationReference().Article: " + criterion.getLegislationReference().getArticle());
            System.out.println("EspdRequest().Criterion().LegislationReference().URI: " + criterion.getLegislationReference().getURI());
            for (RequirementGroup requirementGroup : criterion.getRequirementGroups()) {
                System.out.println("EspdRequest().Criteria().RequirementGroup().ID: " + requirementGroup.getID());
                for (Requirement requirement : requirementGroup.getRequirements()) {
                    System.out.println("EspdRequest().Criteria().RequirementGroup().Requirement().ID: " + requirement.getID());
                    System.out.println("EspdRequest().Criteria().RequirementGroup().Requirement().ResponseDataType: " + requirement.getResponseDataType());
                    System.out.println("EspdRequest().Criteria().RequirementGroup().Requirement().Description: " + requirement.getDescription());
                }
            }
        }
    }
}
