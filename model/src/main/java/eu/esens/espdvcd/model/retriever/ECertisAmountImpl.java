/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.retriever;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.esens.espdvcd.model.util.CustomStringValueDeserializer;

/**
 *
 * @author Konstantinos Raptis
 */
@JsonPropertyOrder(
        {
            "value", 
            "currencyID", 
            "currencyCodeListVersionID"
        })
public class ECertisAmountImpl implements ECertisAmount {
        
    private String value;
    private String currencyID;
    private String currencyCodeListVersionID;
    
    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    @Override
    @JsonProperty("currencyID")
    public String getCurrencyID() {
        return currencyID;
    }

    @Override
    @JsonDeserialize(using = CustomStringValueDeserializer.class)
    public void setCurrencyCodeListVersionID(String currencyCodeListVersionID) {
        this.currencyCodeListVersionID = currencyCodeListVersionID;
    }

    @Override
    @JsonProperty("currencyCodeListVersionID")
    public String getCurrencyCodeListVersionID() {
        return currencyCodeListVersionID;
    }
        
}
