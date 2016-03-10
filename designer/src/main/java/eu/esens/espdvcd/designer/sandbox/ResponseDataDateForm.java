package eu.esens.espdvcd.designer.sandbox;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by ixuz on 3/8/16.
 */
public class ResponseDataDateForm extends VerticalLayout {

    private DateField date = new DateField();

    public ResponseDataDateForm(PojoDate responseDataDate) {
        addComponent(date);

        // Bind the this forms fields
        final BeanFieldGroup<PojoDate> bindGroup = new BeanFieldGroup<>(PojoDate.class);
        bindGroup.setItemDataSource(responseDataDate);
        bindGroup.setBuffered(false);
        bindGroup.bindMemberFields(this);
    }
}
