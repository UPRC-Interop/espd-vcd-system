package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class ESPDEvidenceURLResponseForm extends ResponseForm {
    private EvidenceURLResponse evidenceURLResponse = null;
    private TextField evidenceURL = new TextField("Evidence URL: ");

    public ESPDEvidenceURLResponseForm(EvidenceURLResponse evidenceURLResponse, String caption, boolean readOnly) {
        this.evidenceURLResponse = evidenceURLResponse;
        addComponent(evidenceURL);
        evidenceURL.setCaption(caption);
        //evidenceURL.setNullRepresentation("");
        evidenceURL.setWidth(280, Unit.PIXELS);

        // Bind fields
        final Binder<EvidenceURLResponse> binder = new BeanValidationBinder<>(EvidenceURLResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.evidenceURLResponse);


        //final BeanFieldGroup<EvidenceURLResponse> binder = new BeanFieldGroup<>(EvidenceURLResponse.class);
        //binder.bindMemberFields(this);
        //binder.setItemDataSource(this.evidenceURLResponse);
        //binder.setBuffered(false);
        //binder.setReadOnly(readOnly);
    }
}
