package eu.esens.espdvcd.model.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.retriever.ECertisLegislationReferenceImpl;
import java.io.IOException;

/**
 *
 * @author Konstantinos Raptis
 */
public class CustomLegislationReferenceDeserializer extends JsonDeserializer<LegislationReference> {

    @Override
    public ECertisLegislationReferenceImpl deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(p);
        ECertisLegislationReferenceImpl result = null; 
        for (JsonNode legislationReference : root) {
            ObjectMapper legislationReferenceMapper = new ObjectMapper();
            result = legislationReferenceMapper.treeToValue(legislationReference, ECertisLegislationReferenceImpl.class);
            break;
        }
        return result;
    }
    
}
