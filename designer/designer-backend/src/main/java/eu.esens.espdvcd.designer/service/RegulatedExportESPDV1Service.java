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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import java.io.InputStream;

public class RegulatedExportESPDV1Service implements ExportESPDService {

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeV1ESPD(model)).getAsInputStream();
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeV1ESPD(model)).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeV1ESPD(model)).getAsInputStream();
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeV1ESPD(model)).getAsString();
    }

    private ESPDRequest finalizeV1ESPD(final ESPDRequest model) {
        model.getFullCriterionList()
                .stream()
                .filter(cr -> cr
                        .getTypeCode()
                        .equals("CRITERION.SELECTION.ALL_SATISFIED"))
                .findFirst()
                .ifPresent(cr -> {
                    if (cr.isSelected())
                        model.getFullCriterionList()
                                .removeIf(selectableCriterion -> selectableCriterion
                                        .getTypeCode()
                                        .matches("CRITERION.SELECTION.*"));

                });
        return model;
    }
}
