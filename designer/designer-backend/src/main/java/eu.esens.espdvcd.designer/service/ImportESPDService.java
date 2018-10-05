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

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.util.DocumentDetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImportESPDService<T extends ESPDRequest> {
    T importESPDFile(File XML) throws RetrieverException, BuilderException, JAXBException, SAXException, ValidationException, IOException;
    DocumentDetails getDocumentDetails(File XML);

    default void generateUUIDs(T espdDocument){
        int counter = 0;
        for (SelectableCriterion cr : espdDocument.getFullCriterionList()) {
            cr.setUUID(cr.getID());
            generateUUIDSForAllRequirementGroups(cr.getRequirementGroups(), counter);
        }
    }

    default void generateUUIDSForAllRequirementGroups(List<RequirementGroup> reqGroups, int counter) {
        for (RequirementGroup reqGroup : reqGroups) {
            counter++;
            reqGroup.setUUID(String.format("%s-%d", reqGroup.getID(), counter));
            generateUUIDSForAllRequirementGroups(reqGroup.getRequirementGroups(), counter);
            List<Requirement> reqs = reqGroup.getRequirements();
            for (Requirement req : reqs) {
                counter++;
                req.setUUID(String.format("%s-%d", req.getID(), counter));
            }
        }
    }
}