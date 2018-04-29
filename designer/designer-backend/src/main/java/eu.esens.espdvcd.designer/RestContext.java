package eu.esens.espdvcd.designer;

import eu.esens.espdvcd.designer.endpoint.Endpoint;
import spark.Service;

import java.util.logging.Logger;

public class RestContext {

    private static final Logger logger = Logger.getLogger(RestContext.class.getName());

    private final Service spark;

    private final String basePath;

    public RestContext(String basePath, Service spark) {
        this.basePath = basePath;
        this.spark = spark;
    }

    public void addEndpoint(Endpoint endpoint) {

        endpoint.configure(spark, basePath);
//        logger.info("REST endpoints registered for "+ endpoint.getClass().getSimpleName());
    }

    public void addEndpointWithPath(Endpoint endpoint, String path) {

        endpoint.configure(spark, basePath + path);
//        logger.info("REST endpoints registered for "+ endpoint.getClass().getSimpleName());
    }

}
