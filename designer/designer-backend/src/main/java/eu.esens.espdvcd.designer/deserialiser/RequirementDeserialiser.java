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
    public ResponseRequirement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = p.getCodec().readTree(p);
        JsonNode responseType = root.get("responseDataType");
        JsonNode ID = root.get("id");
        JsonNode description = root.get("description");

        ResponseRequirement responseRequirement = new ResponseRequirement(ID.asText(), ResponseTypeEnum.valueOf(responseType.asText()), description.asText());
        Response response;

        switch (responseRequirement.getResponseDataType()) {
            case CODE:
                response = mapper.treeToValue(root.get("response"), EvidenceURLCodeResponse.class);
                break;
            case AMOUNT:
                response = mapper.treeToValue(root.get("response"), AmountResponse.class);
                break;
            case QUANTITY:
                response = mapper.treeToValue(root.get("response"), QuantityResponse.class);
                break;
            case INDICATOR:
                response = mapper.treeToValue(root.get("response"), IndicatorResponse.class);
                break;
            case PERCENTAGE:
                response = mapper.treeToValue(root.get("response"), PercentageResponse.class);
                break;
            case DESCRIPTION:
                response = mapper.treeToValue(root.get("response"), DescriptionResponse.class);
                break;
            case CODE_COUNTRY:
                response = mapper.treeToValue(root.get("response"), CountryCodeResponse.class);
                break;
            case QUANTITY_YEAR:
                response = mapper.treeToValue(root.get("response"), QuantityYearResponse.class);
                break;
            case QUANTITY_INTEGER:
                response = mapper.treeToValue(root.get("response"), QuantityIntegerResponse.class);
                break;
            case PERIOD:
                response = mapper.treeToValue(root.get("response"), PeriodResponse.class);
                break;
            case EVIDENCE_URL:
                response = mapper.treeToValue(root.get("response"), EvidenceURLResponse.class);
                break;
            default:
                response = null;
                break;
        }
        responseRequirement.setResponse(response);
        return responseRequirement;
    }

}