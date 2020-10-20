/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria.resource.utils;

import eu.esens.espdvcd.codelist.enums.ecertis.ECertisDomainIdEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 17/10/2020.
 */
public class ECertisURIBuilder {

    private ECertisNationalEntityEnum nationalEntity;
    private ECertisLanguageCodeEnum lang;
    private ECertisDomainIdEnum domainId;
    private ECertisDomainIdEnum.ECertisScenarioIdEnum scenarioId;

    public ECertisURIBuilder lang(ECertisLanguageCodeEnum lang) {
        this.lang = lang;
        return ECertisURIBuilder.this;
    }

    public ECertisURIBuilder nationalEntity(ECertisNationalEntityEnum nationalEntity) {
        this.nationalEntity = nationalEntity;
        return ECertisURIBuilder.this;
    }

    public ECertisURIBuilder domainId(ECertisDomainIdEnum domainId) {
        this.domainId = domainId;
        return ECertisURIBuilder.this;
    }

    public ECertisURIBuilder scenarioId(ECertisDomainIdEnum.ECertisScenarioIdEnum scenarioId) {
        this.scenarioId = scenarioId;
        return ECertisURIBuilder.this;
    }

    /**
     * Without any parameters, it creates an e-Certis URI of all EU criteria,
     * that are not parent criteria.
     *
     * @return
     */
    public URI buildCriteriaURI() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder().setScheme(ResourceConfig.INSTANCE.getECertisScheme())
                .setHost(ResourceConfig.INSTANCE.getECertisHost())
                .setPath(ResourceConfig.INSTANCE.getECertisCriteriaPath());
        applyParamsIfNotNull(uriBuilder);
        return uriBuilder.build();
    }

    /**
     * Creates an e-Certis URI for the specific criterion.
     *
     * @param id The criterion UUID
     * @return
     */
    public URI buildCriterionURI(String id) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder().setScheme(ResourceConfig.INSTANCE.getECertisScheme())
                .setHost(ResourceConfig.INSTANCE.getECertisHost())
                .setPath(ResourceConfig.INSTANCE.getECertisCriteriaPath() + "/" + id);
        applyParamsIfNotNull(uriBuilder);
        return uriBuilder.build();
    }

    private void applyParamsIfNotNull(URIBuilder uriBuilder) {

        if (lang != null) {
            uriBuilder.setParameter("lang", lang.name().toLowerCase());
        }

        if (nationalEntity != null) {
            uriBuilder.setParameter("nationalEntity", nationalEntity.name().toLowerCase());
        }

        if (domainId != null && scenarioId != null) {
            uriBuilder.setParameter("domainId", domainId.name().toLowerCase());
            uriBuilder.setParameter("scenarioId", scenarioId.name().toLowerCase());
        }

    }

}
