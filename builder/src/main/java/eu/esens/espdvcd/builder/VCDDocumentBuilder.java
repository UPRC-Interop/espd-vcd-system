/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.builder.schema.SchemaFactory;
import eu.esens.espdvcd.model.ESPDRequest;

import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.EvidenceURLResponse;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.espd.schema.v1.espdresponse_1.ESPDResponseType;
import no.difi.asic.AsicWriterFactory;
import no.difi.asic.AsicWriter;
import no.difi.asic.MimeType;
import no.difi.asic.SignatureHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URI;

/**
 * Created by Ulf Lotzmann on 08/06/2016.
 */
public class VCDDocumentBuilder extends DocumentBuilderV1 {

    private SignatureHelper signatureHelper;

    /**
     * List of documents attached to this ESPD to be part of the VCD.
     */
    private List<URI> documents = new ArrayList<>();

    public VCDDocumentBuilder(ESPDRequest req, SignatureHelper signatureHelper) {
        super(req);
        this.signatureHelper = signatureHelper;

        // extract references to evidence documents from ESPDResponse
        if (req instanceof ESPDResponse) {
            scanForDocuments((ESPDResponse) req);
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

        // Creates the actual container with all the data objects (files) and signs it
        try {
            // Add the XML business document
            AsicWriter asicWriter = asicWriterFactory.newContainer(archiveOutputFile)
                    .add(espdStream, "espd.xml", MimeType.forString("application/xml"));

            // Add all found evidence documents with modified file entry name
            for (URI document : documents) {
                asicWriter.add(new File(document), EvidenceHelper.getASiCResourcePath(document.toString()));
            }
            asicWriter.sign(signatureHelper);
        } catch (IOException ex) {
            Logger.getLogger(VCDDocumentBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Creates an input stream for the container
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
     *
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
     *
     * @param rg
     */
    private void scanRequirementGroup(RequirementGroup rg) {
        for (RequirementGroup innerRg : rg.getRequirementGroups()) {
            scanRequirementGroup(innerRg);
        }
        for (Requirement rq : rg.getRequirements()) {
            Response resp = rq.getResponse();
            //if (rq.getResponseDataType().equals(ResponseTypeEnum.EVIDENCE_URL)) {
            if (resp instanceof EvidenceURLResponse) {
                String uriStr = ((EvidenceURLResponse) resp).getEvidenceURL();
                if (uriStr != null) {
                    try {
                        URI uri = URI.create(uriStr);
                        File document = new File(uri);
                        if (document.exists()) {
                            documents.add(uri);
                        }
                    } catch (Exception ex) {
                        // do nothing
                    }

                }
            }
        }
    }

    @java.lang.Override
    protected String getProfileID() {
        return "56";
    }


    @Override
    protected ESPDResponseType createXML(ESPDResponse res) {
        ESPDResponseType resType = finalize(SchemaFactory.withEDM_V1().VCD_RESPONSE
                .extractESPDResponseType(res));
        return resType;
    }
}
