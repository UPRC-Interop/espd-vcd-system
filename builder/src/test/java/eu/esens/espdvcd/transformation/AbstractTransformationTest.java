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
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class AbstractTransformationTest {

    /**
     * Gets the path of ESPD Request HTML document.
     * @param espdRequest is the ESPD Request model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws URISyntaxException
     * @return java.nio.file.Path
     */
    Path writeHtml(ESPDRequest espdRequest,EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException, URISyntaxException {
        final HtmlHelperTest htmlHelperTest = new HtmlHelperTest();
        return htmlHelperTest.generateTempHtml(espdRequest,lang);
    }

    /**
     * Gets the path of ESPD Response HTML document.
     * @param espdResponse is the ESPD Response model.
     * @param lang is the selected language for HTML document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws URISyntaxException
     * @return java.nio.file.Path
     */
    Path writeHtml(ESPDResponse espdResponse,EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException, URISyntaxException {
        final HtmlHelperTest htmlHelperTest = new HtmlHelperTest();
        return htmlHelperTest.generateTempHtml(espdResponse,lang);
    }


    /**
     * Gets the path of ESPD Request PDF document.
     * @param espdRequest is the ESPD Request model.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws URISyntaxException
     * @return java.nio.file.Path
     */
    Path writePdf(ESPDRequest espdRequest, EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException, URISyntaxException {
        final PdfHelperTest pdfHelperTest = new PdfHelperTest();
        return pdfHelperTest.generateTempPDF(espdRequest, lang);
    }

    /**
     * Gets the path of ESPD Response PDF document.
     * @param espdResponse is the ESPD Response model.
     * @param lang is the selected language for PDF document.
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws URISyntaxException
     * @return java.nio.file.Path
     */
    Path writePdf(ESPDResponse espdResponse, EULanguageCodeEnum lang) throws IOException,
            TemplateException, ParserConfigurationException, SAXException, URISyntaxException {
        final PdfHelperTest pdfHelperTest = new PdfHelperTest();
        return pdfHelperTest.generateTempPDF(espdResponse, lang);
    }

    /**
     * Shows the transferred path with the default app if a head is available.
     * @param path
     * @throws IOException
     */
    void openPath(Path path) throws IOException {
        if (!GraphicsEnvironment.isHeadless()) {
            Desktop.getDesktop().open(path.toFile());
        }
    }
}