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
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ExportESPDService;
import eu.esens.espdvcd.designer.service.ExportESPDV1Service;
import eu.esens.espdvcd.designer.service.ExportESPDV2Service;
import eu.esens.espdvcd.designer.typeEnum.ExportType;
import eu.esens.espdvcd.designer.util.Config;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDRequestImpl;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.ESPDResponseImpl;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.schema.EDMVersion;
import spark.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ExportESPDEndpoint extends Endpoint {
    private static final String LANGUAGE = "language";
    private final ExportESPDService service;
    private final String LOGGER_DESERIALIZATION_ERROR = "Error occurred in ESPDEndpoint while converting a JSON object to XML.";

    public ExportESPDEndpoint(EDMVersion version) {
        MAPPER.registerModule(new SimpleModule().addDeserializer(Requirement.class, new RequirementDeserialiser(version)));
        switch (version) {
            case V1:
                this.service = ExportESPDV1Service.getInstance();
                break;
            case V2:
                this.service = ExportESPDV2Service.getInstance();
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

            spark.post("/:artefactType/:exportType",
                    (rq, rsp) -> {
                        ExportType exportType;
                        InputStream streamToReturn;
                        try {
                            exportType = ExportType.valueOf(rq.params("exportType").toUpperCase());
                        } catch (IllegalArgumentException e) {
                            rsp.type("application/json");
                            return JsonUtil.toJson(Errors.standardError(404, "Requested export type not found."));
                        }

                        EULanguageCodeEnum languageCode;
                        try {
                            languageCode = EULanguageCodeEnum.valueOf(rq.queryParams(LANGUAGE).toUpperCase());
                        } catch (IllegalArgumentException e) {
                            rsp.status(404);
                            return JsonUtil.toJson(Errors.standardError(404, "Requested language not found."));
                        }

                        String artefactType = rq.params("artefactType");
                        if (rq.contentType().contains("application/json")) {

                            if (Config.isArtefactDumpingEnabled()) {
                                Files.createDirectories(Paths.get(Config.dumpIncomingArtefactsLocation() + "/json/"));
                                File dumpedFile = new File(Config.dumpIncomingArtefactsLocation()
                                        + "/json/" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMddHHmmss")) + ".json");
                                byte[] jsonStream = rq.body().getBytes(StandardCharsets.UTF_8);
                                Files.write(dumpedFile.toPath(), jsonStream, StandardOpenOption.CREATE);
                                LOGGER.info("Dumping exported json artefact to " + dumpedFile.getAbsolutePath());
                            }

                            try {
                                if (artefactType.equalsIgnoreCase("request")) {
                                    //Handle request
                                    ESPDRequest document;
                                    document = MAPPER.readValue(rq.body(), ESPDRequestImpl.class);
                                    streamToReturn = service.exportESPDRequestAs(document, languageCode, exportType);
                                } else if (artefactType.equalsIgnoreCase("response")) {
                                    //Handle response
                                    ESPDResponse document;
                                    document = MAPPER.readValue(rq.body(), ESPDResponseImpl.class);
                                    streamToReturn = service.exportESPDResponseAs(document, languageCode, exportType);
                                } else {
                                    rsp.status(400);
                                    return JsonUtil.toJson(Errors.standardError(
                                            400,
                                            "You need to specify if the ESPD is a request or response."));
                                }
                                rsp.type("application/xml");
                                rsp.header("Content-Disposition", String.format(
                                        "attachment; filename=\"%s.%s\";", artefactType.toLowerCase(), exportType.name().toLowerCase()));
                                return streamToReturn;
                            } catch (IOException e) {
                                rsp.status(400);
                                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                                rsp.type("application/json");
                                return JsonUtil.toJson(Errors.artefactDeserialisationError(e.getMessage()));
                            } catch (ValidationException e) {
                                LOGGER.severe(e.getMessage());
                                rsp.status(406);
                                rsp.type("application/json");
                                return JsonUtil.toJson(Errors.validationError(e.getMessage(), e.getResults()));
                            } catch (UnsupportedOperationException e) {
                                rsp.status(406);
                                rsp.type("application/json");
                                return JsonUtil.toJson(Errors.notAcceptableError(e.getMessage()));
                            }
                        } else {
                            LOGGER.warning("Got unexpected content-type: " + rq.contentType());
                            rsp.status(406);
                            rsp.type("application/json");
                            return JsonUtil.toJson(Errors.unacceptableContentType());
                        }
                    });
        });
    }
}
