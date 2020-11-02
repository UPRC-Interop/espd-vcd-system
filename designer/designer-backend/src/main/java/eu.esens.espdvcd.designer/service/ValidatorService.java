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

import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface ValidatorService {
    ArtefactValidator validateESPDFile(File request) throws JAXBException, SAXException, ValidationException;

    default ArtefactValidator validateESPDString(String theXML) throws IOException, JAXBException, SAXException, ValidationException {
        Path tempFile = Files.createTempFile("espd", ".tmp");
        Files.write(tempFile, theXML.getBytes(StandardCharsets.UTF_8));
        return validateESPDFile(tempFile.toFile());
    }

    default ArtefactValidator disabledValidationResponse() {
        return new ArtefactValidator() {
            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public List<ValidationResult> getValidationMessages() {
                return null;
            }

            @Override
            public List<ValidationResult> getValidationMessagesFiltered(String keyWord) {
                return null;
            }
        };
    }
}
