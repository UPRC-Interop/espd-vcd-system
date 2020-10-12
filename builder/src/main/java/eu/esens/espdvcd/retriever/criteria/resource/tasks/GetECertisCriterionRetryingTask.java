/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Konstantinos Raptis
 */
public class GetECertisCriterionRetryingTask implements Callable<ECertisCriterion> {

    private String ID;
    private EULanguageCodeEnum lang;

    public GetECertisCriterionRetryingTask(String ID) {
        this(ID, EULanguageCodeEnum.EN);
    }

    public GetECertisCriterionRetryingTask(String ID, EULanguageCodeEnum lang) {
        this.ID = ID;
        this.lang = lang;
    }

    @Override
    public ECertisCriterion call() throws ExecutionException, RetryException, IOException {
        String theLang = lang.name().toLowerCase();

        GetFromECertisTask task = new GetFromECertisTask(ResourceConfig.INSTANCE.getECertisCriteriaURL() + "/" + ID + "?lang=" + theLang);
        GetFromECertisRetryingTask rTask = new GetFromECertisRetryingTask(task);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.readValue(rTask.call(), ECertisCriterionImpl.class);
    }

}
