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
package eu.esens.espdvcd.transformation;

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import freemarker.template.TemplateException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PdfHelperTest {

    private final TransformationService transformationService = new TransformationService();

    /**
     * Generates an ESPD Request to PDF document.
     * @param target is the target path.
     * @param espdRequest is the ESPD Request Model.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TemplateException
     */
    private void generatePDF(Path target,ESPDRequest espdRequest, EULanguageCodeEnum lang) throws IOException, TemplateException,
            ParserConfigurationException, SAXException {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))) {
            transformationService.createPdf(espdRequest, os, lang);
        }
    }

    /**
     * Generates an ESPD Response to HTML document,
     * @param target is the target path.
     * @param espdResponse is the ESPD Response Model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TemplateException
     */
    private void generatePDF(Path target, ESPDResponse espdResponse, EULanguageCodeEnum lang) throws IOException, TemplateException,
            ParserConfigurationException, SAXException {
        try (OutputStream os = new BufferedOutputStream(Files.newOutputStream(target))) {
            transformationService.createPdf(espdResponse, os, lang);
        }
    }

    /**
     * Generates the template of PDF ESPD Request document.
     * @param requestContent is the ESPD Request model.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.nio.file.Path
     */
    public Path generateTempPDF(ESPDRequest requestContent, EULanguageCodeEnum lang) throws IOException, TemplateException,
            ParserConfigurationException, SAXException {
        final Path target = Files.createTempFile("testpdf", ".pdf");
        generatePDF(target, requestContent, lang);
        return target;
    }

    /**
     * Generates the template of PDF ESPD Response document.
     * @param responseContent is the ESPD Response model.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @return java.nio.file.Path
     */
    public Path generateTempPDF(ESPDResponse responseContent, EULanguageCodeEnum lang) throws IOException, TemplateException,
            ParserConfigurationException, SAXException {
        final Path target = Files.createTempFile("testpdf", ".pdf");
        generatePDF(target, responseContent, lang);
        return target;
    }
}