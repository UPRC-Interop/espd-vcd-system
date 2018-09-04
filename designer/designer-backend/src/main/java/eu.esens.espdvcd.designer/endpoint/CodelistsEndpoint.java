package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.service.CodelistsService;
import eu.esens.espdvcd.designer.util.ErrorResponse;
import spark.Service;

public class CodelistsEndpoint extends Endpoint {

    private final CodelistsService service;
    private final String CODELIST_ERROR = "Codelist not found.";

    public CodelistsEndpoint(CodelistsService service) {
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath + "/codelists", () -> {
            spark.get("/:codelist", ((request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return WRITER.writeValueAsString(service.getCodelist(request.params("codelist")));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CODELIST_ERROR).build());
                }
            }));

            spark.get("/:codelist/:lang", (request, response) -> {
                response.header("Content-Type", "application/json");
                try {
                    return WRITER.writeValueAsString(service.getTranslatedCodelist(request.params("codelist"), request.params("lang")));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, CODELIST_ERROR).build());
                } catch (UnsupportedOperationException e) {
                    response.status(406);
                    return WRITER.writeValueAsString(new ErrorResponse.ErrorBuilder(404, "Translation is not supported for V1 Codelists.").build());
                }
            });

            spark.get("/", (request, response) -> {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getAvailableCodelists());
            });

            spark.get("", (request, response) -> {
                response.header("Content-Type", "application/json");
                return WRITER.writeValueAsString(service.getAvailableCodelists());
            });
        });
    }
}
