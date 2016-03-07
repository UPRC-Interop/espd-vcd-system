package eu.esens.espdvcd.designer.views;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import eu.esens.espdvcd.designer.components.ESPDRequestForm;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.designer.components.CriteriaForm;

/**
 * Created by ixuz on 2/23/16.
 */

public class Sandbox extends Master {

    private Panel pagePanel = new Panel();
    private VerticalLayout pageContent = new VerticalLayout();

    public Sandbox(Navigator navigator) {
        super(navigator, false);

        pagePanel.setContent(pageContent);
        mainContent.addComponent(pagePanel);

        boundselect(pageContent);
    }

    void boundselect(Layout rootLayout) {

        // Prepare a POJO
        List<SelectableCriterion> exclusionCriteria = new ArrayList<SelectableCriterion>();
        List<SelectableCriterion> selectionCriteria = new ArrayList<SelectableCriterion>();

        {
            ArrayList<Requirement> requirements1 = new ArrayList<>();
            requirements1.add(new Requirement("7d35fb7c-da5b-4830-b598-4f347a04dceb", "DESCRIPTION", "Reason"));

            ArrayList<Requirement> requirements2 = new ArrayList<>();
            requirements2.add(new Requirement("974c8196-9d1c-419c-9ca9-45bb9f5fd59a", "INDICATOR", "Your answer?"));
            requirements2.add(new Requirement("ecf40999-7b64-4e10-b960-7f8ff8674cf6", "DATE", "Date of conviction"));

            ArrayList<RequirementGroup> requirementGroups1 = new ArrayList<>();
            requirementGroups1.add(new RequirementGroup("7c637c0c-7703-4389-ba52-02997a055bd7", requirements1));
            requirementGroups1.add(new RequirementGroup("41dd2e9b-1bfd-44c7-93ee-56bd74a4334b", requirements2));

            LegislationReference legislationReference = new LegislationReference("title", "description", "jurisdiction", "article", "uri");

            SelectableCriterion criterion = new SelectableCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab",
                    "EXCLUSION.CRIMINAL_CONVICTIONS",
                    "Participation in a criminal organisation",
                    "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for participation in a criminal orgnisation, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Council Framework Decision 2008/841/JHA of 24 October 2008 on the fight against organised crime (OJ L 300, 11.11.2008, p. 42).",
                    legislationReference,
                    requirementGroups1);

            exclusionCriteria.add(criterion);
        }
        {
            ArrayList<Requirement> requirements1 = new ArrayList<>();
            requirements1.add(new Requirement("7d35fb7c-da5b-4830-b598-4f347a04dceb", "DESCRIPTION", "Reason"));

            ArrayList<Requirement> requirements2 = new ArrayList<>();
            requirements2.add(new Requirement("974c8196-9d1c-419c-9ca9-45bb9f5fd59a", "INDICATOR", "Your answer?"));
            requirements2.add(new Requirement("ecf40999-7b64-4e10-b960-7f8ff8674cf6", "DATE", "Date of conviction"));

            ArrayList<RequirementGroup> requirementGroups1 = new ArrayList<>();
            requirementGroups1.add(new RequirementGroup("7c637c0c-7703-4389-ba52-02997a055bd7", requirements1));
            requirementGroups1.add(new RequirementGroup("41dd2e9b-1bfd-44c7-93ee-56bd74a4334b", requirements2));

            LegislationReference legislationReference = new LegislationReference("title", "description", "jurisdiction", "article", "uri");

            SelectableCriterion criterion = new SelectableCriterion("005eb9ed-1347-4ca3-bb29-9bc0db64e1ab",
                    "EXCLUSION.CRIMINAL_CONVICTIONS",
                    "Participation in a criminal organisation",
                    "Has the economic operator itself or any person who is a member of its administrative, management or supervisory body or has powers of representation, decision or control therein been the subject of a conviction by final judgment for participation in a criminal orgnisation, by a conviction rendered at the most five years ago or in which an exclusion period set out directly in the conviction continues to be applicable? As defined in Article 2 of Council Framework Decision 2008/841/JHA of 24 October 2008 on the fight against organised crime (OJ L 300, 11.11.2008, p. 42).",
                    legislationReference,
                    requirementGroups1);

            selectionCriteria.add(criterion);
        }

        CADetails caDetails = new CADetails();
        caDetails.setCAOfficialName("CAOfficialName");
        caDetails.setCACountry("SE");
        caDetails.setProcurementProcedureTitle("ProcurementProcedureTitle");
        caDetails.setProcurementProcedureDesc("ProcurementProcedureDescription");
        caDetails.setProcurementProcedureFileReferenceNo("ProcurementProcedureFileReferenceNumber");

        ESPDRequestImpl espdRequest = new ESPDRequestImpl();
        espdRequest.setCADetails(caDetails);
        espdRequest.setExclusionCriteria(exclusionCriteria);
        espdRequest.setSelectionCriteria(selectionCriteria);

        // Cenerate the espd request form base on the provided espd request model
        ESPDRequestForm espdRequestForm = new ESPDRequestForm(espdRequest);
        rootLayout.addComponent(espdRequestForm);

/*
        // Generate the critera form based on the provided Bean/PROJ
        CriteriaForm criteriaForm = new CriteriaForm(criteria);
        mainContent.addComponent(criteriaForm);

        // Button that prints the values of the Bean/PROJ
        mainContent.addComponent(new Button("Save", (ClickEvent event) -> {
            System.out.println("Criteria().ID: " + criteria.getID());
            System.out.println("Criteria().TypeCode: " + criteria.getTypeCode());
            System.out.println("Criteria().Name: " + criteria.getName());
            System.out.println("Criteria().Description: " + criteria.getDescription());
            for (RequirementGroup requirementGroup : criteria.getRequirementGroups()) {
                System.out.println("Criteria().RequirementGroup.ID: " + requirementGroup.getID());
                for (Requirement requirement : requirementGroup.getRequirements()) {
                    System.out.println("Criteria().RequirementGroup().Requirement().ID: " + requirement.getID());
                    System.out.println("Criteria().RequirementGroup().Requirement().Description: " + requirement.getDescription());
                    System.out.println("Criteria().RequirementGroup().Requirement().ResponseDataType: " + requirement.getResponseDataType());
                }
            }
        }));*/
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
    }
}
