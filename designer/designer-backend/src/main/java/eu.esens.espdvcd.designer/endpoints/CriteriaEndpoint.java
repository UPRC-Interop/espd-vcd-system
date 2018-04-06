package eu.esens.espdvcd.designer.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.esens.espdvcd.designer.services.CriteriaService;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.logging.Logger;

public class CriteriaEndpoint extends Endpoint {

    private final CriteriaService service;
    private final String CRITERIA_ERROR = "Criteria requested not found",
            TRANSLATION_ERROR = "Translation not supported",
            RETRIEVER_ERROR = "Failed to get criteria with error: ";

    public CriteriaEndpoint(CriteriaService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", (request, response) -> {
                String lang = request.queryParams("lang");
                return getNoFilter(response, lang);
            });

            spark.get("/", (request, response) -> {
                String lang = request.queryParams("lang");
                return getNoFilter(response, lang);
            });

            spark.get("/:firstFilter", (request, response) -> {
                String lang = request.queryParams("lang");
                return getSingleFilter(request, response, lang);
            });

            spark.get("/:firstFilter/", (request, response) -> {
                String lang = request.queryParams("lang");
                return getSingleFilter(request, response, lang);
            });
        });

    }

    private Object getNoFilter(Response response, String lang) throws JsonProcessingException {
        if (lang == null) {
            try {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getCriteria());
            } catch (RetrieverException e) {
                response.status(500);
                LOGGER.severe(e.getMessage());
                return RETRIEVER_ERROR + e.getMessage();
            }
        } else {
            try {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getTranslatedCriteria(lang));
            } catch (UnsupportedOperationException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return TRANSLATION_ERROR;
            } catch (RetrieverException e) {
                response.status(500);
                LOGGER.severe(e.getMessage());
                return RETRIEVER_ERROR + e.getMessage();
            }
        }
    }

    private Object getSingleFilter(Request request, Response response, String lang) throws JsonProcessingException {
        if (lang == null) {
            try {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getFilteredCriteriaList(request.params("firstFilter").toUpperCase()));
            } catch (IllegalArgumentException e) {
                response.status(404);
                LOGGER.warning(e.getMessage());
                return CRITERIA_ERROR + e.getMessage();
            } catch (RetrieverException e) {
                response.status(500);
                LOGGER.severe(e.getMessage());
                return RETRIEVER_ERROR + e.getMessage();
            }
        } else {
            try {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getFilteredTranslatedCriteriaList(request.params("firstFilter").toUpperCase(), lang));
            } catch (IllegalArgumentException e) {
                response.status(404);
                LOGGER.warning(e.getMessage());
                return CRITERIA_ERROR + e.getMessage();
            } catch (UnsupportedOperationException e) {
                response.status(406);
                LOGGER.warning(e.getMessage());
                return TRANSLATION_ERROR;
            } catch (RetrieverException e) {
                response.status(500);
                LOGGER.severe(e.getMessage());
                return RETRIEVER_ERROR + e.getMessage();
            }
        }
    }
}
