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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.builder.PDFDocumentBuilderV2;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ExportType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.transformation.TransformationService;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static eu.esens.espdvcd.designer.util.CriteriaUtil.*;

public enum ExportESPDV2Service implements ExportESPDService {
    INSTANCE;

    private final TransformationService transformationService = new TransformationService();
    private final ValidatorService schemaValidationService = SchemaValidatorService.getInstance();
    private final ValidatorService schematronValidationService = SchematronValidatorService.getInstance();

    public static ExportESPDV2Service getInstance() {
        return INSTANCE;
    }

    @Override
    public InputStream exportESPDRequest(ESPDRequest model) throws ValidationException, JAXBException, IOException, SAXException {
        String theXML = BuilderFactory.EDM_V2.createXMLDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
        schematronValidationService.validateESPDString(theXML);
        schemaValidationService.validateESPDString(theXML);
        return new ByteArrayInputStream(theXML.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public InputStream exportESPDRequestAs(ESPDRequest model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException, BuilderException {
        Objects.requireNonNull(exportType);
        switch (exportType) {
            case XML:
                return exportESPDRequest(model);
            case PDF:
                ESPDRequest importExportModel = BuilderFactory.EDM_V2
                        .createRegulatedModelBuilder()
                        .importFrom(
                                BuilderFactory.EDM_V2
                                        .createXMLDocumentBuilderFor(finalizeBeforeExport(model))
                                        .getAsInputStream())
                        .createESPDRequest();
                PDFDocumentBuilderV2 pdfDocumentBuilderV2 = BuilderFactory.EDM_V2
                        .createPDFDocumentBuilderFor(finalizeBeforeExport(importExportModel));
                return transformationService.createPdfStream(new StreamSource(new ByteArrayInputStream(pdfDocumentBuilderV2.getAsString().getBytes(StandardCharsets.UTF_8))), languageCodeEnum);
            case HTML:
                return transformationService.createHtmlStream(new StreamSource(exportESPDRequest(model)), languageCodeEnum);
            default:
                throw new UnsupportedOperationException(String.format("Exporting to %s is not supported.", exportType.name()));
        }
    }

    @Override
    public InputStream exportESPDResponse(ESPDResponse model) throws ValidationException, JAXBException, IOException, SAXException {
        String theXML = BuilderFactory.EDM_V2.createXMLDocumentBuilderFor(finalizeBeforeExport(model)).getAsString();
        schematronValidationService.validateESPDString(theXML);
        schemaValidationService.validateESPDString(theXML);
        return new ByteArrayInputStream(theXML.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public InputStream exportESPDResponseAs(ESPDResponse model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException, BuilderException {
        Objects.requireNonNull(exportType);
        switch (exportType) {
            case XML:
                return exportESPDResponse(model);
            case PDF:
                ESPDResponse importExportModel = BuilderFactory.EDM_V2
                        .createRegulatedModelBuilder()
                        .importFrom(
                                BuilderFactory.EDM_V2
                                        .createXMLDocumentBuilderFor(finalizeBeforeExport(model))
                                        .getAsInputStream())
                        .createESPDResponse();

                PDFDocumentBuilderV2 pdfDocumentBuilderV2 = BuilderFactory.EDM_V2
                        .createPDFDocumentBuilderFor(finalizeBeforeExport(importExportModel));
                return transformationService.createPdfStream(new StreamSource(new ByteArrayInputStream(pdfDocumentBuilderV2.getAsString().getBytes(StandardCharsets.UTF_8))), languageCodeEnum);
            case HTML:
                return transformationService.createHtmlStream(new StreamSource(exportESPDResponse(model)), languageCodeEnum);
            default:
                throw new UnsupportedOperationException(String.format("Exporting to %s is not supported.", exportType.name()));
        }
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
