/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.endpoint.*;
import eu.esens.espdvcd.designer.service.*;
import eu.esens.espdvcd.designer.util.Errors;
import eu.esens.espdvcd.designer.util.JsonUtil;
import eu.esens.espdvcd.schema.EDMVersion;
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
        String initialPath = "";
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
//            if (args.length > 1) {
//                initialPath = args[1];
//            }
        }

        LOGGER.info("Attempting to bind to port " + portToBind);
        Service spark = Service.ignite().port(portToBind);
        spark.staticFiles.location("/public");

        spark.initExceptionHandler(e -> {
            LOGGER.severe("Failed to ignite the Spark server");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        });

        enableCORS(spark);

        dropTrailingSlashes(spark);

        spark.notFound((request, response) -> JsonUtil.toJson(Errors.notFoundError("Endpoint not found.")));

        spark.internalServerError((request, response) -> JsonUtil.toJson(Errors.standardError(
                500,
                "An internal error has occured. Please check your inputs. If this keeps happening, contact the server administrator.")));

        LOGGER.info("Starting endpoint configuration");

        RestContext v2RegulatedContext = new RestContext(initialPath + "/api/v2/regulated", spark);
        RestContext v2SelfContainedContext = new RestContext(initialPath + "/api/v2/selfcontained", spark);
        RestContext v1Context = new RestContext(initialPath + "/api/v1", spark);
        RestContext v1RegulatedContext = new RestContext(initialPath + "/api/v1/regulated", spark);

        RestContext v2Context = new RestContext(initialPath + "/api/v2", spark);
        RestContext baseContext = new RestContext(initialPath + "/api", spark);

        LOGGER.info("Configuring codelistsV1 endpoint...");
        Endpoint v1Codelists = new CodelistsEndpoint(CodelistsV1Service.getInstance());
        v1Context.addEndpoint(v1Codelists);

        LOGGER.info("Configuring codelistsV2 endpoint...");
        Endpoint v2Codelists = new CodelistsEndpoint(CodelistsV2Service.getInstance());
        v2Context.addEndpoint(v2Codelists);

        LOGGER.info("Configuring predefinedCriteria endpoint...");
        Endpoint predefCriteriaEndpointV1 = new CriteriaEndpoint(RegulatedCriteriaService.getV1Instance());
        Endpoint predefCriteriaEndpointV2 = new CriteriaEndpoint(RegulatedCriteriaService.getV2Instance());
        Endpoint predefCriteriaEndpointSelfContainedV2 = new CriteriaEndpoint(SelfContainedCriteriaService.getInstance());

        v1RegulatedContext.addEndpoint(predefCriteriaEndpointV1);
        v1Context.addEndpoint(predefCriteriaEndpointV1);
        v2RegulatedContext.addEndpoint(predefCriteriaEndpointV2);
        v2SelfContainedContext.addEndpoint(predefCriteriaEndpointSelfContainedV2);

        LOGGER.info("Configuring ExportESPDV1 endpoint...");
        Endpoint ESPDRequestV1Endpoint = new ExportESPDEndpoint(EDMVersion.V1);
        v1Context.addEndpoint(ESPDRequestV1Endpoint);

        LOGGER.info("Configuring ExportESPDV2 endpoint...");
        Endpoint ESPDRequestV2Endpoint = new ExportESPDEndpoint(EDMVersion.V2);
        v2Context.addEndpoint(ESPDRequestV2Endpoint);

        LOGGER.info("Configuring ImportESPDRequest endpoint...");
        Endpoint importESPDReq = new ImportESPDEndpoint(ImportESPDRequestService.getInstance());
        baseContext.addEndpointWithPath(importESPDReq, "/importESPD/request");

        LOGGER.info("Configuring ImportESPDResponse endpoint...");
        Endpoint importESPDResp = new ImportESPDEndpoint(ImportESPDResponseService.getInstance());
        baseContext.addEndpointWithPath(importESPDResp, "/importESPD/response");

        LOGGER.info("Server is up and running at port " + portToBind);
    }


    // Enables CORS on requests.
    private static void enableCORS(Service spark) {

        spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null)
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null)
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);

            return "OK";
        });

        spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Content-Disposition");
        });

        LOGGER.info("CORS support is now enabled. Enjoy ;)");
    }

    //Drop trailing slashes from URLs so that you don't have to specify the routes twice
    private static void dropTrailingSlashes(Service spark) {
        spark.before((req, res) -> {
            String path = req.pathInfo();
            if (path.endsWith("/"))
                res.redirect(path.substring(0, path.length() - 1), 301);
        });
    }
}
