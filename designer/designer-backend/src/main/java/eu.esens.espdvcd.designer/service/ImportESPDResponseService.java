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
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.util.CriteriaUtil;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public enum ImportESPDResponseService implements ImportESPDService<ESPDResponse> {
    INSTANCE;

    private final ValidatorService schemaValidationService = SchemaValidatorService.getInstance();
    private final ValidatorService schematronValidationService = SchematronValidatorService.getInstance();

    public static ImportESPDService getInstance() {
        return INSTANCE;
    }

    @Override
    public ESPDResponse importESPDFile(File XML) throws BuilderException, JAXBException, SAXException, ValidationException, IOException {
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(XML);
        QualificationApplicationTypeEnum qualificationApplicationType = ArtefactUtils.findQualificationApplicationType(XML);

        if (Objects.isNull(artefactVersion))
            throw new ValidationException("Cannot determine artefact version.");

        schemaValidationService.validateESPDFile(XML);
        schematronValidationService.validateESPDFile(XML);

        InputStream is = new FileInputStream(XML);
        ESPDResponse response = null;
        switch (artefactVersion) {
            case V1:
                response = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                break;
            case V2:
                switch (qualificationApplicationType) {
                    case REGULATED:
                        response = BuilderFactory.EDM_V2.createRegulatedModelBuilder().importFrom(is).createESPDResponse();
                        break;
                    case SELFCONTAINED:
                        response = BuilderFactory.EDM_V2.createSelfContainedModelBuilder().importFrom(is).createESPDResponse();
                        break;
                }
                break;
        }
        CriteriaUtil.generateUUIDs(response.getFullCriterionList());
        is.close();
        return response;
    }
}
