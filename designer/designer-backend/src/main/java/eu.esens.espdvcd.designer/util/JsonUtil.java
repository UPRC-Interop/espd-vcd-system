package eu.esens.espdvcd.designer.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import spark.ResponseTransformer;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JsonUtil {

    private static final Logger LOGGER = Logger.getLogger(JsonUtil.class.getName());

    public static String toJson(Object object) throws JsonProcessingException {
        if (object instanceof String)
            return (String) object;
        else
            return new ObjectMapper()
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
                                        LOGGER.warning(String.format("Ignoring %s for field '%s' of %s instance", e.getClass().getName(), this.getName(), bean.getClass().getName()));
                                    }
                                }
                            }).collect(Collectors.toList());
                        }
                    }))
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .setDateFormat(new StdDateFormat())
                    .writer().withDefaultPrettyPrinter().writeValueAsString(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}
