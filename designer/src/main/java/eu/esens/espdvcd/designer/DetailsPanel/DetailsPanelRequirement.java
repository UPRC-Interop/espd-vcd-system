package eu.esens.espdvcd.designer.DetailsPanel;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

/**
 * Created by ixuz on 6/23/16.
 */
public final class DetailsPanelRequirement extends VerticalLayout {

    RequirementGroup requirementGroup;
    Requirement requirement;
    HorizontalLayout columns = new HorizontalLayout();
    VerticalLayout columnLeft = new VerticalLayout();
    VerticalLayout columnRight = new VerticalLayout();
    VerticalLayout requirementLayout = new VerticalLayout();

    Button deleteRequirementButton = new Button("");

    public DetailsPanelRequirement(RequirementGroup requirementGroup, Requirement requirement) {
        this.requirementGroup = requirementGroup;
        this.requirement = requirement;
        setStyleName("detailsPanelRequirement");

        columns.setSizeFull();

        addComponent(columns);
        columns.addComponent(columnLeft);
        columns.addComponent(columnRight);
        columns.setComponentAlignment(columnLeft, Alignment.TOP_LEFT);
        columns.setComponentAlignment(columnRight, Alignment.TOP_RIGHT);
        columns.setExpandRatio(columnLeft, 0.8f);
        columns.setExpandRatio(columnRight, 0.2f);

        columnRight.addComponent(deleteRequirementButton);
        columnRight.setComponentAlignment(deleteRequirementButton, Alignment.TOP_RIGHT);

        columnLeft.addComponent(requirementLayout);
        requirementLayout.setMargin(true);

        Label typeLabel = new Label(requirementResponseDataTypeToString(requirement.getResponseDataType()));
        typeLabel.setCaption("Type");
        requirementLayout.addComponent(typeLabel);

        Label descriptionLabel = new Label(requirement.getDescription());
        descriptionLabel.setCaption("Description");
        requirementLayout.addComponent(descriptionLabel);

        deleteRequirementButton.setStyleName("detailsPanelRequirementGroupDeleteButton");
        deleteRequirementButton.setIcon(FontAwesome.REMOVE);

        deleteRequirementButton.addClickListener((clickEvent) -> {
            deleteRequirement();

            ComponentContainer parent = (ComponentContainer) getParent();
            parent.removeComponent(this);
        });
    }

    public void deleteRequirement() {
        requirementGroup.getRequirements().remove(requirement);
    }

    public RequirementGroup getRequirementGroup() {
        return requirementGroup;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public static String requirementResponseDataTypeToString(ResponseTypeEnum responseType) {
        switch (responseType) {
            case INDICATOR:
                return "INDICATOR";
            case DATE:
                return "DATE";
            case DESCRIPTION:
                return "DESCRIPTION";
            case QUANTITY:
                return "QUANTITY";
            case QUANTITY_YEAR:
                return "QUANTITY_YEAR";
            case QUANTITY_INTEGER:
                return "QUANTITY_INTEGER";
            case AMOUNT:
                return "AMOUNT";
            case CODE_COUNTRY:
                return "CODE_COUNTRY";
            case PERCENTAGE:
                return "PERCENTAGE";
            case PERIOD:
                return "PERIOD";
            case EVIDENCE_URL:
                return "EVIDENCE_URL";
            case CODE:
                return "CODE";
            default:
                return null;
        }
    }

    public static ResponseTypeEnum requirementResponseDataStringToType(String responseType) {
        switch (responseType) {
            case "INDICATOR":
                return ResponseTypeEnum.INDICATOR;
            case "DATE":
                return ResponseTypeEnum.DATE;
            case "DESCRIPTION":
                return ResponseTypeEnum.DESCRIPTION;
            case "QUANTITY":
                return ResponseTypeEnum.QUANTITY;
            case "QUANTITY_YEAR":
                return ResponseTypeEnum.QUANTITY_YEAR;
            case "QUANTITY_INTEGER":
                return ResponseTypeEnum.QUANTITY_INTEGER;
            case "AMOUNT":
                return ResponseTypeEnum.AMOUNT;
            case "CODE_COUNTRY":
                return ResponseTypeEnum.CODE_COUNTRY;
            case "PERCENTAGE":
                return ResponseTypeEnum.PERCENTAGE;
            case "PERIOD":
                return ResponseTypeEnum.PERIOD;
            case "EVIDENCE_URL":
                return ResponseTypeEnum.EVIDENCE_URL;
            case "CODE":
                return ResponseTypeEnum.CODE;
            default:
                return null;
        }
    }
}
