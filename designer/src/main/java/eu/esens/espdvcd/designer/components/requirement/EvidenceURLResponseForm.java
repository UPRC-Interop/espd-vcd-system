package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.EvidenceURLResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class EvidenceURLResponseForm extends FormLayout {
    private EvidenceURLResponse evidenceURLResponse = null;
    private TextField evidenceURL = new TextField("Evidence URL: ");

    public EvidenceURLResponseForm(EvidenceURLResponse evidenceURLResponse, String caption) {
        this.evidenceURLResponse = evidenceURLResponse;
        addComponent(evidenceURL);
        setCaption(caption);
        evidenceURL.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<EvidenceURLResponse> binder = new BeanFieldGroup<>(EvidenceURLResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.evidenceURLResponse);
        binder.setBuffered(false);
    }
}
