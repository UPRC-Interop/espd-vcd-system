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
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;
import eu.esens.espdvcd.retriever.criteria.resource.utils.ECertisURI;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis
 */
public class GetECertisCriterionRetryingTask implements Callable<ECertisCriterion> {

    private static final Logger LOGGER = Logger.getLogger(GetECertisCriterionRetryingTask.class.getName());

    private final URI uri;

    private GetECertisCriterionRetryingTask(Builder builder) {
        this.uri = ECertisURI.baseURL()
            .withLang(builder.lang)
            .withCriterionId(builder.id)
            .build().asURI();
        System.out.println("URI " + uri.toString());
    }

    @Override
    public ECertisCriterion call() throws ExecutionException, RetryException, IOException {

        GetFromECertisTask task = new GetFromECertisTask(uri);
        GetFromECertisRetryingTask rTask = new GetFromECertisRetryingTask(task);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        try {
            return mapper.readValue(rTask.call(), ECertisCriterionImpl.class);
        } catch (MismatchedInputException e) {
            LOGGER.log(Level.SEVERE, "MismatchedInputException when reading " + uri.toString());
            throw e;
        }
    }

    public static Builder forCriterion(String id) {
        return new Builder(id);
    }

    public static class Builder {

        /* mandatory params */
        private final String id;
        /* optional params */
        private ECertisLanguageCodeEnum lang;

        /**
         * @param id The Criterion id (UUID).
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * @param lang The language that the Criteria data will be returned.
         * @return
         */
        public Builder lang(@Nullable ECertisLanguageCodeEnum lang) {
            this.lang = Optional.ofNullable(lang).orElse(ECertisLanguageCodeEnum.EN);
            return Builder.this;
        }

        public GetECertisCriterionRetryingTask build() {
            return new GetECertisCriterionRetryingTask(Builder.this);
        }

    }

}
