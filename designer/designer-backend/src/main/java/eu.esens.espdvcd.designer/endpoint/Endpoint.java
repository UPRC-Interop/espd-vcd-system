package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import spark.Service;

import java.util.logging.Logger;

public abstract class Endpoint {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    protected final ObjectWriter WRITER = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    protected final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    abstract public void configure(Service spark, String basePath);
}
