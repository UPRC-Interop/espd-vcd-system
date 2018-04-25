package eu.esens.espdvcd.designer.deserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.model.requirement.response.*;

import java.io.IOException;

public class RequirementDeserialiser extends StdDeserializer<ResponseRequirement> {
    public RequirementDeserialiser() {
        this(null);
    }

    public RequirementDeserialiser(Class<?> vc) {
        super(vc);
    }

    @Override
    public ResponseRequirement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = p.getCodec().readTree(p);
//        System.out.println(root.get("id").asText());
        JsonNode responseType = root.get("responseDataType");
        JsonNode ID = root.get("id");
        JsonNode description = root.get("description");

        ResponseRequirement responseRequirement = new ResponseRequirement(ID.asText(), ResponseTypeEnum.valueOf(responseType.asText()), description.asText());
        Response x;

        switch (responseRequirement.getResponseDataType()) {
            case CODE:
                x = mapper.treeToValue(root.get("response"), EvidenceURLCodeResponse.class);
                break;
            case AMOUNT:
                x = mapper.treeToValue(root.get("response"), AmountResponse.class);
                break;
            case QUANTITY:
                x = mapper.treeToValue(root.get("response"), QuantityResponse.class);
                break;
            case INDICATOR:
                x = mapper.treeToValue(root.get("response"), IndicatorResponse.class);
                break;
            case PERCENTAGE:
                x = mapper.treeToValue(root.get("response"), PercentageResponse.class);
                break;
            case DESCRIPTION:
                x = mapper.treeToValue(root.get("response"), DescriptionResponse.class);
                break;
            case CODE_COUNTRY:
                x = mapper.treeToValue(root.get("response"), CountryCodeResponse.class);
                break;
            case QUANTITY_YEAR:
                x = mapper.treeToValue(root.get("response"), QuantityYearResponse.class);
                break;
            case QUANTITY_INTEGER:
                x = mapper.treeToValue(root.get("response"), QuantityIntegerResponse.class);
                break;
            case PERIOD:
                x = mapper.treeToValue(root.get("response"), PeriodResponse.class);
                break;
            case EVIDENCE_URL:
                x = mapper.treeToValue(root.get("response"), EvidenceURLResponse.class);
                break;
            default:
                x = null;
                break;
        }

        responseRequirement.setResponse(x);
        return responseRequirement;
    }

}