/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import eu.esens.espdvcd.model.DocumentDetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;
import eu.esens.espdvcd.validator.ArtefactValidator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public enum ImportESPDRequestService implements ImportESPDService<ESPDRequest> {
    INSTANCE;

    private final ValidatorService schemaValidationService = SchemaValidatorService.getInstance();
    private final ValidatorService schematronValidationService = SchematronValidatorService.getInstance();

    public static ImportESPDService getInstance() {
        return INSTANCE;
    }

    @Override
    public ESPDRequest importESPDFile(File XML) throws RetrieverException, BuilderException, JAXBException, SAXException, ValidationException, IOException {
        EDMVersion artefactVersion = ArtefactUtils.findEDMVersion(new FileInputStream(XML));
        QualificationApplicationTypeEnum qualificationApplicationType = ArtefactUtils.findQualificationApplicationType(XML);

        if (Objects.isNull(artefactVersion))
            throw new ValidationException("Cannot determine artefact version.");

        ArtefactValidator schemaResult = schemaValidationService.validateESPDFile(XML);
        ArtefactValidator schematronResult = schematronValidationService.validateESPDFile(XML);

        if (!schemaResult.isValid())
            throw new ValidationException("XSD validation failed on the supplied xml document.", schemaResult.getValidationMessages());
        if (!schematronResult.isValid())
            throw new ValidationException("Schematron validation failed on the supplied xml document.", schematronResult.getValidationMessages());

        InputStream is = new FileInputStream(XML);
        ESPDRequest request = null;
        CriteriaService criteriaService = null;
        switch (artefactVersion) {
            case V1:
                request = BuilderFactory.EDM_V1.createRegulatedModelBuilder().importFrom(is).createESPDRequest();
                criteriaService = RegulatedCriteriaService.getV1Instance();
                break;
            case V2:
                switch (qualificationApplicationType) {
                    case REGULATED:
                        request = BuilderFactory.EDM_V2.createRegulatedModelBuilder().importFrom(is).createESPDRequest();
                        criteriaService = RegulatedCriteriaService.getV2Instance();
                        break;
                    case SELFCONTAINED:
                        criteriaService = SelfContainedCriteriaService.getInstance();
                        request = BuilderFactory.EDM_V2.createSelfContainedModelBuilder().importFrom(is).createESPDRequest();
                        break;
                }
                break;
        }
        Objects.requireNonNull(request);
        request.setCriterionList(criteriaService.getUnselectedCriteria(request.getFullCriterionList()));
        CriteriaUtil.generateUUIDs(request.getFullCriterionList());
        is.close();
        request.setDocumentDetails(new DocumentDetails(artefactVersion,
                ArtefactUtils.findArtefactType(XML),
                qualificationApplicationType));
        return request;
    }
}
