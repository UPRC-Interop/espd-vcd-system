package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;

/**
 * Created by ixuz on 4/11/16.
 */
public class EvidenceURLCodeResponseForm extends FormLayout {
    private EvidenceURLCodeResponse evidenceURLCodeResponse = null;
    private TextField evidenceURLCode = new TextField("Evidence URL Code: ");

    public EvidenceURLCodeResponseForm(EvidenceURLCodeResponse evidenceURLCodeResponse, String caption) {
        this.evidenceURLCodeResponse = evidenceURLCodeResponse;
        addComponent(evidenceURLCode);
        setCaption(caption);
        evidenceURLCode.setNullRepresentation("");

        // Bind fields
        final BeanFieldGroup<EvidenceURLCodeResponse> binder = new BeanFieldGroup<>(EvidenceURLCodeResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.evidenceURLCodeResponse);
        binder.setBuffered(false);
    }
}
