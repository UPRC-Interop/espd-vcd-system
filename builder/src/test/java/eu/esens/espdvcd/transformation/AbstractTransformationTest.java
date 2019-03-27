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
import org.w3c.dom.Document;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AbstractTransformationTest {

    /**
     * Gibt die DomSource als String in UTF-8 zurueck ohne INDENT
     */
    private String convertToString(Source source) throws TransformerException {
        return new TransformationHelper().convertToString(source);
    }

    /**
     * Nimmt das HTML entgegen und ersetzt die Labels mit Velocity
     */
    private Path writeHtml(String htmlContent) throws IOException {
        final Path tempFile = Files.createTempFile("testfile", ".html");
        Files.write(tempFile, htmlContent.getBytes(StandardCharsets.UTF_8));
        return tempFile;
    }

    /**
     * Nimmt das HTML entgegen und ersetzt die Labels mit Velocity
     */
    Path writeHtml(DOMSource domSource) throws TransformerException, IOException {
        return writeHtml(convertToString(domSource));
    }
    /**
     * Erzeugt eine Tempfile mit der PDF und gibt dessen Pfad zur?ck
     */
    Path writePdf(Source source, EULanguageCodeEnum lang) throws IOException {
        final PdfHelperTest pdfHelperTest = new PdfHelperTest();
        return pdfHelperTest.generateTempPDF(source, lang);
    }

    /**
     * Zeigt den ?bergebenen Pfad mit der Default-App an, wenn ein Head vorhanden ist.
     */
    void openPath(Path path) throws IOException {
        if (!GraphicsEnvironment.isHeadless()) {
            Desktop.getDesktop().open(path.toFile());
        }
    }
}
