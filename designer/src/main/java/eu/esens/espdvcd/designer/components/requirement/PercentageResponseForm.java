package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class PercentageResponseForm extends ResponseForm {
    private PercentageResponse percentageResponse = null;
    private TextField percentage = new TextField("Percentage: ");

    public PercentageResponseForm(PercentageResponse percentageResponse, String caption) {
        this.percentageResponse = percentageResponse;
        addComponent(percentage);
        setCaption(caption);
        percentage.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<PercentageResponse> binder = new BeanFieldGroup<>(PercentageResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.percentageResponse);
        binder.setBuffered(false);
    }
}
