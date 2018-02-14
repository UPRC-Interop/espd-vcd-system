package eu.esens.espdvcd.designer.components.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.DetailsPanel.DetailsPanelRequirement;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Created by ixuz on 6/23/16.
 */
public class RequirementWindow extends Window {

    RequirementGroup requirementGroup;
    AbstractLayout destinationLayout = null;
    VerticalLayout windowLayout = new VerticalLayout();

    TextField id = new TextField("ID");
    ComboBox<ResponseTypeEnum> type = new ComboBox("Type");
    TextArea description = new TextArea("Description");

    HorizontalLayout buttonBar = new HorizontalLayout();
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Close");

    UUID generatedUUID = UUID.randomUUID();

    public RequirementWindow(RequirementGroup requirementGroup, AbstractLayout destinationLayout) {
        this.requirementGroup = requirementGroup;
        this.destinationLayout = destinationLayout;

        setCaption("Requirement window");
        setContent(windowLayout);
        setWidth(600, Unit.PIXELS);

        center();
        setClosable(false);
        setDraggable(false);
        setResizable(false);
        setModal(true);

        windowLayout.setMargin(true);
        windowLayout.setSpacing(true);

        windowLayout.addComponent(id);
        windowLayout.addComponent(type);
        windowLayout.addComponent(description);

        windowLayout.addComponent(buttonBar);
        buttonBar.addComponent(saveButton);
        buttonBar.addComponent(closeButton);

        saveButton.addClickListener(this::onSave);
        closeButton.addClickListener(this::onClose);

        id.setWidth(100, Unit.PERCENTAGE);
        type.setWidth(100, Unit.PERCENTAGE);
        description.setWidth(100, Unit.PERCENTAGE);

        id.setValue(generatedUUID.toString());
        id.setReadOnly(true);
        
        type.setItems(CodelistsV1.ResponseDataType.getDataMap().keySet().stream()
                .map(respId -> DetailsPanelRequirement.requirementResponseDataStringToType(respId)));
        type.setItemCaptionGenerator(i -> WordUtils.capitalize(CodelistsV1.ResponseDataType.getValueForId(i.name()).toLowerCase()));

    }

    public void onSave(Button.ClickEvent clickEvent) {

        RequestRequirement requirement = new RequestRequirement(id.getValue(), (ResponseTypeEnum)type.getValue(), description.getValue());
        DetailsPanelRequirement detailsPanelRequirement = new DetailsPanelRequirement(requirementGroup, requirement);
        destinationLayout.addComponent(detailsPanelRequirement);

        requirementGroup.getRequirements().add(requirement);

        Notification notification = new Notification("Your requirement has been added!");
        notification.setPosition(Position.TOP_CENTER);
        notification.setDelayMsec(1000);
        notification.show(Page.getCurrent());

        UI.getCurrent().removeWindow(this);
    }

    public void onClose(Button.ClickEvent clickEvent) {
        UI.getCurrent().removeWindow(this);
    }
}
