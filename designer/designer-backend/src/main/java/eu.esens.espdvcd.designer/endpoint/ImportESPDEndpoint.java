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

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ImportESPDService;
import eu.esens.espdvcd.designer.util.AppConfig;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.util.IOUtils;
import spark.Service;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import javax.xml.bind.UnmarshalException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

public class ImportESPDEndpoint extends Endpoint {
    private final ImportESPDService service;

    public ImportESPDEndpoint(ImportESPDService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                response.type("application/json");
                return Errors.standardError(405, "You need to POST an XML artefact.");
            }), JsonUtil.json());

            spark.post("", (rq, rsp) -> {
                rsp.header("Content-Type", "application/json");
                if (Objects.nonNull(rq.contentType())) {
                    String LOGGER_DOCUMENT_ERROR = "Error occurred in ESPDEndpoint while converting an XML response to an object. ";
                    if (rq.contentType().contains("multipart/form-data")) {
                        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                                "file-uploads",
                                1024 * 1024 * AppConfig.getInstance().getMaxFileSize(),
                                1024 * 1024 * AppConfig.getInstance().getMaxFileSize(),
                                0);
                        rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                        Collection<Part> parts;
                        try {
                            parts = rq.raw().getParts();
                        } catch (IllegalStateException e) {
                            return Errors.standardError(422, "Your file is too large, please upload a file up to 2MB.");
                        }
                        if (parts.iterator().hasNext()) {
                            Part part = parts.iterator().next();
                            File tempFile = File.createTempFile("espd-file", ".tmp");
                            tempFile.deleteOnExit();

                            try (InputStream input = part.getInputStream();
                                 FileOutputStream out = new FileOutputStream(tempFile)) {
                                IOUtils.copy(input, out);
                            } catch (IOException e) {
                                LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                                rsp.status(500);
                                return Errors.standardError(500, "Unable to save uploaded file. More info:\n" + e.getMessage());
                            }

                            if (part.getSubmittedFileName().endsWith("json")) {
                                return new String(IOUtils.toByteArray(part.getInputStream()));
                            }else if (!(part.getSubmittedFileName().endsWith("xml") && part.getContentType().contains("xml"))){
                                return Errors.standardError(422, "Please upload an XML file.");
                            }
                            writeDumpedFile(tempFile);
                            try {
                                return service.importESPDFile(tempFile);
                            } catch (RetrieverException | BuilderException | NullPointerException e) {
                                LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                                rsp.status(406);
                                return Errors.notAcceptableError(e.getMessage());
                            } catch (IllegalStateException e) {
                                LOGGER.severe(e.getMessage());
                                rsp.status(406);
                                return Errors.standardError(406, e.getMessage());
                            } catch (ValidationException e) {
                                LOGGER.severe(e.getMessage());
                                rsp.status(406);
                                return Errors.validationError(e.getMessage(), e.getResults());
                            }
//                finally {
//                     Files.deleteIfExists(tempFile.toPath());
//                }
                        } else {
                            rsp.status(400);
                            return Errors.standardError(400, "There was no file found in your upload, please check your input.");
                        }
                    } else if (rq.contentType().contains("application/xml")) {
                        Path tempFile = Files.createTempFile("espd-file", ".tmp");
                        try {
                            byte[] xmlStream = rq.body().getBytes(StandardCharsets.UTF_8);
                            Files.write(tempFile, xmlStream, StandardOpenOption.CREATE);
                            File espdFile = tempFile.toFile();

                            writeDumpedFile(espdFile);

                            return service.importESPDFile(espdFile);
                        } catch (RetrieverException | BuilderException | UnmarshalException e) {
                            LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                            rsp.status(406);
                            return Errors.notAcceptableError(e.getMessage());
                        } catch (IllegalStateException e) {
                            LOGGER.severe(e.getMessage());
                            rsp.status(406);
                            return Errors.standardError(406, e.getMessage());
                        } catch (ValidationException e) {
                            LOGGER.severe(e.getMessage());
                            rsp.status(406);
                            return Errors.validationError(e.getMessage(), e.getResults());
                        } finally {
                            Files.deleteIfExists(tempFile);
                        }
                    } else {
                        LOGGER.warning("Got unexpected content-type: " + rq.contentType());
                        rsp.status(406);
                        return Errors.unacceptableContentType();
                    }
                } else {
                    LOGGER.warning("Got null content-type");
                    rsp.status(406);
                    return Errors.unacceptableContentType();
                }
            }, JsonUtil.json());
        });
    }

    private void writeDumpedFile(File espdFile) throws IOException {
        if (AppConfig.getInstance().isArtefactDumpingEnabled()) {
            Files.createDirectories(Paths.get(AppConfig.getInstance().dumpIncomingArtefactsLocation() + "/xml/"));
            File dumpedFile;
            try {
                dumpedFile = new File(AppConfig.getInstance().dumpIncomingArtefactsLocation()
                        + "/xml/" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss")) + ".xml");
                Files.copy(espdFile.toPath(), dumpedFile.toPath());
            } catch (FileAlreadyExistsException e) {
                dumpedFile = new File(AppConfig.getInstance().dumpIncomingArtefactsLocation()
                        + "/xml/" + ZonedDateTime.now().format(DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss-"))
                        + RandomStringUtils.randomAlphabetic(3) + ".xml");
                Files.copy(espdFile.toPath(), dumpedFile.toPath());
            }
            LOGGER.info("Dumping imported xml artefact to " + dumpedFile.getAbsolutePath());
        }
    }
}
