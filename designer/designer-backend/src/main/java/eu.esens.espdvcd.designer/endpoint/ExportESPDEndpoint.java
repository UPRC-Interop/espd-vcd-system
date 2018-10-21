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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ExportESPDService;
import eu.esens.espdvcd.designer.service.RegulatedExportESPDV1Service;
import eu.esens.espdvcd.designer.service.RegulatedExportESPDV2Service;
import eu.esens.espdvcd.designer.util.ErrorResponse;
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
import java.text.MessageFormat;

public class ExportESPDEndpoint extends Endpoint {
    private static final String ESPD_RESPONSE = "espd-response";
    private static final String ESPD_REQUEST = "espd-request";
    private static final String PDF_SUFFIX = ".pdf";
    private static final String HTML_SUFFIX = ".html";
    private static final String XML_SUFFIX = ".xml";
    private final ExportESPDService service;
    private final String DESERIALIZATION_ERROR = "Oops, the provided JSON document was not valid and could not be converted to an object. Did you provide the correct format? \nThis could help you:\n",
            LOGGER_DESERIALIZATION_ERROR = "Error occurred in ESPDEndpoint while converting a JSON object to XML. ";

    public ExportESPDEndpoint(EDMVersion version) {
        MAPPER.registerModule(new SimpleModule().addDeserializer(Requirement.class, new RequirementDeserialiser(version)));
        switch (version) {
            case V1:
                this.service = new RegulatedExportESPDV1Service();
                break;
            case V2:
                this.service = new RegulatedExportESPDV2Service();
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
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(405, "You need to POST an artefact in json format.").build());
            }));

            spark.post("/request/xml", this::handleESPDXmlRequest);

            spark.post("/request/xml/", this::handleESPDXmlRequest);

            spark.post("/request/pdf", this::handleESPDPdfRequest);

            spark.post("/request/pdf/", this::handleESPDPdfRequest);

            spark.post("/request/html", this::handleESPDHtmlRequest);

            spark.post("/request/html/", this::handleESPDHtmlRequest);

            spark.post("/response/xml", this::handleESPDXmlResponse);

            spark.post("/response/xml/", this::handleESPDXmlResponse);

            spark.post("/response/pdf", this::handleESPDPdfResponse);

            spark.post("/response/pdf/", this::handleESPDPdfResponse);

            spark.post("/response/html", this::handleESPDHtmlResponse);

            spark.post("/response/html/", this::handleESPDHtmlResponse);
        });
    }

    private Object handleESPDXmlRequest(Request request, Response response) throws JsonProcessingException {
        return handleESPDRequest(request, response, ExportType.XML);
    }

    private Object handleESPDHtmlRequest(Request request, Response response) throws JsonProcessingException {
        return handleESPDRequest(request, response, ExportType.HTML);
    }

    private Object handleESPDPdfRequest(Request request, Response response) throws JsonProcessingException {
        return handleESPDRequest(request, response, ExportType.PDF);
    }

    private Object handleESPDHtmlResponse(Request request, Response response) throws JsonProcessingException {
        return handleESPDResponse(request, response, ExportType.HTML);
    }

    private Object handleESPDPdfResponse(Request request, Response response) throws JsonProcessingException {
        return handleESPDResponse(request, response, ExportType.PDF);
    }

    private Object handleESPDXmlResponse(Request request, Response response) throws JsonProcessingException {
        return handleESPDResponse(request, response, ExportType.XML);
    }

    private Object handleESPDRequest(Request rq, Response rsp, ExportType exportType) throws JsonProcessingException {
        if (rq.contentType().contains("application/json")) {
            ESPDRequest document;
            try {
                document = MAPPER.readValue(rq.body(), RegulatedESPDRequest.class);
            } catch (IOException e) {
                rsp.status(400);
                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                rsp.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(400, DESERIALIZATION_ERROR + e.getMessage()).build());
            }
            rsp.header("Content-Type", "application/octet-stream");
            rsp.header("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}\";", getRequestFilename(exportType)));

            try {
                return exportESPDRequest(exportType, document);

            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
                rsp.status(406);
                rsp.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            rsp.header("Content-Type", "application/json");
            return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, "Unacceptable content-type specified.").build());
        }
    }

    private Object exportESPDRequest(ExportType exportType, ESPDRequest document) throws ValidationException {
        switch (exportType) {
            case PDF:
                return service.exportESPDRequestPdfAsInputStream(document);
            case XML:
                return service.exportESPDRequestAsInputStream(document);
            case HTML:
                return service.exportESPDRequestHtmlAsInputStream(document);
            default:
                throw new IllegalArgumentException(MessageFormat.format("No export operation defined for type ''{0}''.", exportType.name()));
        }
    }

    private Object handleESPDResponse(Request rq, Response rsp, ExportType exportType) throws JsonProcessingException {
        if (rq.contentType().contains("application/json")) {
            ESPDResponse document;
            try {
                document = MAPPER.readValue(rq.body(), RegulatedESPDResponse.class);
            } catch (IOException e) {
                rsp.status(400);
                rsp.header("Content-Type", "application/json");
                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(400, DESERIALIZATION_ERROR + e.getMessage()).build());
            }
            rsp.header("Content-Type", "application/octet-stream");
            rsp.header("Content-Disposition", MessageFormat.format("attachment; filename=\"{0}\";", getReponseFilename(exportType)));
            try {
                return exportESPDResponse(exportType, document);
            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
                rsp.status(406);
                rsp.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            rsp.header("Content-Type", "application/json");
            return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, "Unacceptable content-type specified.").build());
        }
    }

    private Object exportESPDResponse(ExportType exportType, ESPDResponse document) throws ValidationException {
        switch (exportType) {
            case PDF:
                return service.exportESPDResponsePdfAsInputStream(document);
            case XML:
                return service.exportESPDResponseAsInputStream(document);
            case HTML:
                return service.exportESPDResponseHtmlAsInputStream(document);
            default:
                throw new IllegalArgumentException(MessageFormat.format("No export operation defined for type ''{0}''.", exportType.name()));
        }
    }

    private String getRequestFilename(ExportType exportType) {
        return MessageFormat.format("{0}{1}", ESPD_REQUEST, getSuffix(exportType));
    }

    private String getReponseFilename(ExportType exportType) {
        return MessageFormat.format("{0}{1}", ESPD_RESPONSE, getSuffix(exportType));
    }

    private String getSuffix(ExportType exportType) {
        String suffix;
        switch (exportType) {
            case PDF:
                suffix = PDF_SUFFIX;
                break;
            case XML:
                suffix = XML_SUFFIX;
                break;
            case HTML:
                suffix = HTML_SUFFIX;
                break;
            default:
                throw new IllegalArgumentException(MessageFormat.format("No suffix defined for type ''{0}''.", exportType.name()));
        }
        return suffix;
    }

    enum ExportType {
        PDF, HTML, XML
    }
}
