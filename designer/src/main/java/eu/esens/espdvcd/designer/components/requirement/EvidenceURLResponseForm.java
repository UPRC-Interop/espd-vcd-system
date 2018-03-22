package eu.esens.espdvcd.designer.components.requirement;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import eu.esens.espdvcd.model.EvidenceIssuerDetails;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.QuantityIntegerResponse;
import eu.esens.espdvcd.model.requirement.response.evidence.VCDEvidenceResponse;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class EvidenceURLResponseForm extends ResponseForm {
    private VCDEvidenceResponse evidenceURLResponse = null;
    private TextField evidenceURL = new TextField("Evidence URL: ");
    private Button uploadEvidenceButton = new Button("Upload evidence");
    private Button downloadEvidenceButton = new Button("Download evidence");
    private VerticalLayout layout = new VerticalLayout();
    private VerticalLayout selectMethodLayout = new VerticalLayout();
    private VerticalLayout uploadLayout = new VerticalLayout();
    private VerticalLayout urlLayout = new VerticalLayout();
    private VerticalLayout completedLayout = new VerticalLayout();
    private VerticalLayout downloadLayout = new VerticalLayout();

    private Button selectFileButton = new Button("Evidence File");
    private Button selectUrlButton = new Button("Evidence URL");
    private Label filenameLabel = new Label("");

    public EvidenceURLResponseForm(VCDEvidenceResponse evidenceURLResponse, String caption, int displayEvidences, boolean readOnly) {
        this.evidenceURLResponse = evidenceURLResponse;

//        uploadEvidenceButton.addClickListener(this::onEvidenceUpload);
//        addComponent(uploadEvidenceButton);

        layout.setStyleName("evidenceUrlResponseFormLayout");
        layout.setMargin(true);

        addComponent(layout);
        layout.addComponent(selectMethodLayout);
        layout.addComponent(uploadLayout);
        layout.addComponent(urlLayout);
        layout.addComponent(completedLayout);
        layout.addComponent(downloadLayout);

        // Select method layout
        Label selectMethodLabel = new Label("Select method");
        selectMethodLabel.setStyleName("evidenceTitleLabel");
        selectMethodLayout.addComponent(selectMethodLabel);
        HorizontalLayout selectButtonsLayout = new HorizontalLayout();
        selectMethodLayout.addComponent(selectButtonsLayout);
        selectButtonsLayout.addComponent(selectFileButton);
        selectButtonsLayout.addComponent(selectUrlButton);

        selectFileButton.addClickListener((clickEvent) -> { showUploadLayout(); });
        selectUrlButton.addClickListener((clickEvent) -> { showUrlLayout(); });

        selectFileButton.setStyleName("evidenceButton");
        selectUrlButton.setStyleName("evidenceButton");
        selectFileButton.setIcon(FontAwesome.UPLOAD);
        selectUrlButton.setIcon(FontAwesome.EDIT);

        // Upload layout
        Label evidenceUploadLabel = new Label("Evidence upload");
        evidenceUploadLabel.setStyleName("evidenceTitleLabel");
        uploadLayout.addComponent(evidenceUploadLabel);

        class FileUploader implements Upload.Receiver, Upload.SucceededListener {
            public File file;
            UUID generatedUUID = UUID.randomUUID();

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                FileOutputStream fos = null; // Stream to write to
                try {
                    File dir = new File("tmp/evidences/" + generatedUUID.toString());
                    dir.mkdirs();
                    file = new File("tmp/evidences/" + generatedUUID.toString() + "/" + filename);

                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("Could not open file<br/>",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos;
            }

            public void uploadSucceeded(Upload.SucceededEvent event) {

                try {
                    InputStream is = new FileInputStream(file);
                    is.close();

                    filenameLabel.setValue(file.getName());

                    Notification notification = new Notification("Your evidence has been added!");
                    notification.setPosition(Position.TOP_CENTER);
                    notification.setDelayMsec(1000);
                    notification.show(Page.getCurrent());

                    evidenceURL.setValue(file.toURI().toURL().toExternalForm());

                    // UL 2018-01-25 test code: add meta-data for uploaded evidence
                    /*
                    evidenceURLResponse.setName("NEW-DATA-FIELD_EVIDENCE-NAME");
                    evidenceURLResponse.setDate(LocalDate.now());
                    evidenceURLResponse.setTime(LocalTime.now());
                    evidenceURLResponse.setDescription("This is a dummy description for a dummy evidence document.");
                    evidenceURLResponse.setEmbeddedEvidenceIndicator(true);
                    evidenceURLResponse.setTypeCode(UUID.randomUUID().toString());
                    EvidenceIssuerDetails eid = new EvidenceIssuerDetails();
                    eid.setID(UUID.randomUUID().toString());
                    eid.setName("Dummy Evidence Issuer");
                    eid.setWebsite("https://www.uni-koblenz.de");
                    evidenceURLResponse.setEvidenceIssuer(eid);
                    */

                    showCompletedLayout();
                } catch (IOException e) {
                    Notification.show("Failed to upload evidence",
                            "Upload failed",
                            Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (Exception e) {
                    Notification.show("Failed to upload evidence",
                            "Upload failed",
                            Notification.Type.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }

        FileUploader receiver = new FileUploader();
        Upload upload = new Upload(null, receiver);
        upload.setButtonCaption("Import");
        upload.addSucceededListener(receiver);
        uploadLayout.addComponent(upload);

        // Url layout
        urlLayout.addComponent(evidenceURL);
        evidenceURL.setCaption(caption);
        evidenceURL.setWidth(280, Unit.PIXELS);

        // Completed layout
        Label evidenceLabel = new Label("Evidence uploaded");
        evidenceLabel.setStyleName("evidenceTitleLabel");
        completedLayout.addComponent(evidenceLabel);
        completedLayout.addComponent(filenameLabel);

        if (displayEvidences == 1) {
            showSelectMethodLayout();
        } else if (displayEvidences == 2) {
            showDownloadLayout();
        }

        // Download layout
        downloadLayout.addComponent(downloadEvidenceButton);

        StreamResource.StreamSource source = new StreamResource.StreamSource()
        {
            public java.io.InputStream getStream()
            {
                try
                {
                    String filePath = null;
                    try {
                        filePath = new URI(evidenceURLResponse.getEvidenceURL()).getPath();
                        File file = new File(filePath);
                        FileInputStream fileInputStream = new FileInputStream(file);
                        return fileInputStream;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
        };

        if (evidenceURLResponse != null && evidenceURLResponse.getEvidenceURL() != null) {
            try {
                String filename = new File(new URI(evidenceURLResponse.getEvidenceURL()).getPath()).getName();
                StreamResource downloadableResponse = new StreamResource(source, filename);

                FileDownloader fileDownloader = new FileDownloader(downloadableResponse);
                fileDownloader.extend(downloadEvidenceButton);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            downloadEvidenceButton.setEnabled(false);
            downloadEvidenceButton.setCaption("No evidence");
        }

        // Bind fields
        final Binder<VCDEvidenceResponse> binder = new BeanValidationBinder<>(VCDEvidenceResponse.class);
        binder.bindInstanceFields(this);
        binder.setBean(this.evidenceURLResponse);
        if (displayEvidences == 1) {
            binder.setReadOnly(false);
        } else {
            binder.setReadOnly(readOnly);
        }
    }

    public void onEvidenceUpload(Button.ClickEvent clickEvent) {


    }

    public void showSelectMethodLayout() {
        selectMethodLayout.setVisible(true);
        selectMethodLayout.setEnabled(true);
        uploadLayout.setVisible(false);
        uploadLayout.setEnabled(false);
        urlLayout.setVisible(false);
        urlLayout.setEnabled(false);
        completedLayout.setVisible(false);
        completedLayout.setEnabled(false);
        downloadLayout.setVisible(false);
        downloadLayout.setEnabled(false);
    }

    public void showUploadLayout() {
        selectMethodLayout.setVisible(false);
        selectMethodLayout.setEnabled(false);
        uploadLayout.setVisible(true);
        uploadLayout.setEnabled(true);
        urlLayout.setVisible(false);
        urlLayout.setEnabled(false);
        completedLayout.setVisible(false);
        completedLayout.setEnabled(false);
        downloadLayout.setVisible(false);
        downloadLayout.setEnabled(false);
    }

    public void showUrlLayout() {
        selectMethodLayout.setVisible(false);
        selectMethodLayout.setEnabled(false);
        uploadLayout.setVisible(false);
        uploadLayout.setEnabled(false);
        urlLayout.setVisible(true);
        urlLayout.setEnabled(true);
        completedLayout.setVisible(false);
        completedLayout.setEnabled(false);
        downloadLayout.setVisible(false);
        downloadLayout.setEnabled(false);
    }

    public void showCompletedLayout() {
        selectMethodLayout.setVisible(false);
        selectMethodLayout.setEnabled(false);
        uploadLayout.setVisible(false);
        uploadLayout.setEnabled(false);
        urlLayout.setVisible(false);
        urlLayout.setEnabled(false);
        completedLayout.setVisible(true);
        completedLayout.setEnabled(true);
        downloadLayout.setVisible(false);
        downloadLayout.setEnabled(false);
    }

    public void showDownloadLayout() {
        selectMethodLayout.setVisible(false);
        selectMethodLayout.setEnabled(false);
        uploadLayout.setVisible(false);
        uploadLayout.setEnabled(false);
        urlLayout.setVisible(false);
        urlLayout.setEnabled(false);
        completedLayout.setVisible(false);
        completedLayout.setEnabled(false);
        downloadLayout.setVisible(true);
        downloadLayout.setEnabled(true);
    }
}
