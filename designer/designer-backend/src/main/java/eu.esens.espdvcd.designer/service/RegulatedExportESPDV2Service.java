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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.model.requirement.response.IndicatorResponse;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.hasNullCriterion;

public enum RegulatedExportESPDV2Service implements ExportESPDService {
    INSTANCE;

    public static RegulatedExportESPDV2Service getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(model).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        finalizeV2Response(model);
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsInputStream();
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        finalizeV2Response(model);
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(model).getAsString();
    }

    private void finalizeV2Response(final ESPDResponse document) {
        document.getEvidenceList().removeIf(e -> e.getEvidenceURL() == null);
        document.getFullCriterionList().forEach(cr -> finalizeResponses(cr.getRequirementGroups()));
    }

    private void finalizeResponses(final List<RequirementGroup> requirementGroupList) {
        for (RequirementGroup rg : requirementGroupList) {
            if (rg.getRequirements().get(0).getResponseDataType().equals(ResponseTypeEnum.INDICATOR) && rg.getRequirementGroups().size() > 0) {
                IndicatorResponse indicator = (IndicatorResponse) rg.getRequirements().get(0).getResponse();
                if (indicator != null) {
                    rg.getRequirementGroups().forEach(requirementGroup -> {
                        switch (requirementGroup.getCondition()) {
                            case "ONTRUE":
                                if (!indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            case "ONFALSE":
                                if (indicator.isIndicator()) {
                                    requirementGroup.getRequirements().forEach(rq -> rq.setResponse(null));
                                }
                                break;
                            default:
                                Logger.getLogger(ExportESPDService.class.getName()).warning("Ignoring condition " + requirementGroup.getCondition());
                                break;
                        }
                    });
                }
            }
            finalizeResponses(rg.getRequirementGroups());
        }
    }
}
