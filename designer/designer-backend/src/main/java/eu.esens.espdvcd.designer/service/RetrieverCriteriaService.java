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

import eu.esens.espdvcd.designer.typeEnum.CriteriaType;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractorBuilder;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.EDMVersion;

import java.util.List;

public class RetrieverCriteriaService implements CriteriaService {

    private final CriteriaExtractor predefinedExtractor;
    private int counter = 0;

    public RetrieverCriteriaService(EDMVersion version) {
        CriteriaExtractorBuilder b = new CriteriaExtractorBuilder(version);
        predefinedExtractor = b.build();
    }

    @Override
    public List<SelectableCriterion> getCriteria() throws RetrieverException {
        List<SelectableCriterion> criteria = predefinedExtractor.getFullList();
        counter = 0;
        criteria.forEach(cr -> {
            cr.setUUID(cr.getID());
            idFix(cr.getRequirementGroups());
        });
        return criteria;
    }

    @Override
    public List<SelectableCriterion> getUnselectedCriteria(List<SelectableCriterion> initialList) throws RetrieverException {
        return predefinedExtractor.getFullList(initialList);
    }

    @Override
    public List<SelectableCriterion> getTranslatedCriteria(String lang) {
        throw new UnsupportedOperationException("Translation is not yet supported for the criteria");
    }

    @Override
    public CriteriaType[] getCriteriaFilters() {
        return CriteriaType.values();
    }

    private void idFix(List<RequirementGroup> reqGroups) {
        counter++;
        for (RequirementGroup reqGroup : reqGroups) {
            reqGroup.setUUID(reqGroup.getID() + "-" + counter);
            idFix(reqGroup.getRequirementGroups());
            List<Requirement> reqs = reqGroup.getRequirements();
            reqs.forEach(req -> req.setUUID(req.getID() + "-" + counter));
        }
    }
}

