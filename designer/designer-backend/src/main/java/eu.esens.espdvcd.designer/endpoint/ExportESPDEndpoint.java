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
package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ExportESPDService;
import eu.esens.espdvcd.designer.service.RegulatedExportESPDV1Service;
import eu.esens.espdvcd.designer.service.RegulatedExportESPDV2Service;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.schema.EDMVersion;
import spark.Request;
import spark.Response;
import spark.Service;

import java.io.IOException;

public class ExportESPDEndpoint extends Endpoint {
    private final ExportESPDService service;
    private static final String LOGGER_DESERIALIZATION_ERROR = "Error occurred in ESPDEndpoint while converting a JSON to XML. ";

    public ExportESPDEndpoint(EDMVersion version) {
        MAPPER.registerModule(new SimpleModule().addDeserializer(Requirement.class, new RequirementDeserialiser(version)));
        switch (version) {
            case V1:
                this.service = RegulatedExportESPDV1Service.getInstance();
                break;
            case V2:
                this.service = RegulatedExportESPDV2Service.getInstance();
                break;
            default:
                throw new IllegalArgumentException("Version supplied cannot be null.");
        }
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath + "/espd", () -> {
            spark.get("*", ((request, response) -> {
                response.status(405);
                response.header("Content-Type", "application/json");
                return Errors.artefactInWrongFormatError();
            }), JsonUtil.json());

            spark.post("/request", this::handleESPDRequest, JsonUtil.json());

            spark.post("/response", this::handleESPDResponse, JsonUtil.json());
        });
    }

    private Object handleESPDRequest(Request rq, Response rsp) {
        if (rq.contentType().contains("application/json")) {
            ESPDRequest document;
            try {
                document = MAPPER.readValue(rq.body(), RegulatedESPDRequest.class);
            } catch (IOException e) {
                rsp.status(400);
                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                rsp.header("Content-Type", "application/json");
                return Errors.artefactDeserialisationError(e.getMessage());
            }
            rsp.header("Content-Type", "application/octet-stream");
            rsp.header("Content-Disposition", "attachment; filename=\"espd-request.xml\";");

            try {
                return service.exportESPDRequestAsInputStream(document);
            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
                rsp.status(406);
                rsp.header("Content-Type", "application/json");
                return Errors.notAcceptableError(e.getMessage());
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            rsp.header("Content-Type", "application/json");
            return Errors.unacceptableContentType();
        }
    }

    private Object handleESPDResponse(Request rq, Response rsp) {
        if (rq.contentType().contains("application/json")) {
            ESPDResponse document;
            try {
                document = MAPPER.readValue(rq.body(), RegulatedESPDResponse.class);
            } catch (IOException e) {
                rsp.status(400);
                rsp.header("Content-Type", "application/json");
                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                return Errors.artefactDeserialisationError(e.getMessage());
            }
            rsp.header("Content-Type", "application/octet-stream");
            rsp.header("Content-Disposition", "attachment; filename=\"espd-response.xml\";");
            try {
                return service.exportESPDResponseAsInputStream(document);
            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
                rsp.status(406);
                rsp.header("Content-Type", "application/json");
                return Errors.notAcceptableError(e.getMessage());
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            rsp.header("Content-Type", "application/json");
            return Errors.unacceptableContentType();
        }
    }
}
