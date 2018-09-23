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

public class ExportESPDEndpoint extends Endpoint {
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

            spark.post("/request", this::handleESPDRequest);

            spark.post("/request/", this::handleESPDRequest);

            spark.post("/response", this::handleESPDResponse);

            spark.post("/response/", this::handleESPDResponse);
        });
    }

    private Object handleESPDRequest(Request rq, Response rsp) throws JsonProcessingException {
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
            rsp.header("Content-Disposition", "attachment; filename=\"espd-request.xml\";");

            try {
                return service.exportESPDRequestAsInputStream(document);
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

    private Object handleESPDResponse(Request rq, Response rsp) throws JsonProcessingException {
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
            rsp.header("Content-Disposition", "attachment; filename=\"espd-response.xml\";");
            try {
                return service.exportESPDResponseAsInputStream(document);
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
}
