/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder.model;

import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import java.util.stream.Collectors;


public class ESPDRequestModelExtractor implements ModelExtractor {

    /* package private constructor. Create only through factory */
    ESPDRequestModelExtractor() {
    }

    public ESPDRequest extractESPDRequest(ESPDRequestType reqType) {

        RegulatedESPDRequest req = new RegulatedESPDRequest();

        req.getFullCriterionList().addAll(reqType.getCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));
        req.setCADetails(extractCADetails(reqType.getContractingParty(),
                reqType.getContractFolderID(),
                reqType.getAdditionalDocumentReference()));

        req.setServiceProviderDetails(extractServiceProviderDetails(reqType.getServiceProviderParty()));

        return req;
    }

    public ESPDRequest extractESPDRequest(QualificationApplicationRequestType reqType) {

        RegulatedESPDRequest req = new RegulatedESPDRequest();

        req.getFullCriterionList().addAll(reqType.getTenderingCriterion().stream()
                .map(c -> extractSelectableCriterion(c))
                .collect(Collectors.toList()));
        req.setCADetails(extractCADetails(reqType.getContractingParty(),
                reqType.getContractFolderID(),
                reqType.getAdditionalDocumentReference()));

        // Apply global weighting
        req.getCADetails().getWeightScoringMethodologyNoteList()
                .addAll(reqType.getWeightScoringMethodologyNote().stream()
                        .map(noteType -> noteType.getValue())
                        .collect(Collectors.toList()));

        if (reqType.getWeightingTypeCode() != null
                && reqType.getWeightingTypeCode().getValue() != null) {
            req.getCADetails().setWeightingType(reqType.getWeightingTypeCode().getValue());
        }

        req.setServiceProviderDetails(extractServiceProviderDetails(reqType.getContractingParty()));

        return req;
    }

}
