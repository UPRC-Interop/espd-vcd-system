package eu.esens.espdvcd.designer.components.windows;

import com.vaadin.ui.*;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.components.CriterionGroupForm;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;

/**
 * Created by ixuz on 6/21/16.
 */
public class CriterionGroupWindow extends Window {

    ESPDRequest espd;
    Master view;
    VerticalLayout windowLayout = new VerticalLayout();
    AbstractLayout destinationLayout;
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Close");
    TextField caption = new TextField("Caption:");

    public CriterionGroupWindow(ESPDRequest espd, Master view, AbstractLayout destinationLayout) {
        this.espd = espd;
        this.view = view;
        this.destinationLayout = destinationLayout;

        setContent(windowLayout);
        setWidth(600, Unit.PIXELS);

        center();
        setClosable(false);
        setDraggable(false);
        setResizable(false);
        setModal(true);

        windowLayout.setMargin(true);
        windowLayout.setSpacing(true);
        windowLayout.addComponent(caption);
        windowLayout.addComponent(saveButton);
        windowLayout.addComponent(closeButton);

        saveButton.addClickListener(this::onSave);
        closeButton.addClickListener(this::onClose);

        caption.setWidth(100, Unit.PERCENTAGE);
    }

    public void onSave(Button.ClickEvent clickEvent) {
        CriterionGroupForm criterionGroupForm = new CriterionGroupForm(espd, view, caption.getValue(), null);
        destinationLayout.addComponent(criterionGroupForm);
        UI.getCurrent().removeWindow(this);
    }

    public void onClose(Button.ClickEvent clickEvent) {
        UI.getCurrent().removeWindow(this);
    }
}
