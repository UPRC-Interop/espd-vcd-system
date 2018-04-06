package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.endpoints.*;
import eu.esens.espdvcd.designer.services.*;
import spark.Service;

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
        Service spark = Service.ignite().port(portToBind); //SET THE SPARK SERVER ON FIRE
        enableCORS(spark);

        LOGGER.info("Starting endpoint configuration");

        RestContext v2Context = new RestContext("/api/v2", spark),
                v1Context = new RestContext("/api/v1", spark);

        try {
            LOGGER.info("Configuring codelistsV1 endpoint...");
            Endpoint v1Codelists = new CodelistsEndpoint(new CodelistsV1Service());
            v1Context.addEndpoint(v1Codelists);

            LOGGER.info("Configuring codelistsV2 endpoint...");
            Endpoint v2Codelists = new CodelistsEndpoint(new CodelistsV2Service());
            v2Context.addEndpoint(v2Codelists);

            LOGGER.info("Configuring eCertisCriteria endpoint...");
            Endpoint eCertisCriteriaEndpoint = new CriteriaEndpoint(new ECertisCriteriaService());
            v1Context.addEndpointWithPath(eCertisCriteriaEndpoint, "/criteria/eCertis");
            v2Context.addEndpointWithPath(eCertisCriteriaEndpoint, "/criteria/eCertis");

            LOGGER.info("Configuring predefinedCriteria endpoint...");
            Endpoint predefCriteriaEndpoint = new CriteriaEndpoint(new PredefinedCriteriaService());
            v1Context.addEndpointWithPath(predefCriteriaEndpoint, "/criteria/predefined");
            v2Context.addEndpointWithPath(predefCriteriaEndpoint, "/criteria/predefined");

            LOGGER.info("Configuring ESPDRequestV1 endpoint...");
            Endpoint ESPDRequestV1Endpoint = new ESPDRequestEndpoint(new RegulatedESPDRequestV1Service());
            v1Context.addEndpointWithPath(ESPDRequestV1Endpoint, "/espd/request");

            LOGGER.info("Configuring ESPDResponseV1 endpoint...");
            Endpoint ESPDResponseV1Endpoint = new ESPDResponseEndpoint(new RegulatedESPDResponseV1Service());
            v1Context.addEndpointWithPath(ESPDResponseV1Endpoint, "/espd/response");

            LOGGER.info("Configuring ESPDRequestV2 endpoint...");
            Endpoint ESPDRequestV2Endpoint = new ESPDRequestEndpoint(new RegulatedESPDRequestV2Service());
            v2Context.addEndpointWithPath(ESPDRequestV2Endpoint, "/espd/request");

            LOGGER.info("Configuring ESPDResponseV2 endpoint...");
            Endpoint ESPDResponseV2Endpoint = new ESPDResponseEndpoint(new RegulatedESPDResponseV2Service());
            v2Context.addEndpointWithPath(ESPDResponseV2Endpoint, "/espd/response");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize the endpoints with error " + e.getMessage());
            System.exit(1);
        }

        LOGGER.info("Server is up and running at port 8080");
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

        LOGGER.info("CORS support is now enabled. Enjoy.");
    }

}
