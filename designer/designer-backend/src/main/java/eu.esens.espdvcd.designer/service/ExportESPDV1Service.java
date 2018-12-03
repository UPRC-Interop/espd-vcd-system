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
import eu.esens.espdvcd.designer.typeEnum.ExportType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.*;

public enum ExportESPDV1Service implements ExportESPDService {
    INSTANCE;

    private final ValidatorService schemaValidationService = SchemaValidatorService.getInstance();
    private final ValidatorService schematronValidationService = SchematronValidatorService.getInstance();

    public static ExportESPDV1Service getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream exportESPDRequest(ESPDRequest model) throws ValidationException, JAXBException, IOException, SAXException {
        InputStream xmlStream = BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
        schematronValidationService.validateESPDStream(xmlStream);
        schemaValidationService.validateESPDStream(xmlStream);
        return xmlStream;
    }

    @Override
    public InputStream exportESPDRequestAs(ESPDRequest model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException {
        Objects.requireNonNull(exportType);
        if (exportType == ExportType.XML) {
            return exportESPDRequest(model);
        }
        throw new UnsupportedOperationException(String.format("Exporting to %s is not supported.", exportType.name()));
    }

    @Override
    public InputStream exportESPDResponse(ESPDResponse model) throws ValidationException, JAXBException, IOException, SAXException {
        InputStream xmlStream = BuilderFactory.EDM_V1.createDocumentBuilderFor(finalizeBeforeExport(model)).getAsInputStream();
        schematronValidationService.validateESPDStream(xmlStream);
        schemaValidationService.validateESPDStream(xmlStream);
        return xmlStream;
    }

    @Override
    public InputStream exportESPDResponseAs(ESPDResponse model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException {
        Objects.requireNonNull(exportType);
        if (exportType == ExportType.XML) {
            return exportESPDResponse(model);
        }
        throw new UnsupportedOperationException(String.format("Exporting to %s is not supported.", exportType.name()));
    }

    private ESPDRequest finalizeBeforeExport(final ESPDRequest model) throws ValidationException {
        if (hasNullCriterion(model.getFullCriterionList()))
            throw new ValidationException("Null criteria are not permitted.");
        return removeSelectionCriteriaIfAlpha(model);
    }

    private ESPDResponse finalizeBeforeExport(final ESPDResponse model) throws ValidationException {
        finalizeBeforeExport((ESPDRequest) model);
        finalizeESPDResponse(model);
        return model;
    }
}
