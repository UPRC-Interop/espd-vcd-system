/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.designer.deserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.codelist.enums.RequirementTypeEnum;
import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.requirement.ResponseRequirement;
import eu.esens.espdvcd.model.requirement.response.*;
import eu.esens.espdvcd.schema.EDMVersion;

import java.io.IOException;
import java.util.logging.Logger;

public class RequirementDeserialiser extends StdDeserializer<ResponseRequirement> {

    protected static final Logger LOGGER = Logger.getLogger(RequirementDeserialiser.class.getName());

    private EDMVersion version;

    public RequirementDeserialiser(EDMVersion version) {
        this((Class<?>) null);
        this.version = version;
    }

    public RequirementDeserialiser(Class<?> vc) {
        super(vc);
    }

    @Override
    public ResponseRequirement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JsonNode root = p.getCodec().readTree(p);
        JsonNode responseType = root.get("responseDataType");
        JsonNode type = root.get("type");
        JsonNode ID = root.get("id");
        JsonNode description = root.get("description");

        ResponseRequirement responseRequirement = new ResponseRequirement(ID.asText(), RequirementTypeEnum.valueOf(type.asText()), ResponseTypeEnum.valueOf(responseType.asText()), description.asText());
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
                if (version.equals(EDMVersion.V1))
                    response = mapper.treeToValue(root.get("response"), PeriodResponse.class);
                else
                    response = mapper.treeToValue(root.get("response"), ApplicablePeriodResponse.class);
                break;
            case EVIDENCE_IDENTIFIER:
                response = mapper.treeToValue(root.get("response"), EvidenceIdentifierResponse.class);
                break;
            case EVIDENCE_URL:
                response = mapper.treeToValue(root.get("response"), EvidenceURLResponse.class);
                break;
            case DATE:
                response = mapper.treeToValue(root.get("response"), DateResponse.class);
                break;
            case URL:
                response = mapper.treeToValue(root.get("response"), URLResponse.class);
                break;
            case IDENTIFIER:
                response = mapper.treeToValue(root.get("response"), IdentifierResponse.class);
                break;
            case ECONOMIC_OPERATOR_IDENTIFIER:
                response = mapper.treeToValue(root.get("response"), EOIdentifierResponse.class);
                break;
            case LOT_IDENTIFIER:
                response = mapper.treeToValue(root.get("response"), LotIdentifierResponse.class);
                break;
            case WEIGHT_INDICATOR:
                response = mapper.treeToValue(root.get("response"), WeightIndicatorResponse.class);
                break;
            case NONE:
                response = null;
                break;
            default:
                response = null;
                LOGGER.warning("RESPONSE TYPE NOT FOUND");
                LOGGER.warning(responseRequirement.getResponseDataType().name());
                break;
        }
        responseRequirement.setResponse(response);
        return responseRequirement;
    }

}