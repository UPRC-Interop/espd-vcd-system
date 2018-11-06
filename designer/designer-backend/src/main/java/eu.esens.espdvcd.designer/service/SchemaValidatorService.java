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

import eu.esens.espdvcd.validator.ArtefactValidator;
import eu.esens.espdvcd.validator.ValidationResult;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public enum SchemaValidatorService implements ValidatorService {
    INSTANCE;

    public static ValidatorService getInstance() {
        return INSTANCE;
    }

    @Override
    public ArtefactValidator validateESPDFile(File request) throws JAXBException, SAXException {
//        return ValidatorFactory.createESPDSchemaValidator(request);
        Logger.getLogger(this.getClass().getName()).warning("Schema Validation has been disabled for debug purposes!");
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
