package eu.esens.espdvcd.designer.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import spark.Service;

import java.util.logging.Logger;

public abstract class Endpoint {
    final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    final ObjectWriter WRITER = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    abstract public void configure(Service spark, String basePath);
}
