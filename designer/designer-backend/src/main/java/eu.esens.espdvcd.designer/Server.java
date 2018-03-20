package eu.esens.espdvcd.designer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import eu.esens.espdvcd.builder.BuilderFactory;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.routes.CriteriaRoute;
import eu.esens.espdvcd.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static spark.Spark.*;

public final class Server {

    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final static ObjectWriter ow = new ObjectMapper().findAndRegisterModules().writer().withDefaultPrettyPrinter();
    private final static ObjectMapper om = new ObjectMapper().findAndRegisterModules();

    private static final String EXCLUSION_REGEXP = "^CRITERION.EXCLUSION.+";
    private static final String EXCLUSION_CONVICTION_REGEXP = "^CRITERION.EXCLUSION.CONVICTIONS.+";
    private static final String EXCLUSION_CONTRIBUTION_REGEXP = "^CRITERION.EXCLUSION.CONTRIBUTIONS.+";
    private static final String EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP = "(^CRITERION.EXCLUSION.SOCIAL.+)|(^CRITERION.EXCLUSION.BUSINESS.+)|(^CRITERION.EXCLUSION.MISCONDUCT.+)|(^CRITERION.EXCLUSION.CONFLICT_OF_INTEREST.+)";
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

        //ROUTES
        path("/api", () -> {
            before("/*", (q, a) -> LOGGER.info("Received api call"));

            path("/codelists", () -> {
                path("/v2", () -> {
                    get("/languageCodeEU", (request, response) -> ow.writeValueAsString(CodelistsV2.LanguageCodeEU.getDataMap()));

                    get("/countryID", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.CountryIdentification.getDataMap(lang));
                    });

                    get("/procedureType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ProcedureType.getDataMap(lang));
                    });

                    get("/projectType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ProjectType.getDataMap(lang));
                    });

                    get("/bidType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.BidType.getDataMap(lang));
                    });

                    get("/confidentialityLevel", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ConfidentialityLevel.getDataMap(lang));
                    });

                    get("/criterionElementType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.CriterionElementType.getDataMap(lang));
                    });

                    get("/currency", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.Currency.getDataMap(lang));
                    });

                    get("/documentReferenceContentType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.DocumentReferenceContentType.getDataMap(lang));
                    });

                    get("/EOIDType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.EOIDType.getDataMap(lang));
                    });

                    get("/EOIndustryClassification", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.EOIndustryClassification.getDataMap(lang));
                    });

                    get("/EORoleType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.EORoleType.getDataMap(lang));
                    });

                    get("/evaluationMethodType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.EvaluationMethodType.getDataMap(lang));
                    });

                    get("/financialRatioType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.FinancialRatioType.getDataMap(lang));
                    });

                    get("/legislationType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.LegislationType.getDataMap(lang));
                    });

                    get("/periodMeasureType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.PeriodMeasureType.getDataMap(lang));
                    });

                    get("/profileExecutionID", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ProfileExecutionID.getDataMap(lang));
                    });

                    get("/propertyGroupType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.PropertyGroupType.getDataMap(lang));
                    });

                    get("/qualificationApplicationType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.QualificationApplicationType.getDataMap(lang));
                    });

                    get("/responseDataType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ResponseDataType.getDataMap(lang));
                    });

                    get("/servicesProjectSubType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.ServicesProjectSubType.getDataMap(lang));
                    });

                    get("/technicalCapabilityType", (request, response) -> {
                        String lang = request.queryParams("lang");
                        return ow.writeValueAsString(CodelistsV2.TechnicalCapabilityType.getDataMap(lang));
                    });
                });

                path("/v1", () -> {
                    get("/languageCodeEU", (request, response) -> ow.writeValueAsString(CodelistsV1.LanguageCodeEU.getDataMap()));

                    get("/countryID", (request, response) -> ow.writeValueAsString(CodelistsV1.CountryIdentification.getDataMap()));

                    get("/procedureType", (request, response) -> ow.writeValueAsString(CodelistsV1.ProcedureType.getDataMap()));

                    get("/projectType", (request, response) -> ow.writeValueAsString(CodelistsV1.ProjectType.getDataMap()));

                    get("/currency", (request, response) -> ow.writeValueAsString(CodelistsV1.Currency.getDataMap()));

                    get("/documentReferenceContentType", (request, response) -> ow.writeValueAsString(CodelistsV1.DocumentReferenceContentType.getDataMap()));

                    get("/periodMeasureType", (request, response) -> ow.writeValueAsString(CodelistsV1.PeriodMeasureType.getDataMap()));

                    get("/profileExecutionID", (request, response) -> ow.writeValueAsString(CodelistsV1.ProfileExecutionID.getDataMap()));

                    get("/responseDataType", (request, response) -> ow.writeValueAsString(CodelistsV1.ResponseDataType.getDataMap()));

                    get("/servicesProjectSubType", (request, response) -> ow.writeValueAsString(CodelistsV1.ServicesProjectSubType.getDataMap()));

                    get("/technicalCapabilityType", (request, response) -> ow.writeValueAsString(CodelistsV1.TechnicalCapabilityType.getDataMap()));
                });
            });

            path("/criteriaList", () -> {
                get("/ecertis", ((request, response) -> ow.writeValueAsString(criteriaRoute.getECertisCriteria())));

                path("/predefined", () -> {
                    get("/", (request, response)
                            -> ow.writeValueAsString(criteriaRoute.getPredefinedCriteria()));
                    get("", (request, response)
                            -> ow.writeValueAsString(criteriaRoute.getPredefinedCriteria()));

                    path("/selection", () -> {
                        get("", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));
                        get("/", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));
                        get("/technical", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_TECHNICAL_REGEXP)));
                        get("/suitability", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_SUITABILITY_REGEXP)));
                        get("/quality", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_CERTIFICATES_REGEXP)));
                        get("/economic", (request, response) -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_ECONOMIC_REGEXP)));
                    });
                    path("/exclusion", () -> {
                        get("/", (request, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));
                        get("", (rsequest, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));
                        get("/contribution", (request, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_CONTRIBUTION_REGEXP)));
                        get("/conviction", (request, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_CONVICTION_REGEXP)));
                        get("/national", (request, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_NATIONAL_REGEXP)));
                        get("/insolvencyConflictsMisconduct", (request, response)
                                -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_SOCIAL_BUSINESS_MISCONDUCT_CONFLICT_REGEXP)));
                    });
                    get("/selection", (request, response)
                            -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), SELECTION_REGEXP)));

                    get("/exclusion", (request, response)
                            -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EXCLUSION_REGEXP)));

                    get("/eorelated", (request, response)
                            -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), EO_RELATED_REGEXP)));

                    get("/reductionOfCandidates", (request, response)
                            -> ow.writeValueAsString(filterCriteriaList(criteriaRoute.getPredefinedCriteria(), REDUCTION_OF_CANDIDATES_REGEXP)));
                });

            });

            path("/espd", () -> {
                path("/v1", () -> {

                    get("/request", (rq, rsp) -> {
                        ESPDRequest req = BuilderFactory.V1.getModelBuilder().createRegulatedESPDRequest();
                        return ow.writeValueAsString(req);
                    });

                    get("/response", (rq, rsp) -> {
                        ESPDResponse req = BuilderFactory.V1.getModelBuilder().createRegulatedESPDResponse();
                        return ow.writeValueAsString(req);
                    });

                    post("/request", (rq, rsp) -> {

                        switch (rq.contentType()) {
                            case "application/json":
                                ESPDRequest espdRequest = om.readValue(rq.body(), RegulatedESPDRequest.class);
                                return BuilderFactory.V1.getDocumentBuilderFor(espdRequest).getAsInputStream();
                            case "text/xml":
                            case "application/xml":
//                        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
//                        rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
//                        Collection<Part> parts = rq.raw().getParts();
//                        ESPDRequest req = BuilderFactory.V1.getModelBuilder().importFrom(parts.iterator().next().getInputStream()).createRegulatedESPDRequest();
                                ESPDRequest req = BuilderFactory.V1.getModelBuilder().importFrom(new ByteArrayInputStream(rq.bodyAsBytes())).createRegulatedESPDRequest();
                                return ow.writeValueAsString(req);
                            default:
                                rsp.status(400);
                                return "Invalid request";
                        }

                    });

                    post("/response", (rq, rsp) -> {
//                    MultipartConfigElement multipartConfigElement = new MultipartConfigElement("file-uploads", 1000000000, 1000000000, 100);
//                    rq.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
//                    Collection<Part> parts = rq.raw().getParts();
//                    ESPDResponse resp = BuilderFactory.V1.getModelBuilder().importFrom(parts.iterator().next().getInputStream()).createRegulatedESPDResponse();
                        switch (rq.contentType()) {
                            case "application/json":
                                ESPDResponse espdResponse = om.readValue(rq.body(), RegulatedESPDResponse.class);
                                return BuilderFactory.V1.getDocumentBuilderFor(espdResponse).getAsInputStream();
                            case "text/xml":
                            case "application/xml":
                                ESPDResponse resp = BuilderFactory.V1.getModelBuilder().importFrom(new ByteArrayInputStream(rq.bodyAsBytes())).createRegulatedESPDResponse();
                                return ow.writeValueAsString(resp);
                            default:
                                rsp.status(400);
                                return "Invalid request";
                        }
                    });

                });

                path("/v2", () -> {
                    path("/regulated", () -> {
                        get("/request", (rq, rsp) -> {
                            ESPDRequest req = BuilderFactory.V2.getModelBuilder().createRegulatedESPDRequest();
                            return ow.writeValueAsString(req);
                        });

                        get("/response", (rq, rsp) -> {
                            ESPDResponse req = BuilderFactory.V2.getModelBuilder().createRegulatedESPDResponse();
                            return ow.writeValueAsString(req);
                        });

                        post("/request", (rq, rsp) -> {
                            throw new NotImplementedException();
                        });

                        post("/response", (rq, rsp) -> {
                            throw new NotImplementedException();
                        });
                    });
                    path("/selfContained", () -> {
                        get("/request", (rq, rsp) -> {
                            ESPDRequest req = BuilderFactory.V2.getModelBuilder().createSelfContainedESPDRequest();
                            return ow.writeValueAsString(req);
                        });

                        get("/response", (rq, rsp) -> {
                            ESPDResponse req = BuilderFactory.V2.getModelBuilder().createSelfContainedESPDResponse();
                            return ow.writeValueAsString(req);
                        });

                        post("/request", (rq, rsp) -> {
                            throw new NotImplementedException();
                        });

                        post("/response", (rq, rsp) -> {
                            throw new NotImplementedException();
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
