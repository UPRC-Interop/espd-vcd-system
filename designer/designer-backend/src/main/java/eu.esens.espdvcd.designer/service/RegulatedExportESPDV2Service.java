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
import eu.esens.espdvcd.transformation.TransformationService;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.finalizeESPDResponse;
import static eu.esens.espdvcd.designer.util.CriteriaUtil.hasNullCriterion;

public enum RegulatedExportESPDV2Service implements ExportESPDService {
    INSTANCE;

    public static RegulatedExportESPDV2Service getInstance() {
        return INSTANCE;
    }

    private final TransformationService transformationService = new TransformationService();

    @Override
    public InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException {
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
    }

    @Override
    public InputStream exportESPDRequestHtmlAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        return transformationService.createHtmlStream(new StreamSource(exportESPDRequestAsInputStream(model)), languageCodeEnum);
    }

    @Override
    public InputStream exportESPDRequestPdfAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        return transformationService.createPdfStream(new StreamSource(exportESPDRequestAsInputStream(model)), languageCodeEnum);
    }

    @Override
    public String exportESPDRequestAsString(ESPDRequest model) throws ValidationException {
        return BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
    }

    @Override
    public InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException {
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
    }

    @Override
    public InputStream exportESPDResponseHtmlAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        return transformationService.createHtmlStream(new StreamSource(exportESPDResponseAsInputStream(model)), languageCodeEnum);
    }

    @Override
    public InputStream exportESPDResponsePdfAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException {
        return transformationService.createPdfStream(new StreamSource(exportESPDResponseAsInputStream(model)), languageCodeEnum);
    }

    @Override
    public String exportESPDResponseAsString(ESPDResponse model) throws ValidationException {
        return BuilderFactory.EDM_V2.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
    }

    private ESPDRequest finalizeBeforeExport(final ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return model;
    }

    private ESPDResponse finalizeBeforeExport(final ESPDResponse model) throws ValidationException {
        finalizeBeforeExport((ESPDRequest) model);
        finalizeESPDResponse(model);
        return model;
    }
}
