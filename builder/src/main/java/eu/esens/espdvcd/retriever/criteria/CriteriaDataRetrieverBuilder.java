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
package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.EvidencesResource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverBuilder {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverBuilder.class.getName());

    private List<EvidencesResource> eResourceList;
    private ECertisResource eCertisResource;

    public CriteriaDataRetrieverBuilder() {
        eResourceList = new ArrayList<>();
    }

    public CriteriaDataRetrieverBuilder addEvidencesResource(EvidencesResource resource) {
        eResourceList.add(resource);
        return CriteriaDataRetrieverBuilder.this;
    }

    public CriteriaDataRetriever build() {

        if (eResourceList.isEmpty()) {
            eResourceList.add(createDefaultEvidencesResource());
        }

        return new CriteriaDataRetrieverImpl(eResourceList);
    }

    private EvidencesResource createDefaultEvidencesResource() {
        LOGGER.log(Level.INFO, "Creating default evidences resource");
        initECertisResource();

        return eCertisResource;
    }

    /**
     * Lazy initialization of default eCertis resource
     */
    private void initECertisResource() {

        if (eCertisResource == null) {
            eCertisResource = new ECertisResource();
            LOGGER.log(Level.INFO, "eCertis Resource initialized");
        }
    }

}
