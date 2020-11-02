/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.requirement.response.Response;
import eu.esens.espdvcd.model.EvidenceIssuerDetails;
import eu.esens.espdvcd.model.requirement.response.evidence.VCDEvidenceResponse;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.ResponseType;

/**
 * Created by Ulf Lotzmann on 04/05/2017.
 */
public class VCDResponseModelExtractor extends ESPDResponseModelExtractor {

    /**
     * Extract the evidence uri response.
     *
     * @param res
     * @return
     */
    @java.lang.Override
    protected Response extractEvidenceURLResponse(ResponseType res) {
        VCDEvidenceResponse eResp = new VCDEvidenceResponse();

        if (!res.getEvidence().isEmpty()) {

            if (!res.getEvidence().get(0).getEvidenceDocumentReference().isEmpty()) {

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getAttachment().getExternalReference().getURI().getValue() != null) {

                    eResp.setEvidenceURL(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getAttachment()
                            .getExternalReference()
                            .getURI()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getID() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getID().getValue() != null) {
                    eResp.setID(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getID()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getIssueDate() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getIssueDate().getValue() != null) {
                    eResp.setDate(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getIssueDate()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getIssueTime() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getIssueTime().getValue() != null) {
                    eResp.setTime(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getIssueTime()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getDocumentTypeCode() != null
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getDocumentTypeCode().getValue() != null) {
                    eResp.setTypeCode(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getDocumentTypeCode()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getDocumentDescription() != null
                        && !res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getDocumentDescription().isEmpty()
                        && res.getEvidence().get(0).getEvidenceDocumentReference().get(0).getDocumentDescription().get(0).getValue() != null) {
                    eResp.setTypeCode(res.getEvidence()
                            .get(0)
                            .getEvidenceDocumentReference()
                            .get(0)
                            .getDocumentDescription()
                            .get(0)
                            .getValue());
                }

            }

            if (res.getEvidence().get(0).getEvidenceName() != null
                    && res.getEvidence().get(0).getEvidenceName().getValue() != null) {
                eResp.setName(res.getEvidence()
                        .get(0)
                        .getEvidenceName()
                        .getValue());
            }

            if (res.getEvidence().get(0).getEmbeddedEvidenceIndicator() != null) {
                eResp.setEmbeddedEvidenceIndicator(res.getEvidence()
                        .get(0)
                        .getEmbeddedEvidenceIndicator()
                        .isValue());
            }


            EvidenceIssuerDetails issuerParty = new EvidenceIssuerDetails();
            eResp.setEvidenceIssuer(issuerParty);

            if (res.getEvidence().get(0).getEvidenceIssuerParty() != null) {

                if (res.getEvidence().get(0).getEvidenceIssuerParty().getPartyIdentification() != null
                        && !res.getEvidence().get(0).getEvidenceIssuerParty().getPartyIdentification().isEmpty()
                        && res.getEvidence().get(0).getEvidenceIssuerParty().getPartyIdentification().get(0).getID() != null
                        && res.getEvidence().get(0).getEvidenceIssuerParty().getPartyIdentification().get(0).getID().getValue() != null) {
                    issuerParty.setID(res.getEvidence()
                            .get(0)
                            .getEvidenceIssuerParty()
                            .getPartyIdentification()
                            .get(0)
                            .getID()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceIssuerParty().getPartyName() != null
                        && !res.getEvidence().get(0).getEvidenceIssuerParty().getPartyName().isEmpty()
                        && res.getEvidence().get(0).getEvidenceIssuerParty().getPartyName().get(0).getName() != null
                        && res.getEvidence().get(0).getEvidenceIssuerParty().getPartyName().get(0).getName().getValue() != null) {

                    issuerParty.setName(res.getEvidence()
                            .get(0)
                            .getEvidenceIssuerParty()
                            .getPartyName()
                            .get(0)
                            .getName()
                            .getValue());
                }

                if (res.getEvidence().get(0).getEvidenceIssuerParty().getWebsiteURI() != null
                        && res.getEvidence().get(0).getEvidenceIssuerParty().getWebsiteURI().getValue() != null) {
                    issuerParty.setWebsite(res.getEvidence()
                            .get(0)
                            .getEvidenceIssuerParty()
                            .getWebsiteURI()
                            .getValue());
                }

            }

        }

        return eResp;
    }
}
