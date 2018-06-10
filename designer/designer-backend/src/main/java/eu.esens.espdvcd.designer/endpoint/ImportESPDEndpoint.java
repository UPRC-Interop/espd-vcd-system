package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.builder.util.ArtefactUtils;
import eu.esens.espdvcd.codelist.enums.ProfileExecutionIDEnum;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.model.DocumentDetails;
import eu.esens.espdvcd.designer.service.ESPDtoModelService;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.schema.SchemaUtil;
import eu.esens.espdvcd.schema.SchemaVersion;
import org.xml.sax.SAXException;
import spark.Request;
import spark.Response;
import spark.Service;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.UUID;

public class ImportESPDEndpoint extends Endpoint {
    private final ESPDtoModelService service;
    private final String DOCUMENT_ERROR = "Oops, the XML document provided was not valid and could not be converted to JSON. Did you provide the correct input? \nThis could help you:\n",
            LOGGER_DOCUMENT_ERROR = "Error occurred in ESPDEndpoint while converting an XML response to an object. ";

    public ImportESPDEndpoint(ESPDtoModelService service) {
        this.service = service;

        SimpleModule desrModule = new SimpleModule();
        desrModule.addDeserializer(Requirement.class, new RequirementDeserialiser());
        MAPPER.registerModule(desrModule);
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                return "You need to POST an artefact in xml.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST an artefact in xml.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request rq, Response rsp) throws IOException, ServletException, JAXBException, SAXException {
        SchemaVersion artefactVersion = null;
        if (rq.contentType().contains("multipart/form-data")) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Collection<Part> parts = rq.raw().getParts();
            if (parts.iterator().hasNext()) {
                Part part = parts.iterator().next();
                String tempFileName = UUID.randomUUID().toString();
                try (InputStream input = part.getInputStream()) {
                    artefactVersion = ArtefactUtils.findSchemaVersion(input);
                    Files.copy(input, Paths.get(tempFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                    rsp.status(500);
                    return "Unable to save uploaded file. More info:\n" + e.getMessage();
                }
                try {
                    rsp.header("Content-Type", "application/json");
                    Object espd = service.CreateModelFromXML(new File (tempFileName));
                    DocumentDetails details = new DocumentDetails(artefactVersion.name(), "regulated");
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
                    return DOCUMENT_ERROR + e.getMessage() + "\n" + "";
                } catch (ValidationException e) {
                    LOGGER.severe(e.getMessage());
                    LOGGER.severe(ListPrinter(e.getResults()));
                    rsp.status(406);
                    return DOCUMENT_ERROR + e.getMessage() + ListPrinter(e.getResults());
                } finally {
                    Files.deleteIfExists(Paths.get(tempFileName));
                }
            } else {
                rsp.status(400);
                return "Bad request.";
            }
        } else if (rq.contentType().contains("application/xml")) {
            String path = UUID.randomUUID().toString();
            try {
                rsp.header("Content-Type", "application/json");
                byte[] xmlStream = rq.body().getBytes(StandardCharsets.UTF_8);
                artefactVersion = ArtefactUtils.findSchemaVersion(new ByteArrayInputStream(xmlStream));
                Files.write(Paths.get(path), xmlStream, StandardOpenOption.CREATE);
                File tempFile = new File(path);

                Object espd = service.CreateModelFromXML(tempFile);
                DocumentDetails details = new DocumentDetails(artefactVersion.name(), "regulated");
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
                return DOCUMENT_ERROR + e.getMessage();
            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
                LOGGER.severe(ListPrinter(e.getResults()));
                rsp.status(406);
                return DOCUMENT_ERROR + e.getMessage() + ListPrinter(e.getResults());
            } finally {
                Files.deleteIfExists(Paths.get(path));
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            return "Unacceptable content-type specified.";
        }
    }
}