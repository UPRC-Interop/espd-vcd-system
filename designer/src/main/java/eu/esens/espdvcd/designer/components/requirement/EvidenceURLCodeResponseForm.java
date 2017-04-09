package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.TextField;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLCodeResponse;

public class EvidenceURLCodeResponseForm extends ResponseForm {
    private EvidenceURLCodeResponse evidenceURLCodeResponse = null;
    private TextField evidenceURLCode = new TextField("Evidence URL Code: ");

    public EvidenceURLCodeResponseForm(EvidenceURLCodeResponse evidenceURLCodeResponse, String caption, boolean readOnly) {
        this.evidenceURLCodeResponse = evidenceURLCodeResponse;
        addComponent(evidenceURLCode);
        evidenceURLCode.setCaption(caption);
        evidenceURLCode.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<EvidenceURLCodeResponse> binder = new BeanValidationBinder<>(EvidenceURLCodeResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.evidenceURLCodeResponse);
        binder.setReadOnly(readOnly);
    }
}
