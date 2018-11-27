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
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import javax.el.MethodNotFoundException;
import java.io.InputStream;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.finalizeESPDResponse;
import static eu.esens.espdvcd.designer.util.CriteriaUtil.hasNullCriterion;

public enum RegulatedExportESPDV1Service implements ExportESPDService {
    INSTANCE;

    public static RegulatedExportESPDV1Service getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
    }

    @Override
    public InputStream exportESPDRequestHtmlAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        throw new MethodNotFoundException("not implemented");
    }

    @Override
    public InputStream exportESPDRequestPdfAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        throw new MethodNotFoundException("not implemented");
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
    }

    @Override
    public InputStream exportESPDResponseHtmlAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        throw new MethodNotFoundException("not implemented");
    }

    @Override
    public InputStream exportESPDResponsePdfAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        throw new MethodNotFoundException("not implemented");
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
    }

    private ESPDRequest finalizeBeforeExport(final ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
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
                                        .matches("(?!.*ALL_SATISFIED*)^CRITERION.SELECTION.+"));

                });
        return model;
    }

    private ESPDResponse finalizeBeforeExport(final ESPDResponse model) throws ValidationException {
        finalizeBeforeExport((ESPDRequest) model);
        finalizeESPDResponse(model);
        return model;
    }
}
