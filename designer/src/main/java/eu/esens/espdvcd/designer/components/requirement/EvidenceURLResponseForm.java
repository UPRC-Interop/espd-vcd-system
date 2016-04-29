package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class EvidenceURLResponseForm extends ResponseForm {
    private EvidenceURLResponse evidenceURLResponse = null;
    private TextField evidenceURL = new TextField("Evidence URL: ");

    public EvidenceURLResponseForm(EvidenceURLResponse evidenceURLResponse, String caption, boolean readOnly) {
        this.evidenceURLResponse = evidenceURLResponse;
        addComponent(evidenceURL);
        evidenceURL.setCaption(caption);
        evidenceURL.setNullRepresentation("");
        evidenceURL.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<EvidenceURLResponse> binder = new BeanFieldGroup<>(EvidenceURLResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.evidenceURLResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}
