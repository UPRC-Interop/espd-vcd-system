package eu.esens.espdvcd.designer.components.windows;

import com.vaadin.ui.*;
import eu.esens.espdvcd.codelist.Codelists;
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
    ComboBox typecode = new ComboBox("Type:");

    public CriterionWindow(ESPDRequest espd, Master view, AbstractLayout destinationLayout) {
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
        windowLayout.addComponent(description);
        windowLayout.addComponent(typecode);
        windowLayout.addComponent(saveButton);
        windowLayout.addComponent(closeButton);

        saveButton.addClickListener(this::onSave);
        closeButton.addClickListener(this::onClose);

        caption.setWidth(100, Unit.PERCENTAGE);
        description.setWidth(100, Unit.PERCENTAGE);

        Iterator<String> iterator = Codelists.CriteriaType.getBiMap().values().iterator();
        while (iterator.hasNext()) {
            String countryName = iterator.next();
            String countryId = Codelists.CriteriaType.getIdForData(countryName);
            typecode.addItem(countryId);
            typecode.setItemCaption(countryId, WordUtils.capitalize(countryName.toLowerCase()));
        }

        typecode.setInputPrompt("Select criterion type");
    }

    public void onSave(Button.ClickEvent clickEvent) {
        SelectableCriterion criterion = new SelectableCriterion();
        criterion.setName(caption.getValue());
        criterion.setDescription(description.getValue());
        criterion.setTypeCode((String)typecode.getValue());
        criterion.setSelected(true);
        espd.getFullCriterionList().add(criterion);
        CriterionForm criterionForm = new CriterionForm(view, criterion, false, false);
        destinationLayout.addComponent(criterionForm);
        UI.getCurrent().removeWindow(this);
        Notification.show("Your criterion has been added!");
    }

    public void onClose(Button.ClickEvent clickEvent) {
        UI.getCurrent().removeWindow(this);
    }
}
