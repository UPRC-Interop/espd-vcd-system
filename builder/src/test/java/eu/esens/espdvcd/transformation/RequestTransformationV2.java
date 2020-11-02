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

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.ESPDRequest;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.xml.transform.stream.StreamSource;
import java.nio.file.Path;

public class RequestTransformationV2 extends AbstractTransformationTest{

    private static ESPDRequest espdRequest;
    private static final String XML_REF = "artefacts/regulated/v2/2.1.0/UPRC-ESPD-Regulated-Request-2.1.0-Artefact-22-4-2019.xml";

    @BeforeClass
    public static void transform() throws BuilderException {
        StreamSource xmlSource = new StreamSource(RequestTransformationV2.class.getClassLoader().getResourceAsStream(XML_REF));
        TransformationHelper th = new TransformationHelper();
        espdRequest = th.getESPDRequestV2(xmlSource);
    }

    @Test
    public void showHtml() throws Exception {
        final Path path = writeHtml(espdRequest,EULanguageCodeEnum.EL);
        openPath(path);
    }

    @Test
    public void showPdf() throws Exception {
        final Path path = writePdf(espdRequest, EULanguageCodeEnum.EL);
        openPath(path);
    }

}