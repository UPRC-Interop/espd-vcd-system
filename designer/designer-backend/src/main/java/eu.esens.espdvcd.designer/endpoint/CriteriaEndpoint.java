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
package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.esens.espdvcd.designer.service.CriteriaService;
import eu.esens.espdvcd.designer.util.ErrorResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Objects;

public class CriteriaEndpoint extends Endpoint {

    private final CriteriaService service;
    private final String CRITERIA_ERROR = "Criteria requested not found.",
            RETRIEVER_ERROR = "Retriever failed to get criteria. Additional info: \n";

    public CriteriaEndpoint(CriteriaService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {

        spark.path(basePath+"/criteria", () -> {
            spark.get("", (request, response) -> {
                String lang = request.queryParams("lang");
                return getNoFilter(response, lang);
            });

            spark.get("/", (request, response) -> {
                String lang = request.queryParams("lang");
                return getNoFilter(response, lang);
            });

            spark.get("/getFilters", this::getFilters);

            spark.get("/getFilters/", this::getFilters);

            spark.get("/:filter", (request, response) -> {
                String lang = request.queryParams("lang");
                return getFilter(request, response, lang);
            });

            spark.get("/:filter/", (request, response) -> {
                String lang = request.queryParams("lang");
                return getFilter(request, response, lang);
            });

        });

    }

    private Object getNoFilter(Response response, String lang) throws JsonProcessingException {
        response.header("Content-Type", "application/json");
        if (Objects.isNull(lang)) {
            try {
                return WRITER.writeValueAsString(service.getCriteria());
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(502, RETRIEVER_ERROR + e.getMessage()).build());
            }
        } else {
            try {
                return WRITER.writeValueAsString(service.getTranslatedCriteria(lang));
            } catch (UnsupportedOperationException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(502, RETRIEVER_ERROR + e.getMessage()).build());
            }
        }
    }

    private Object getFilter(Request request, Response response, String lang) throws JsonProcessingException {
        response.header("Content-Type", "application/json");
        if (Objects.isNull(lang)) {
            try {
                return WRITER.writeValueAsString(service.getFilteredCriteriaList(request.params("filter").toUpperCase()));
            } catch (IllegalArgumentException e) {
                response.status(404);
                LOGGER.warning(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CRITERIA_ERROR).build());
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(502, RETRIEVER_ERROR + e.getMessage()).build());
            }
        } else {
            try {
                return WRITER.writeValueAsString(service.getFilteredTranslatedCriteriaList(request.params("filter").toUpperCase(), lang));
            } catch (IllegalArgumentException e) {
                response.status(404);
                LOGGER.warning(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CRITERIA_ERROR).build());
            } catch (UnsupportedOperationException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(502, RETRIEVER_ERROR + e.getMessage()).build());
            }
        }
    }

    private Object getFilters(Request request, Response response) throws JsonProcessingException{
        response.header("Content-Type", "application/json");
        return WRITER.writeValueAsString(service.getCriteriaFilters());
    }
}
