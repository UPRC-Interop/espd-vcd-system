package eu.esens.espdvcd.designer.endpoint;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.designer.deserialiser.RequirementDeserialiser;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.validator.ValidationResult;
import spark.Service;

import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Endpoint {
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    protected final ObjectWriter WRITER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .registerModule(new SimpleModule().setSerializerModifier(new BeanSerializerModifier() {
                @Override
                public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                    return beanProperties.stream().map(bpw -> new BeanPropertyWriter(bpw) {
                        @Override
                        public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
                            try {
                                super.serializeAsField(bean, gen, prov);
                            } catch (Exception e) {
                                LOGGER.info(String.format("Ignoring %s for field '%s' of %s instance", e.getClass().getName(), this.getName(), bean.getClass().getName()));
                            }
                        }
                    }).collect(Collectors.toList());
                }
            }))
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new StdDateFormat())
            .writer().withDefaultPrettyPrinter();
    protected final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    abstract public void configure(Service spark, String basePath);

    protected String ListPrinter(List<ValidationResult> theList) {
        StringBuilder stringifiedList = new StringBuilder().append("\n");
        for (ValidationResult res : theList) {
            stringifiedList.append(res.toString()).append("\n");
        }
        return stringifiedList.toString();
    }
}
//
//    SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
//        resolver.addMapping(Requirement.class, ResponseRequirement.class);
//        SimpleModule serModule = new SimpleModule().addAbstractTypeMapping(Requirement.class, ResponseRequirement.class);
//
//        serModule.setAbstractTypes(resolver);