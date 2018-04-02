package eu.esens.espdvcd.designer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.models.CodelistItem;
import eu.esens.espdvcd.designer.routes.CriteriaRoute;
import eu.esens.espdvcd.model.*;
import eu.esens.espdvcd.model.requirement.RequestRequirement;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.retriever.criteria.CriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.ECertisCriteriaExtractor;
import eu.esens.espdvcd.retriever.criteria.PredefinedESPDCriteriaExtractor;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static spark.Spark.*;

public final class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final static ObjectWriter WRITER = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    private final static ObjectMapper REQ_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule()),
            RESP_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
    private final static PredefinedESPDCriteriaExtractor PREDEFINED_CRITERIA_EXTRACTOR = new PredefinedESPDCriteriaExtractor();
    private final static ECertisCriteriaExtractor E_CERTIS_CRITERIA_EXTRACTOR = new ECertisCriteriaExtractor();

    private final static Map<String, List<CodelistItem>> CODELISTS_MAP_V2 = new LinkedHashMap<>(),
            CODELISTS_MAP_V1 = new LinkedHashMap<>();
    private static final String EXCLUSION_REGEXP = "^CRITERION.EXCLUSION.+";
    private static final String EXCLUSION_CONVICTION_REGEXP = "^CRITERION.EXCLUSION.CONVICTIONS.+";
    private static final String EXCLUSION_CONTRIBUTION_REGEXP = "^CRITERION.EXCLUSION.CONTRIBUTIONS.+";
    private static final String EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP = "(^CRITERION.EXCLUSION.SOCIAL.+)|" +
            "(^CRITERION.EXCLUSION.BUSINESS.+)|" +
            "(^CRITERION.EXCLUSION.MISCONDUCT.+)|" +
            "(^CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.+)";
    private static final String EXCLUSION_NATIONAL_REGEXP = "^CRITERION.EXCLUSION.NATIONAL.+";
    private static final String SELECTION_REGEXP = "^CRITERION.SELECTION.+";
    private static final String SELECTION_SUITABILITY_REGEXP = "^CRITERION.SELECTION.SUITABILITY.+";
    private static final String SELECTION_ECONOMIC_REGEXP = "^CRITERION.SELECTION.ECONOMIC_FINANCIAL_STANDING.+";
    private static final String SELECTION_TECHNICAL_REGEXP = "(?!.*CERTIFICATES*)^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.+";
    private static final String SELECTION_CERTIFICATES_REGEXP = "^CRITERION.SELECTION.TECHNICAL_PROFESSIONAL_ABILITY.CERTIFICATES.+";
    private static final String EO_RELATED_REGEXP = "(?!.*MEETS_THE_OBJECTIVE*)^CRITERION.OTHER.EO_DATA.+";
    private static final String REDUCTION_OF_CANDIDATES_REGEXP = "^CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE*";

    public static void main(String[] args) {

        //SERVER CONFIGURATION
        int portToBind = 0;
        if (args.length == 0)
            portToBind = 8080;
        else {
            try {
                portToBind = Integer.parseInt(args[0]);
                if ((portToBind < 0) || (portToBind > 65535)) {
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
        SimpleModule requestRequirementModule = new SimpleModule("RequestRequirementModule", Version.unknownVersion()),
                responseRequirementModule = new SimpleModule("ResponseRequirementModule", Version.unknownVersion());
        SimpleAbstractTypeResolver requestRequirementResolver = new SimpleAbstractTypeResolver().addMapping(Requirement.class, RequestRequirement.class),
                responseRequirementResolver = new SimpleAbstractTypeResolver().addMapping(Requirement.class, ResponseRequirement.class);
        requestRequirementModule.setAbstractTypes(requestRequirementResolver);
        responseRequirementModule.setAbstractTypes(responseRequirementResolver);

        REQ_MAPPER.registerModule(requestRequirementModule);
        RESP_MAPPER.registerModule(responseRequirementModule);

        //ROUTES
        path("/api", () -> {
            before("/*", (q, a) -> LOGGER.info("Received api call"));

            path("/codelists", () -> {
                get("/:version/:codelist", (request, response) ->
                {
                    final String version = request.params("version"),
                            codelistName = request.params("codelist");
                    if (version.equals("v2") | version.equals("V2")) {
                        String lang = request.queryParams("lang");
                        if (CODELISTS_MAP_V2.containsKey(codelistName + '.' + lang))
                            return WRITER.writeValueAsString(CODELISTS_MAP_V2.get(codelistName + '.' + lang));
                        else {
                            try {
                                Map<String, String> theCodelistMap = CodelistsV2.valueOf(codelistName).getDataMap(lang);
                                List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
                                theCodelistMap.forEach((key, value) -> {
                                    theCodelistList.add(new CodelistItem(key, value));
                                });
                                CODELISTS_MAP_V2.putIfAbsent(codelistName, theCodelistList);
                                return WRITER.writeValueAsString(theCodelistList);
                            } catch (IllegalArgumentException e) {
                                response.status(404);
                                return "CodeList not found";
                            }
                        }
                    } else if (version.equals("v1") | version.equals("V1")) {
                        if (CODELISTS_MAP_V1.containsKey(codelistName))
                            return WRITER.writeValueAsString(CODELISTS_MAP_V1.get(codelistName));
                        else {
                            try {
                                Map<String, String> theCodelistMap = CodelistsV1.valueOf(codelistName).getDataMap();
                                List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
                                theCodelistMap.forEach((key, value) -> {
                                    theCodelistList.add(new CodelistItem(key, value));
                                });
                                CODELISTS_MAP_V1.putIfAbsent(codelistName, theCodelistList);
                                return WRITER.writeValueAsString(theCodelistList);
                            } catch (IllegalArgumentException e) {
                                response.status(404);
                                return "CodeList not found";
                            }
                        }
                    } else {
                        response.status(400);
                        return "Invalid codelist version specified";
                    }
                });
            });

            path("/criteriaList", () -> {

//                get("/:criteriaType/:criteriaLanguage", (request, response) -> {
//                    final String criteriaType = request.params("criteriaType"),
//                            criteriaLanguage = request.params("criteriaLanguage");
//                });

                get("/ecertis/:lang", ((request, response) -> {
                    final String language = request.params("lang");
                    try {
                        E_CERTIS_CRITERIA_EXTRACTOR.setLang(language);
                    } catch (RetrieverException e) {
                        e.printStackTrace();
                        response.status(400);
                        return "Invalid langage specified";
                    }
                    return WRITER.writeValueAsString(E_CERTIS_CRITERIA_EXTRACTOR.getCriterion(E_CERTIS_CRITERIA_EXTRACTOR.getFullList().iterator().next().getID()));
                }));

                path("/predefined", () -> {
                    get("/", (request, response)
                            -> WRITER.writeValueAsString(criteriaRoute.getPredefinedCriteria()));
                    get("", (request, response)
                            -> WRITER.writeValueAsString(criteriaRoute.getPredefinedCriteria()));

                    path("/selection", () -> {
                        get("", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));
                        get("/", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));
                        get("/technical", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_TECHNICAL_REGEXP)));
                        get("/suitability", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_SUITABILITY_REGEXP)));
                        get("/quality", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_CERTIFICATES_REGEXP)));
                        get("/economic", (request, response) -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_ECONOMIC_REGEXP)));
                    });
                    path("/exclusion", () -> {
                        get("/", (request, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));
                        get("", (rsequest, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));
                        get("/contribution", (request, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_CONTRIBUTION_REGEXP)));
                        get("/conviction", (request, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_CONVICTION_REGEXP)));
                        get("/national", (request, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_NATIONAL_REGEXP)));
                        get("/insolvencyConflictsMisconduct", (request, response)
                                -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP)));
                    });
                    get("/selection", (request, response)
                            -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));

                    get("/exclusion", (request, response)
                            -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));

                    get("/eorelated", (request, response)
                            -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EO_RELATED_REGEXP)));

                    get("/reductionOfCandidates", (request, response)
                            -> WRITER.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), REDUCTION_OF_CANDIDATES_REGEXP)));
                });

            });

            path("/espd", () -> {
                path("/v1", () -> {

                    get("/request", (rq, rsp) -> {
                        ESPDRequest req = BuilderFactory.V1.getModelBuilder().createRegulatedESPDRequest();
                        return WRITER.writeValueAsString(req);
                    });

                    get("/response", (rq, rsp) -> {
                        ESPDResponse req = BuilderFactory.V1.getModelBuilder().createRegulatedESPDResponse();
                        return WRITER.writeValueAsString(req);
                    });

                    post("/request", (rq, rsp) -> {
                        if (rq.contentType().contains("application/json")) {
                            LOGGER.info(rq.body());
                            ESPDRequest espdRequest = REQ_MAPPER.readValue(rq.body(), RegulatedESPDRequest.class);
                            rsp.header("Content-Type", "application/octet-stream");
                            rsp.header("Content-Disposition", "attachment; filename=espd-request.xml;");
                            return BuilderFactory.V1.getDocumentBuilderFor(espdRequest).getAsInputStream();
                        } else if (rq.contentType().contains("multipart/form-data")) {
                            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
                            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                            Collection<Part> parts = rq.raw().getParts();
                            if (parts.iterator().hasNext()) {
                                Part part = parts.iterator().next();
                                LOGGER.info(part.getContentType());
                                LOGGER.info(new BufferedReader(new InputStreamReader(part.getInputStream())).lines().collect(Collectors.joining("\n")));
                                ESPDRequest req = BuilderFactory.V1.getModelBuilder().importFrom(part.getInputStream()).createRegulatedESPDRequest();
                                req.setCriterionList(PREDEFINED_CRITERIA_EXTRACTOR.getFullList(req.getFullCriterionList()));
                                return WRITER.writeValueAsString(req);
                            } else {
                                rsp.status(400);
                                return "Bad request";
                            }
                        } else if (rq.contentType().contains("application/xml")) {
                            ESPDRequest req = BuilderFactory.V1.getModelBuilder().importFrom(new ByteArrayInputStream(rq.bodyAsBytes())).createRegulatedESPDRequest();
                            req.setCriterionList(PREDEFINED_CRITERIA_EXTRACTOR.getFullList(req.getFullCriterionList()));
                            return WRITER.writeValueAsString(req);
                        } else {
                            LOGGER.warning("Invalid request.");
                            LOGGER.warning("Got unexpected content/type: " + rq.contentType());
                            LOGGER.warning("With body: " + rq.body());
                            rsp.status(400);
                            return "Bad request";
                        }
                    });

                    post("/response", (rq, rsp) -> {
                        if (rq.contentType().contains("application/json")) {
                            ESPDResponse espdResponse = RESP_MAPPER.readValue(rq.body(), RegulatedESPDResponse.class);
                            rsp.header("Content-Type", "application/octet-stream");
                            rsp.header("Content-Disposition", "attachment; filename=espd-response.xml;");
                            return BuilderFactory.V1.getDocumentBuilderFor(espdResponse).getAsInputStream();
                        } else if (rq.contentType().contains("multipart/form-data")) {
                            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
                            rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                            Collection<Part> parts = rq.raw().getParts();
                            if (parts.iterator().hasNext()) {
                                ESPDResponse resp = BuilderFactory.V1.getModelBuilder().importFrom(parts.iterator().next().getInputStream()).createRegulatedESPDResponse();
                                return WRITER.writeValueAsString(resp);
                            } else {
                                rsp.status(400);
                                return "Bad request";
                            }
                        } else if (rq.contentType().contains("application/xml")) {
                            ESPDResponse resp = BuilderFactory.V1.getModelBuilder().importFrom(new ByteArrayInputStream(rq.bodyAsBytes())).createRegulatedESPDResponse();
                            return WRITER.writeValueAsString(resp);
                        } else {
                            rsp.status(400);
                            return "Bad request";
                        }
                    });

                });

                path("/v2", () -> {
                    path("/regulated", () -> {
                        get("/request", (rq, rsp) -> {
                            ESPDRequest req = BuilderFactory.V2.getModelBuilder().createRegulatedESPDRequest();
                            return WRITER.writeValueAsString(req);
                        });

                        get("/response", (rq, rsp) -> {
                            ESPDResponse req = BuilderFactory.V2.getModelBuilder().createRegulatedESPDResponse();
                            return WRITER.writeValueAsString(req);
                        });

                        post("/request", (rq, rsp) -> {
                            throw new UnsupportedOperationException("Not yet implemented");
                        });

                        post("/response", (rq, rsp) -> {
                            throw new UnsupportedOperationException("Not yet implemented");
                        });
                    });
                    path("/selfContained", () -> {
                        get("/request", (rq, rsp) -> {
                            ESPDRequest req = BuilderFactory.V2.getModelBuilder().createSelfContainedESPDRequest();
                            return WRITER.writeValueAsString(req);
                        });

                        get("/response", (rq, rsp) -> {
                            ESPDResponse req = BuilderFactory.V2.getModelBuilder().createSelfContainedESPDResponse();
                            return WRITER.writeValueAsString(req);
                        });

                        post("/request", (rq, rsp) -> {
                            throw new UnsupportedOperationException("Not yet implemented");
                        });

                        post("/response", (rq, rsp) -> {
                            throw new UnsupportedOperationException("Not yet implemented");
                        });
                    });


                });

            });
        });
    }

    private static List<SelectableCriterion> filterCriteriaList(List<SelectableCriterion> criterionList, String filter) {
        List<SelectableCriterion> crList = criterionList.stream()
                .filter(cr -> cr.getTypeCode().matches(filter))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(crList);
    }

    // Enables CORS on requests.
    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null)
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null)
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }
}
