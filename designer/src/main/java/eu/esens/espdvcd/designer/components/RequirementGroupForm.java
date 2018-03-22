package eu.esens.espdvcd.designer.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.Requirement;

public class RequirementGroupForm extends VerticalLayout {

    public RequirementGroupForm(RequirementGroup requirementGroup, boolean includeResponses, int displayEvidences, boolean readOnly) {

        setMargin(false);
        setSpacing(true);
        setStyleName("requirementGroupForm-layout");
        addStyleName("ignoreCaptionCellWidth");

        setWidth("100%");

        // Bind the this forms fields
//        final Binder<RequirementGroup> requirementGroupGroup = new BeanValidationBinder<>(RequirementGroup.class);
//        requirementGroupGroup.setBean(requirementGroup);
//        requirementGroupGroup.bindInstanceFields(this);
//        requirementGroupGroup.setReadOnly(readOnly);

        Panel panel = new Panel();
        panel.setStyleName("RequirementGroupFormPanel");
        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panel.setContent(panelContent);
        addComponent(panel);

        int numberOfComponentsInPanel = 0;
        // Add a sub requirement forms
        for (Requirement requirement : requirementGroup.getRequirements()) {
            //if (!(displayEvidences == 0 && requirement.getResponseDataType() == ResponseTypeEnum.EVIDENCE_URL)) {
                RequirementForm requirementForm = new RequirementForm(requirement, includeResponses, displayEvidences, readOnly);
                panelContent.addComponent(requirementForm);
                numberOfComponentsInPanel++;
            //}
        }

        // Add a sub requirement group forms
        for (RequirementGroup requirementGroupTemp : requirementGroup.getRequirementGroups()) {
            RequirementGroupForm requirementGroupForm = new RequirementGroupForm(requirementGroupTemp, includeResponses, displayEvidences, readOnly);
            panelContent.addComponent(requirementGroupForm);
            numberOfComponentsInPanel++;
        }
        if (numberOfComponentsInPanel == 0) {
            removeComponent(panel);
        }
    }
}
