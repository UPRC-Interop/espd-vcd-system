package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import spark.Service;

import java.util.TimeZone;
import java.util.logging.Logger;

public abstract class Endpoint {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
//  UNCOMMENT TO RETURN ISO-8601 DATES
    protected final ObjectWriter WRITER = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).setDateFormat(new StdDateFormat()).writer().withDefaultPrettyPrinter();
    protected final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    abstract public void configure(Service spark, String basePath);
}
