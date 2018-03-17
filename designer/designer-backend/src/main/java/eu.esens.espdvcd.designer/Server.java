package eu.esens.espdvcd.designer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.routes.CriteriaRoute;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import static spark.Spark.*;

public final class Server{

    final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    final static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();;

    public static void main(String[] args){

        //SERVER CONFIGURATION
        int portToBind=0;
        if (args.length == 0)
            portToBind = 8080;
        else {
            try {
                portToBind = Integer.parseInt(args[0]);
                if ((portToBind<0)||(portToBind>65535)){
                    System.err.println("Invalid port specified");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid port argument");
                System.exit(1);
            }
        }
        port(portToBind);
        enableCORS();

        //SET UP
        CriteriaRoute criteriaRoute = new CriteriaRoute();
        String path;

        //ROUTES

        //CODELIST
        path = "api/codelists";
        get(path+"/LanguageCode", (request,response) -> {
           return ow.writeValueAsString(CodelistsV2.LanguageCodeEU.getDataMap());
        });

        get(path+"/CountryID", (request, response) -> {
            String lang = request.queryParams("lang");
            return ow.writeValueAsString(CodelistsV2.CountryIdentification.getDataMap(lang));
        });

        get(path+"/ProcedureType", (request, response) -> {
            String lang = request.queryParams("lang");
            return ow.writeValueAsString(CodelistsV2.ProcedureType.getDataMap(lang));
        });

        get(path+"/ProjectType", (request, response) -> {
            String lang = request.queryParams("lang");
            return ow.writeValueAsString(CodelistsV2.ProjectType.getDataMap(lang));
        });

//        post(path+"/*", ((request, response) -> {2
//            response.status();
//        }));

        //CRITERIA
        path = "api/criteria";
        get(path+"/ECertis", ((request, response) -> {
            return ow.writeValueAsString(criteriaRoute.getECertisCriteria());
        }));

        get(path+"/Predefined", (request, response) -> {
            return ow.writeValueAsString(criteriaRoute.getPredefinedCriteria());
        });

//        get(path+"/PredefinedCriterionList",(request, response) -> {
//            String lang = request.queryParams("lang");
//            LOGGER.info("Language is "+lang);
//            return ow.writeValueAsString(predefinedESPDCriteriaExtractor.getFullList());
//        });

        get("/empty", (rq, rsp) -> {
            ESPDRequest req = BuilderFactory.getModelBuilder().createESPDRequest();
            return ow.writeValueAsString(req);
        });


        //DOCUMENTS
        path= "/api/document";
        post(path+"/request", "multipart/form-data", (rq, rsp) -> {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Collection<Part> parts = rq.raw().getParts();
            ESPDRequest req = BuilderFactory.getModelBuilder().importFrom(parts.iterator().next().getInputStream()).createESPDRequest();
            return ow.writeValueAsString(req);
        });

        post(path+"/response", "multipart/form-data", (rq, rsp) -> {
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

            Collection<Part> parts = rq.raw().getParts();
            ESPDResponse resp = BuilderFactory.getModelBuilder().importFrom(parts.iterator().next().getInputStream()).createESPDResponse();
            return ow.writeValueAsString(resp);
        });
    }

    // Enables CORS on requests.
    private static void enableCORS() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}
