/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.typeEnum.ExportType;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

public interface ExportESPDService {
    InputStream exportESPDRequest(ESPDRequest model) throws ValidationException, JAXBException, IOException, SAXException;

    InputStream exportESPDRequestAs(ESPDRequest model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException, BuilderException;

    InputStream exportESPDResponse(ESPDResponse model) throws ValidationException, JAXBException, IOException, SAXException;

    InputStream exportESPDResponseAs(ESPDResponse model, EULanguageCodeEnum languageCodeEnum, ExportType exportType) throws ValidationException, JAXBException, IOException, SAXException, BuilderException;
}
