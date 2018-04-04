package eu.esens.espdvcd.designer.endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.esens.espdvcd.builder.exception.BuilderException;
import eu.esens.espdvcd.designer.services.ESPDResponseService;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.RegulatedESPDResponse;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import spark.Service;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class ESPDResponseEndpoint extends Endpoint {
    private final ESPDResponseService service;
    private final String INTERNAL_SERVER_ERROR = "Oops, an internal server error occurred. (Did you provide the correct input?)";
    public ESPDResponseEndpoint(ESPDResponseService service) {
        this.service = service;

        SimpleModule requirementModule = new SimpleModule("RequirementModule", Version.unknownVersion());
        SimpleAbstractTypeResolver responseRequirementResolver = new SimpleAbstractTypeResolver()
                .addMapping(Requirement.class, ResponseRequirement.class);
        requirementModule.setAbstractTypes(responseRequirementResolver);
        MAPPER.registerModule(requirementModule);
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("/*", ((request, response) -> {
                response.status(405);
                return "You need to POST a document in xml or json format.";
            }));

            spark.post("/*", (rq, rsp) -> {
                if (rq.contentType().contains("application/json")) {
                    ESPDResponse espdResponse = null;
                    try {
                        espdResponse = MAPPER.readValue(rq.body(), RegulatedESPDResponse.class);
                    } catch (IOException e) {
                        rsp.status(500);
                        LOGGER.severe("Error occured in ESPDResponseEndpoint while converting a JSON object to an XML. "+e.getMessage());
                        return INTERNAL_SERVER_ERROR+ " More info: " + e.getMessage();
                    }
                    rsp.header("Content-Type", "application/octet-stream");
                    rsp.header("Content-Disposition", "attachment; filename=espd-response.xml;");
                    return service.ResponseToXMLStreamTransformer(espdResponse);
                } else if (rq.contentType().contains("multipart/form-data")) {
                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
                    rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                    Collection<Part> parts = rq.raw().getParts();
                    if (parts.iterator().hasNext()) {
                        Part part = parts.iterator().next();
                        try {
                            rsp.header("Content-Type", "application/json");
                            return WRITER.writeValueAsString(service.XMLStreamToResponseParser(part.getInputStream()));
                        } catch (RetrieverException | BuilderException | NullPointerException e) {
                            LOGGER.severe("Error occured in ESPDResponseEndpoint while converting an XML response to an object.");
                            rsp.status(500);
                            return INTERNAL_SERVER_ERROR;
                        }
                    } else {
                        rsp.status(400);
                        return "Bad request.";
                    }
                } else if (rq.contentType().contains("application/xml")) {
                    try {
                        rsp.header("Content-Type", "application/json");
                        return WRITER.writeValueAsString(service.XMLStringToResponseParser(rq.body()));
                    } catch (RetrieverException | BuilderException | NullPointerException e) {
                        LOGGER.severe("Error occured in ESPDResponseEndpoint while converting an XML response to an object.");
                        rsp.status(500);
                        return INTERNAL_SERVER_ERROR;
                    }
                } else {
                    LOGGER.severe("Got unexpected content-type: " + rq.contentType());
                    rsp.status(400);
                    return "Bad content-type specified.";
                }
            });

        });
    }
}
