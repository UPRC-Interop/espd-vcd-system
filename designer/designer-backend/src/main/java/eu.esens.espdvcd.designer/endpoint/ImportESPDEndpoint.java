package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ImportESPDService;
import eu.esens.espdvcd.designer.util.DocumentDetails;
import eu.esens.espdvcd.designer.util.ErrorResponse;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.apache.poi.util.IOUtils;
import org.xml.sax.SAXException;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Objects;

public class ImportESPDEndpoint extends Endpoint {
    private final ImportESPDService service;
    private final String LOGGER_DOCUMENT_ERROR = "Error occurred in ESPDEndpoint while converting an XML response to an object. ";

    public ImportESPDEndpoint(ImportESPDService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(405, "You need to POST an artefact in XML.").build());
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(405, "You need to POST an artefact in XML.").build());
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request rq, Response rsp) throws IOException, ServletException, JAXBException, SAXException {
        rsp.header("Content-Type", "application/json");
        if (Objects.nonNull(rq.contentType())) {
            if (rq.contentType().contains("multipart/form-data")) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1024 * 1024 * 2, 1024 * 1024 * 3, 0);
                rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                Collection<Part> parts = rq.raw().getParts();
                if (parts.iterator().hasNext()) {
                    Part part = parts.iterator().next();
                    File tempFile = File.createTempFile("espd-file", ".tmp");
                    tempFile.deleteOnExit();

                    try (InputStream input = part.getInputStream()) {
                        FileOutputStream out = new FileOutputStream(tempFile);
                        IOUtils.copy(input, out);
                        out.close();
                        out.flush();
                    } catch (IOException e) {
                        LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                        rsp.status(500);
                        return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(500, "Unable to save uploaded file. More info:\n" + e.getMessage()).build());
                    }
                    try {
                        Object espd = service.importESPDFile(tempFile);
                        DocumentDetails details = service.getDocumentDetails(tempFile);
                        String serializedDetails = WRITER.writeValueAsString(details);
                        String serializedDocument = WRITER.writeValueAsString(espd);

                        StringBuilder builder = new StringBuilder(serializedDocument);
                        builder.setLength(builder.length() - 1);
                        builder.append(",");
                        builder.append("\"documentDetails\" : ");
                        builder.append(serializedDetails);
                        builder.append("}");

                        return builder.toString();
                    } catch (RetrieverException | BuilderException | NullPointerException e) {
                        LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                        rsp.status(406);
                        return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
                    } catch (ValidationException e) {
                        LOGGER.severe(e.getMessage());
                        rsp.status(406);
                        return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).withResponseObject(e.getResults()).build());

                    }
//                finally {
//                     Files.deleteIfExists(tempFile.toPath());
//                }
                } else {
                    rsp.status(400);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(400, "There was no file found in your upload, please check your input.").build());
                }
            } else if (rq.contentType().contains("application/xml")) {
                Path tempFile = Files.createTempFile("espd-file", ".tmp");
                try {
                    byte[] xmlStream = rq.body().getBytes(StandardCharsets.UTF_8);
                    Files.write(tempFile, xmlStream, StandardOpenOption.CREATE);
                    File espdFile = tempFile.toFile();

                    Object espd = service.importESPDFile(espdFile);
                    DocumentDetails details = service.getDocumentDetails(espdFile);
                    String serializedDetails = WRITER.writeValueAsString(details);
                    String serializedDocument = WRITER.writeValueAsString(espd);

                    StringBuilder builder = new StringBuilder(serializedDocument);
                    builder.setLength(builder.length() - 1);
                    builder.append(",");
                    builder.append("\"documentDetails\" : ");
                    builder.append(serializedDetails);
                    builder.append("}");

                    return builder.toString();
                } catch (RetrieverException | BuilderException | UnmarshalException e) {
                    LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                    rsp.status(406);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).build());
                } catch (ValidationException e) {
                    LOGGER.severe(e.getMessage());
                    rsp.status(406);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, e.getMessage()).withResponseObject(e.getResults()).build());
                } finally {
                    Files.deleteIfExists(tempFile);
                }
            } else {
                LOGGER.warning("Got unexpected content-type: " + rq.contentType());
                rsp.status(406);
                return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, "Unacceptable content-type specified.").build());
            }
        } else {
            LOGGER.warning("Got null content-type");
            rsp.status(406);
            return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(406, "Unacceptable content-type specified.").build());
        }
    }
}
