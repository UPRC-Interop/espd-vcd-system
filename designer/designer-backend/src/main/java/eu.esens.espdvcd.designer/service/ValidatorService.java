/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
import org.apache.poi.util.IOUtils;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface ValidatorService {
    ArtefactValidator validateESPDFile(File request) throws JAXBException, SAXException, ValidationException;

    default ArtefactValidator validateESPDStream(InputStream request) throws IOException, JAXBException, SAXException, ValidationException {
        byte[] bytes = IOUtils.toByteArray(request);
        request = new ByteArrayInputStream(bytes);

        Path tempFile = Files.createTempFile("espd", ".tmp");
        Files.write(tempFile, bytes);
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
