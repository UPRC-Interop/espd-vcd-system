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
package eu.esens.espdvcd.retriever.criteria.resource.tasks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisDomainIdEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisNationalEntityEnum;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis
 */
public class GetECertisCriterionRetryingTask implements Callable<ECertisCriterion> {

    private static final Logger LOGGER = Logger.getLogger(GetECertisCriterionRetryingTask.class.getName());

    private URI uri;

    public GetECertisCriterionRetryingTask(String ID) {
        this(ID, EULanguageCodeEnum.EN);
    }

    public GetECertisCriterionRetryingTask(String ID, EULanguageCodeEnum lang) {
        this(ID, lang, null);
    }

    public GetECertisCriterionRetryingTask(String ID,
                                           EULanguageCodeEnum lang,
                                           ECertisNationalEntityEnum nationalEntity) {

        this(ID, lang, nationalEntity, null, null);
    }

    public GetECertisCriterionRetryingTask(String ID,
                                           EULanguageCodeEnum lang,
                                           ECertisNationalEntityEnum nationalEntity,
                                           ECertisDomainIdEnum.ECertisScenarioIdEnum scenarioID,
                                           ECertisDomainIdEnum domainID) {

        try {
            uri = new ECertisURIBuilder()
                    .lang(ECertisLanguageCodeEnum.valueOf(lang.name()))
                    .nationalEntity(nationalEntity)
                    .domainId(domainID)
                    .scenarioId(scenarioID)
                    .buildCriterionURI(ID);

        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public ECertisCriterion call() throws ExecutionException, RetryException, IOException {
        GetFromECertisTask task = new GetFromECertisTask(uri);
        GetFromECertisRetryingTask rTask = new GetFromECertisRetryingTask(task);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.readValue(rTask.call(), ECertisCriterionImpl.class);
    }

}
