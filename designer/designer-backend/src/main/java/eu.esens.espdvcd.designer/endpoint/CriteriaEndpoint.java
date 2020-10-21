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
package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.designer.service.CriteriaService;
import eu.esens.espdvcd.designer.service.NationalCriteriaEvidenceService;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Objects;

public class CriteriaEndpoint extends Endpoint {

    private final CriteriaService service;
    private static final String CONTRACTING_OPERATOR_PARAM = "contractingOperator";

    public CriteriaEndpoint(CriteriaService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {

        spark.path(basePath + "/criteria", () -> {
            spark.get("", this::getNoFilter, JsonUtil.json());
            spark.get("/getFilters", this::getFilters, JsonUtil.json());
            spark.get("/:filter", this::getFilter, JsonUtil.json());
            spark.get("/eCertisData/:criterionID/country/:countryCode", this::getNational, JsonUtil.json());
            spark.get("/eCertisData/:criterionID/country/:countryCode/lang/:lang", this::getNational, JsonUtil.json());
        });
    }

    private Object getNoFilter(Request request, Response response) throws JsonProcessingException {
        ContractingOperatorEnum contractingOperatorEnum;
        response.type("application/json");
        try {
            contractingOperatorEnum = ContractingOperatorEnum.valueOf(
                    request.queryParams(CONTRACTING_OPERATOR_PARAM));
        } catch (IllegalArgumentException | NullPointerException e) {
            contractingOperatorEnum = ContractingOperatorEnum.CONTRACTING_ENTITY;
        }
        try {
            return service.getCriteria(contractingOperatorEnum);
        } catch (RetrieverException e) {
            response.status(502);
            LOGGER.severe(e.getMessage());
            return Errors.retrieverError(e.getMessage());
        }

    }

    private Object getFilter(Request request, Response response) throws JsonProcessingException {
        ContractingOperatorEnum contractingOperatorEnum;
        response.type("application/json");
        try {
            contractingOperatorEnum = ContractingOperatorEnum.valueOf(
                    request.queryParams(CONTRACTING_OPERATOR_PARAM));
        } catch (IllegalArgumentException | NullPointerException e) {
            contractingOperatorEnum = ContractingOperatorEnum.CONTRACTING_ENTITY;
        }
        try {
            return service.getFilteredCriteriaList(request.params("filter").toUpperCase(), contractingOperatorEnum);
        } catch (IllegalArgumentException e) {
            response.status(404);
            LOGGER.warning(e.getMessage());
            return Errors.criteriaNotFoundError();
        } catch (RetrieverException e) {
            response.status(502);
            LOGGER.severe(e.getMessage());
            return Errors.retrieverError(e.getMessage());
        }
    }

    private Object getNational(Request request, Response response) throws JsonProcessingException {
        String lang = request.params("lang");
        String countryCode = request.params("countryCode");
        String criterionID = request.params("criterionID");
        response.type("application/json");
        if (Objects.nonNull(lang) && Objects.nonNull(countryCode) && Objects.nonNull(criterionID)) {
            try {
                return NationalCriteriaEvidenceService.getInstance().getTranslatedEvidence(criterionID, countryCode, lang);
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return Errors.retrieverError(e.getMessage());
            } catch (IllegalArgumentException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return Errors.notAcceptableError("Language code does not exist.");
            }
        } else if (Objects.isNull(lang) && Objects.nonNull(countryCode) && Objects.nonNull(criterionID)) {
            try {
                return NationalCriteriaEvidenceService.getInstance().getDefaultEvidence(criterionID, countryCode);
            } catch (RetrieverException e) {
                response.status(502);
                LOGGER.severe(e.getMessage());
                return Errors.retrieverError(e.getMessage());
            } catch (IllegalArgumentException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return Errors.notAcceptableError("Language code does not exist.");
            }
        } else {
            response.status(406);
            return Errors.notAcceptableError("Please specify the country code and the criterionID.");
        }
    }

    private Object getFilters(Request request, Response response) throws JsonProcessingException {
        response.type("application/json");
        return service.getCriteriaFilters();
    }
}
