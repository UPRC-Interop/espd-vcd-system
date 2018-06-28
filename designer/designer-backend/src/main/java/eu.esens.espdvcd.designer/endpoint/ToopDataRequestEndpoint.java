package eu.esens.espdvcd.designer.endpoint;

import eu.esens.espdvcd.designer.service.ToopService;
import spark.Request;
import spark.Response;
import spark.Service;

public class ToopDataRequestEndpoint extends Endpoint {

    ToopService service;

    public ToopDataRequestEndpoint(ToopService service){
        this.service = service;
    }
    @Override
    public void configure(Service spark, String basePath) {
        spark.path(basePath, () -> {
            spark.get("", ((request, response) -> {
                response.status(405);
                return "You need to POST a Toop data request.";
            }));

            spark.get("/", ((request, response) -> {
                response.status(405);
                return "You need to POST a Toop data request.";
            }));

            spark.post("", this::postRequest);

            spark.post("/", this::postRequest);

        });
    }

    private Object postRequest(Request request, Response response) {
        //TODO PROCESS UI REQUEST
    }
}