package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.endpoint.*;
import eu.esens.espdvcd.designer.service.*;
import eu.esens.espdvcd.schema.SchemaVersion;
import spark.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String args[]) {

        LOGGER.info("Starting up espd-designer server");

        //SERVER CONFIGURATION
        LOGGER.info("Starting port configuration");
        int portToBind = 0;
        if (args.length == 0) {
            LOGGER.warning("No port specified in the parameters, defaulting to 8080");
            portToBind = 8080;
        } else {
            try {
                portToBind = Integer.parseInt(args[0]);
                if ((portToBind < 0) || (portToBind > 65535)) {
                    LOGGER.severe("Invalid port specified");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid port argument");
                System.exit(1);
            }
        }

        LOGGER.info("Attempting to bind to port " + portToBind);
        Service spark = Service.ignite().port(portToBind);                                //SET THE SPARK SERVER ON FIRE
        spark.initExceptionHandler(e -> {
            LOGGER.severe("Failed to ignite the Spark server");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(2);
        });
        enableCORS(spark);

        LOGGER.info("Starting endpoint configuration");

        RestContext v2Context = new RestContext("/api/v2", spark),
                v1Context = new RestContext("/api/v1", spark),
                unversionedContext = new RestContext("/api", spark);

        try {
            LOGGER.info("Configuring codelistsV1 endpoint...");
            Endpoint v1Codelists = new CodelistsEndpoint(new CodelistsV1Service());
            v1Context.addEndpoint(v1Codelists);

            LOGGER.info("Configuring codelistsV2 endpoint...");
            Endpoint v2Codelists = new CodelistsEndpoint(new CodelistsV2Service());
            v2Context.addEndpoint(v2Codelists);

//            LOGGER.info("Configuring eCertisCriteria endpoint...");
//            Endpoint eCertisCriteriaEndpointV1 = new CriteriaEndpoint(new ECertisCriteriaService(SchemaVersion.V1));
//            Endpoint eCertisCriteriaEndpointV2 = new CriteriaEndpoint(new ECertisCriteriaService(SchemaVersion.V2));
//
//            v1Context.addEndpointWithPath(eCertisCriteriaEndpointV1, "/criteria/eCertis");
//            v2Context.addEndpointWithPath(eCertisCriteriaEndpointV2, "/criteria/eCertis");

            LOGGER.info("Configuring predefinedCriteria endpoint...");
            Endpoint predefCriteriaEndpointV1 = new CriteriaEndpoint(new PredefinedCriteriaService(SchemaVersion.V1));
            Endpoint predefCriteriaEndpointV2 = new CriteriaEndpoint(new PredefinedCriteriaService(SchemaVersion.V2));

            v1Context.addEndpointWithPath(predefCriteriaEndpointV1, "/criteria/predefined");
            v2Context.addEndpointWithPath(predefCriteriaEndpointV2, "/criteria/predefined");

            LOGGER.info("Configuring ExportESPDRequestV1 endpoint...");
            Endpoint ESPDRequestV1Endpoint = new ExportESPDv1Endpoint(new RegulatedModeltoESPDRequestV1Service());
            v1Context.addEndpointWithPath(ESPDRequestV1Endpoint, "/espd/request");

            LOGGER.info("Configuring ExportESPDResponseV1 endpoint...");
            Endpoint ESPDResponseV1Endpoint = new ExportESPDv1Endpoint(new RegulatedModeltoESPDResponseV1Service());
            v1Context.addEndpointWithPath(ESPDResponseV1Endpoint, "/espd/response");

            LOGGER.info("Configuring ExportESPDRequestV2 endpoint...");
            Endpoint ESPDRequestV2Endpoint = new ExportESPDv2Endpoint(new RegulatedModeltoESPDRequestV2Service());
            v2Context.addEndpointWithPath(ESPDRequestV2Endpoint, "/espd/request");

            LOGGER.info("Configuring ExportESPDResponseV2 endpoint...");
            Endpoint ESPDResponseV2Endpoint = new ExportESPDv2Endpoint(new RegulatedModeltoESPDResponseV2Service());
            v2Context.addEndpointWithPath(ESPDResponseV2Endpoint, "/espd/response");

            LOGGER.info("Configuring ImportESPDRequest endpoint...");
            Endpoint importESPDReq = new ImportESPDEndpoint(new ESPDRequestToModelService());
            unversionedContext.addEndpointWithPath(importESPDReq, "/importESPD/request");

            LOGGER.info("Configuring ImportESPDResponse endpoint...");
            Endpoint importESPDResp = new ImportESPDEndpoint(new ESPDResponseToModelService());
            unversionedContext.addEndpointWithPath(importESPDResp, "/importESPD/response");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize the endpoints with error " + e.getMessage());
            System.exit(3);
        }

        LOGGER.info("Server is up and running at port " + portToBind);
    }


    // Enables CORS on requests.
    private static void enableCORS(Service spark) {

        spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                if (accessControlRequestHeaders != null)
                    response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                if (accessControlRequestMethod != null)
                    response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Content-Disposition");
        });

        LOGGER.info("CORS support is now enabled. Enjoy");
    }

}
