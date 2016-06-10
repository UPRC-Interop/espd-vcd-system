package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.ESPDRequest;

import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.Response;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.AsicWriter;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;
import no.difi.xsd.asic.model._1.AsicManifest;
import org.etsi.uri._02918.v1_2.DataObjectReferenceType;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URL;

/**
 * Created by Ulf Lotzmann on 08/06/2016.
 */
public class VCDDocumentBuilder extends DocumentBuilder {

    private SignatureHelper signatureHelper;

    /**
     * List of documents attached to this ESPD to be part of the VCD.
     */
    private List<File> documents = new ArrayList<>();

    public VCDDocumentBuilder(ESPDRequest req, SignatureHelper signatureHelper) {
        super(req);
        this.signatureHelper = signatureHelper;

        // extract references to evidence documents from ESPDResponse
        if (req instanceof ESPDResponse) {
            scanForDocuments((ESPDResponse)req);
        }
    }

    /**
     * Bundles the XML Representation of the ESPD with attached documents to an ASiC, returned as an input stream.
     *
     * @return the VCD ASiC as an input stream
     */
    @Override
    public InputStream getAsInputStream() {
        return createVCD(super.getAsInputStream());
    }

    private InputStream createVCD(InputStream espdStream) {
        // Creates an ASiC archive

        // Name of the file to hold the the ASiC archive
        File archiveOutputFile = new File(System.getProperty("java.io.tmpdir"), "vcd.asice");

        // Creates an AsicWriterFactory with default signature method
        AsicWriterFactory asicWriterFactory = AsicWriterFactory.newFactory();

        // Creates the actual container with all the data objects (files) and signs it.
        try {
            AsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutputFile)
                 .add(espdStream, "espd.xml", MimeType.forString("application/xml"));
            for (File document : documents) {
                asicWriter.add(document);
            }
            asicWriter.sign(signatureHelper);
        }
        catch (IOException ex) {
            Logger.getLogger(VCDDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Creates an input stream for the container.
        InputStream asic = null;
        try {
            asic = new FileInputStream(archiveOutputFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VCDDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        return asic;
    }

    /**
     * Scan the espd for references to locally stored ("java.io.tmpdir" folder) documents
     * @param espd
     */
    private void scanForDocuments(ESPDResponse espd) {
        for (SelectableCriterion cr : espd.getFullCriterionList()) {
            for (RequirementGroup rg : cr.getRequirementGroups()) {
                scanRequirementGroup(rg);
            }
        }
    }

    /**
     * Recursively scan requirement groups for references to locally stored documents
     * @param rg
     */
    private void scanRequirementGroup(RequirementGroup rg) {
        for (RequirementGroup innerRg : rg.getRequirementGroups() ) {
            scanRequirementGroup(innerRg);
        }
        for (Requirement rq : rg.getRequirements()) {
            Response resp = rq.getResponse();
            //if (rq.getResponseDataType().equals(ResponseTypeEnum.EVIDENCE_URL)) {
            if (resp instanceof EvidenceURLResponse) {
                String uriStr = ((EvidenceURLResponse)resp).getEvidenceURL();
                if (uriStr != null) {
                    URI uri = URI.create(uriStr);
                    File document = new File(System.getProperty("java.io.tmpdir"), uri.getPath());
                    if (document.exists()) {
                        documents.add(document);
                    }
                }
            }
        }
    }


}
