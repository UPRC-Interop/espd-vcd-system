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

import eu.esens.espdvcd.codelist.enums.ecertis.ECertisLanguageCodeEnum;
import eu.esens.espdvcd.retriever.criteria.resource.enums.ResourceConfig;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 17/10/2020.
 */
public class ECertisURI {

    private static final Logger LOGGER = Logger.getLogger(ECertisURI.class.getName());

    private ECertisLanguageCodeEnum lang;
    private String id;
    private URI uri;

    private ECertisURI(Builder builder) {
        this.lang = builder.lang;
        this.id = builder.id;
    }

    public URI asURI() {
        if (Objects.isNull(uri)) {
            URIBuilder uriBuilder = new URIBuilder().setScheme(ResourceConfig.INSTANCE.getECertisScheme())
                .setHost(ResourceConfig.INSTANCE.getECertisHost())
                .setPath(createPath());
            applyQueryParamsIfNotNull(uriBuilder);
            try {
                uri = uriBuilder.build();
            } catch (URISyntaxException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        return uri;
    }

    private String createPath() {

        String path;

        if (Objects.isNull(id)) {
            path = ResourceConfig.INSTANCE.getECertisCriteriaPath();
        } else {
            path = ResourceConfig.INSTANCE.useBackwardsCompatibleECertis()
                ? ResourceConfig.INSTANCE.getECertisCriteriaPath() + "/espd/" + id
                : ResourceConfig.INSTANCE.getECertisCriteriaPath() + "/" + id;
        }

        return path;
    }

    private void applyQueryParamsIfNotNull(URIBuilder uriBuilder) {

        if (lang != null) {
            uriBuilder.setParameter("lang", lang.name().toLowerCase());
        }

    }

    public static Builder baseURL() {
        return new Builder();
    }

    public static class Builder {

        private ECertisLanguageCodeEnum lang;
        private String id;

        public Builder withLang(ECertisLanguageCodeEnum lang) {
            this.lang = lang;
            return this;
        }

        public Builder withCriterionId(String id) {
            this.id = id;
            return this;
        }

        public ECertisURI build() {
            return new ECertisURI(this);
        }

    }

    @Override
    public String toString() {
        return "ECertisURI{" + asURI().toString() + "}";
    }
}
