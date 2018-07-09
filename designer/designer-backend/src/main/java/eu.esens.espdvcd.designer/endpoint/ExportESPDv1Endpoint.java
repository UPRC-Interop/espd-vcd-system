package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.designer.service.ModeltoESPDService;
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

public class ExportESPDv1Endpoint extends Endpoint {
    private final ModeltoESPDService service;
    private final ArtefactType artefactType;
    private final String DESERIALIZATION_ERROR = "Oops, the provided JSON document was not valid and could not be converted to an object. Did you provide the correct format? \nThis could help you:\n",
            LOGGER_DESERIALIZATION_ERROR = "Error occurred in ESPDEndpoint while converting a JSON object to XML. ";

    public ExportESPDv1Endpoint(ModeltoESPDService service) {
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
                return "You need to POST an artefact in json format.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST an artefact in json format.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request rq, Response rsp) throws IOException, ServletException, JAXBException, SAXException {
        if (rq.contentType().contains("application/json")) {
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
            try {
                return service.CreateXMLStreamFromModel(document);
            }catch (ValidationException e){
                LOGGER.severe(e.getMessage());
                rsp.status(406);
                return "Found null criterion. Please check your input. Aborting export...";
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            return "Unacceptable content-type specified.";
        }
    }


}
