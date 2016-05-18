package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;

public class EvidenceURLCodeResponseForm extends ResponseForm {
    private EvidenceURLCodeResponse evidenceURLCodeResponse = null;
    private TextField evidenceURLCode = new TextField("Evidence URL Code: ");

    public EvidenceURLCodeResponseForm(EvidenceURLCodeResponse evidenceURLCodeResponse, String caption, boolean readOnly) {
        this.evidenceURLCodeResponse = evidenceURLCodeResponse;
        addComponent(evidenceURLCode);
        evidenceURLCode.setCaption(caption);
        evidenceURLCode.setNullRepresentation("");
        evidenceURLCode.setWidth(280, Unit.PIXELS);

        // Bind fields
        final BeanFieldGroup<EvidenceURLCodeResponse> binder = new BeanFieldGroup<>(EvidenceURLCodeResponse.class);
        binder.bindMemberFields(this);
        binder.setItemDataSource(this.evidenceURLCodeResponse);
        binder.setBuffered(false);
        binder.setReadOnly(readOnly);
    }
}
