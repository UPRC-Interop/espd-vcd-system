package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ESPDService;
import eu.esens.espdvcd.designer.typeEnum.ArtefactType;
import eu.esens.espdvcd.model.RegulatedESPDRequest;
import eu.esens.espdvcd.model.RegulatedESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import eu.esens.espdvcd.validator.ValidationResult;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ESPDEndpoint extends Endpoint {
    private final ESPDService service;
    private final ArtefactType artefactType;
    private final String DOCUMENT_ERROR = "Oops, the XML document provided was not valid and could not be converted to JSON. Did you provide the correct input? \nThis could help you:\n",
            LOGGER_DOCUMENT_ERROR = "Error occurred in ESPDEndpoint while converting an XML response to an object. ",
            DESERIALIZATION_ERROR = "Oops, the provided JSON document was not valid and could not be converted to an object. Did you provide the correct format? \nThis could help you:\n",
            LOGGER_DESERIALIZATION_ERROR = "Error occurred in ESPDEndpoint while converting a JSON object to XML. ";

    public ESPDEndpoint(ESPDService service) {
        this.service = service;
        artefactType = service.getArtefactType();

        SimpleModule desrModule = new SimpleModule();
        desrModule.addDeserializer(Requirement.class, new RequirementDeserialiser());
        MAPPER.registerModule(desrModule);
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                return "You need to POST an artefact in xml or json format.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST an artefact in xml or json format.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request rq, Response rsp) throws IOException, ServletException, JAXBException, SAXException {
        if (rq.contentType().contains("application/json")) {
            LOGGER.info(rq.body());
            Object document = null;
            try {
                switch (artefactType) {
                    case REQUEST:
                        document = MAPPER.readValue(rq.body(), RegulatedESPDRequest.class);
                        break;
                    case RESPONSE:
                        document = MAPPER.readValue(rq.body(), RegulatedESPDResponse.class);
                        break;
                }
            } catch (IOException e) {
                rsp.status(400);
                LOGGER.severe(LOGGER_DESERIALIZATION_ERROR + e.getMessage());
                return DESERIALIZATION_ERROR + e.getMessage();
            }
            rsp.header("Content-Type", "application/octet-stream");
            switch (artefactType) {
                case REQUEST:
                    rsp.header("Content-Disposition", "attachment; filename=espd-request.xml;");
                    break;
                case RESPONSE:
                    rsp.header("Content-Disposition", "attachment; filename=espd-response.xml;");
                    break;
            }
            return service.ObjectToXMLStreamTransformer(document);
        } else if (rq.contentType().contains("multipart/form-data")) {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Collection<Part> parts = rq.raw().getParts();
            if (parts.iterator().hasNext()) {
                Part part = parts.iterator().next();
                String tempFileName = UUID.randomUUID().toString();
                try (InputStream input = part.getInputStream()) {
                    Files.copy(input, Paths.get(tempFileName), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                    rsp.status(500);
                    return "Unable to save uploaded file. More info:\n" + e.getMessage();
                }
                try {
                    rsp.header("Content-Type", "application/json");
                    return WRITER.writeValueAsString(service.XMLFileToObjectTransformer(new File(tempFileName)));
                } catch (RetrieverException | BuilderException | NullPointerException e) {
                    LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                    rsp.status(406);
                    return DOCUMENT_ERROR + e.getMessage() + "\n" + "";
                } catch (ValidationException e) {
                    LOGGER.severe(e.getMessage());
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
                Files.write(Paths.get(path), rq.body().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                File tempFile = new File(path);
                String returnValue = WRITER.writeValueAsString(service.XMLFileToObjectTransformer(tempFile));
                Files.deleteIfExists(Paths.get(path));
                return returnValue;
            } catch (RetrieverException | BuilderException | UnmarshalException e) {
                LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                rsp.status(406);
                return DOCUMENT_ERROR + e.getMessage();
            } catch (ValidationException e) {
                LOGGER.severe(e.getMessage());
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

    private String ListPrinter(List<ValidationResult> theList) {
        StringBuilder stringifiedList = new StringBuilder().append("\n");
        for (ValidationResult res : theList) {
            stringifiedList.append(res.toString()).append("\n");
        }
        return stringifiedList.toString();
    }
}
