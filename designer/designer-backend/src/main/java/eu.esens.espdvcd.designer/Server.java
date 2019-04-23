/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
import eu.esens.espdvcd.designer.util.*;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import spark.Service;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final AppConfig CONFIG = AppConfig.getInstance();

    public static void main(String[] args) {

        LOGGER.info("Starting up espd-designer server");

        //SERVER CONFIGURATION
        LOGGER.info("Starting port configuration");
        int portToBind = CONFIG.getServerPort();
        String initialPath = "";

        if ((portToBind < 0) || (portToBind > 65535)) {
            LOGGER.severe("Invalid port specified");
            System.exit(1);
        }

        LOGGER.info("Attempting to bind to port " + portToBind);
        Service spark = Service.ignite().port(portToBind);
        spark.initExceptionHandler(e -> {
            LOGGER.severe("Failed to ignite the Spark server");
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            System.exit(-1);
        });

        ServerUtil.configureStaticFiles(spark);

        if (CONFIG.isEnchancedSecurityEnabled())
            ServerUtil.addSecurityHeaders(spark);

        if (CONFIG.isCORSEnabled())
            ServerUtil.enableCORS(spark);

        ServerUtil.dropTrailingSlashes(spark);

        spark.notFound((request, response) -> {
            response.type("application/json");
            return JsonUtil.toJson(Errors.notFoundError("Endpoint not found."));
        });

        spark.internalServerError((request, response) -> {
            response.type("application/json");
            return JsonUtil.toJson(Errors.standardError(
                    500,
                    "An internal error has occured. Please check your inputs. If this keeps happening, contact the server administrator."));
        });

        LOGGER.info("Starting endpoint configuration");

        RestContext v2RegulatedContext = new RestContext(initialPath + "/api/v2/regulated", spark);
        RestContext v2SelfContainedContext = new RestContext(initialPath + "/api/v2/selfcontained", spark);
        RestContext v1Context = new RestContext(initialPath + "/api/v1", spark);
        RestContext v1RegulatedContext = new RestContext(initialPath + "/api/v1/regulated", spark);

        RestContext v2Context = new RestContext(initialPath + "/api/v2", spark);
        RestContext baseContext = new RestContext(initialPath + "/api", spark);

        LOGGER.info("Configuring PlatformInfo endpoint...");
        Endpoint platformInfoEndpoint = new PlatformInfoEndpoint();
        baseContext.addEndpointWithPath(platformInfoEndpoint, "/platform-info");

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


        /*
         * MINI CLI
         */
        Scanner sc = new Scanner(System.in);
        if (AppConfig.getInstance().isDebugEnabled() && sc.hasNext()) {
            System.out.println("Debug menu has been enabled, type help for available commands.");
            while (true) {
                String input = sc.next();
                CLIOption option;
                try {
                    option = CLIOption.valueOf(input.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("------------------");
                    System.out.println("Invalid option " + input);
                    System.out.println("------------------");
                    continue;
                }
                switch (option) {
                    case Q:
                    case QUIT:
                    case EXIT:
                        spark.stop();
                        System.exit(0);
                    case STATUS:
                        System.out.println("------------------");
                        System.out.println("Server active thread count: " + spark.activeThreadCount());
                        System.out.println("Server port: " + AppConfig.getInstance().getServerPort());
                        System.out.println("Max upload file size: " + AppConfig.getInstance().getMaxFileSize() + "M");
                        System.out.println("Artefact dumping enabled: " + AppConfig.getInstance().isArtefactDumpingEnabled());
                        System.out.println("CORS enabled: " + AppConfig.getInstance().isCORSEnabled());
                        System.out.println("Artefact dumping location: " + Paths.get(
                                AppConfig.getInstance().dumpIncomingArtefactsLocation()).toAbsolutePath().toString());
                        System.out.println("Enchanced security enabled: "+ AppConfig.getInstance().isEnchancedSecurityEnabled());
                        System.out.println("Validation enabled: " +AppConfig.getInstance().isValidatorEnabled());
                        System.out.println("------------------");
                        break;
                    case ABOUT:
                        System.out.println("------------------");
                        System.out.println("Application info ");
                        System.out.println("Name:  "+AppInfo.getInstance().getAppName());
                        System.out.println("Version: "+AppInfo.getInstance().getAppVersion());
                        System.out.println("Revision: "+AppInfo.getInstance().getAppRevision());
                        System.out.println("Build time: "+AppInfo.getInstance().getBuildTime());
                        System.out.println("------------------");
                        break;
                    case HELP:
                        System.out.println("------------------");
                        System.out.println("Available commands are: " + Arrays.toString(CLIOption.values()));
                        System.out.println("------------------");
                        break;
                }
            }
        }else{
            sc.close();
        }
    }


}
