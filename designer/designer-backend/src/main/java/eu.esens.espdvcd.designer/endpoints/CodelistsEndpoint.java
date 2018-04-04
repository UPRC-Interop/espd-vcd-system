package eu.esens.espdvcd.designer.endpoints;

import eu.esens.espdvcd.designer.services.CodelistsService;
import spark.Service;

public class CodelistsEndpoint extends Endpoint {

    private final CodelistsService service;
    private final String CODELIST_ERROR = "Codelist not found.";

    public CodelistsEndpoint(CodelistsService service){
        this.service = service;
    }

    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath+"/codelists", () -> {
            spark.get("/:codelist", ((request, response) -> {
                try{
                    response.header("Content-Type", "application/json");
                    return WRITER.writeValueAsString(service.getCodelist(request.params("codelist")));
                }catch (IllegalArgumentException e){
                    response.status(404);
                    return CODELIST_ERROR;
                }
            }));

            spark.get("/:codelist/:lang", (request, response) -> {
                try {
                    response.header("Content-Type", "application/json");
                    return WRITER.writeValueAsString(service.getTranslatedCodelist(request.params("codelist"), request.params("lang")));
                } catch (IllegalArgumentException e) {
                    response.status(404);
                    return CODELIST_ERROR;
                } catch (UnsupportedOperationException e){
                    response.status(400);
                    return "Translation not supported.";
                }
            });
        });
    }
}
