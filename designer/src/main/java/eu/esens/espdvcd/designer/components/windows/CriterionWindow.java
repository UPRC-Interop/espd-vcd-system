package eu.esens.espdvcd.designer.components.windows;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.CodeListV1;
import eu.esens.espdvcd.designer.components.CriterionForm;
import eu.esens.espdvcd.designer.views.Master;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Iterator;

/**
 * Created by ixuz on 6/21/16.
 */
public class CriterionWindow extends Window {

    ESPDRequest espd;
    Master view;
    VerticalLayout windowLayout = new VerticalLayout();
    AbstractLayout destinationLayout;
    Button saveButton = new Button("Save");
    Button closeButton = new Button("Close");
    TextField caption = new TextField("Caption:");
    TextArea description = new TextArea("Description:");
    ComboBox<String> typecode = new ComboBox("Type:");
    int displayEvidences;

    public CriterionWindow(ESPDRequest espd, Master view, AbstractLayout destinationLayout, int displayEvidences) {
        this.espd = espd;
        this.view = view;
        this.destinationLayout = destinationLayout;
        this.displayEvidences = displayEvidences;

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
        windowLayout.addComponent(description);
        windowLayout.addComponent(typecode);
        windowLayout.addComponent(saveButton);
        windowLayout.addComponent(closeButton);

        saveButton.addClickListener(this::onSave);
        closeButton.addClickListener(this::onClose);

        caption.setWidth(100, Unit.PERCENTAGE);
        description.setWidth(100, Unit.PERCENTAGE);

        typecode.setItems(CodeListV1.CriteriaType.getBiMap().keySet());
        typecode.setItemCaptionGenerator(i -> WordUtils.capitalize(CodeListV1.CriteriaType.getValueForId(i).toLowerCase()));
        typecode.setPlaceholder("Select criterion type");
    }

    public void onSave(Button.ClickEvent clickEvent) {
        SelectableCriterion criterion = new SelectableCriterion();
        criterion.setName(caption.getValue());
        criterion.setDescription(description.getValue());
        criterion.setTypeCode((String)typecode.getValue());
        criterion.setSelected(true);
        espd.getFullCriterionList().add(criterion);
        CriterionForm criterionForm = new CriterionForm(view, criterion, false, displayEvidences, false, null);
        destinationLayout.addComponent(criterionForm);
        UI.getCurrent().removeWindow(this);
        Notification notification = new Notification("Your criterion has been added!");
        notification.setPosition(Position.TOP_CENTER);
        notification.setDelayMsec(1000);
        notification.show(Page.getCurrent());
    }

    public void onClose(Button.ClickEvent clickEvent) {
        UI.getCurrent().removeWindow(this);
    }
}
