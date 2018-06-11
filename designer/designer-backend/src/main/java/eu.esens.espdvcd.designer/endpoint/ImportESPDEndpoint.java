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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 1000);
            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Collection<Part> parts = rq.raw().getParts();
            if (parts.iterator().hasNext()) {
                Part part = parts.iterator().next();
                File tempFile = File.createTempFile("espd-file", ".tmp");
                tempFile.deleteOnExit();

                try (InputStream input = part.getInputStream()) {
                    artefactVersion = ArtefactUtils.findSchemaVersion(input);
                    FileOutputStream out = new FileOutputStream(tempFile);
                    IOUtils.copy(input, out);
                    out.close();
                    out.flush();
                    out = null;
                    // System.gc();
//                    Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    LOGGER.severe(LOGGER_DOCUMENT_ERROR + e.getMessage());
                    rsp.status(500);
                    return "Unable to save uploaded file. More info:\n" + e.getMessage();
                }
                try {
                    rsp.header("Content-Type", "application/json");

                    File espdFile = tempFile;
                    Object espd = service.CreateModelFromXML(espdFile);

                    // espdFile.setWritable(true);

                    // if(espdFile.delete()){
                    //     System.out.println(espdFile.getName() + " is deleted!");
                    // }else{
                    // //     System.gc();

                    // // if(espdFile.delete()){
                    // //     System.out.println(espdFile.getName() + " is deleted! Attempt 2");
                    // // }else {
                    //     System.out.println("Delete operation failed.");
                    // // }
                    
                    // }

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
                    // Files.deleteIfExists(tempFile.toPath());
                }
            } else {
                rsp.status(400);
                return "Bad request.";
            }
        } else if (rq.contentType().contains("application/xml")) {
            Path tempFile = Files.createTempFile("espd-file", ".tmp");
            try {
                rsp.header("Content-Type", "application/json");
                byte[] xmlStream = rq.body().getBytes(StandardCharsets.UTF_8);
                artefactVersion = ArtefactUtils.findSchemaVersion(new ByteArrayInputStream(xmlStream));
                Files.write(tempFile, xmlStream, StandardOpenOption.CREATE);
                File espdFile = tempFile.toFile();

                Object espd = service.CreateModelFromXML(espdFile);
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
                Files.deleteIfExists(tempFile);
            }
        } else {
            LOGGER.severe("Got unexpected content-type: " + rq.contentType());
            rsp.status(406);
            return "Unacceptable content-type specified.";
        }
    }
}
