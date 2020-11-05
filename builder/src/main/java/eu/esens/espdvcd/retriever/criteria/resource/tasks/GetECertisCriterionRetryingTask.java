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

    private GetECertisCriterionRetryingTask(Builder builder) {
        try {
            this.uri = new ECertisURIBuilder()
                    .lang(ECertisLanguageCodeEnum.valueOf(builder.lang.name()))
                    .countryFilter(builder.countryFilter)
                    .buildCriterionURI(builder.id);
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

    public static class Builder {

        /* mandatory params */
        private final String id;
        /* optional params */
        private EULanguageCodeEnum lang = EULanguageCodeEnum.EN;
        private ECertisNationalEntityEnum countryFilter;

        /**
         * @param id The Criterion id (UUID).
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * @param lang The language that the criterions data will be returned.
         * @return
         */
        public Builder lang(EULanguageCodeEnum lang) {
            if (lang != null) {
                this.lang = lang;
            }
            return Builder.this;
        }

        /**
         * @param countryFilter The country for which criterion data will be returned.
         * @return
         */
        public Builder countryFilter(ECertisNationalEntityEnum countryFilter) {
            this.countryFilter = countryFilter;
            return Builder.this;
        }

        public GetECertisCriterionRetryingTask build() {
            return new GetECertisCriterionRetryingTask(Builder.this);
        }

    }

}
