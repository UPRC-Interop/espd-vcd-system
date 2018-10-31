/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.model.requirement.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.esens.espdvcd.codelist.enums.EvaluationMethodTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Weight Indicator Response
 */

public class WeightIndicatorResponse extends Response implements Serializable {

    private static final long serialVersionUID = -3204508364109778286L;

    /**
     * Criterion response for weighting indicator
     * <p>
     * A special indicator that can also store the weighting information
     * for a criterion.
     * </p>
     * Data types:<br>
     * evaluationMethodType: Code<br>
     * evaluationMethodDescription: Text<br>
     * weight: Float<br>
     * indicator: Boolean<br>
     * Cardinality: 0..1<br>
     * UBL syntax paths:
     * evaluationMethodType: ccv:Criterion.EvaluationMethodTypeCode
     * evaluationMethodDescription ccv:Criterion.WeightingConsiderationDescription
     * weight ccv:Criterion.WeightNumeric
     */
    // private String evaluationMethodType = EvaluationMethodTypeEnum.PASSFAIL.name(); // Since the default value for boolean is false, this should default to PASSFAIL
    private List<String> evaluationMethodDescriptionList;
    private float weight;
    private boolean indicator;

    public WeightIndicatorResponse() {
    }

    public String getEvaluationMethodType() {
        return indicator
                ? EvaluationMethodTypeEnum.WEIGHTED.name()
                : EvaluationMethodTypeEnum.PASSFAIL.name();
    }

    private void initEvaluationMethodDescriptionList() {
        if (evaluationMethodDescriptionList == null) {
            evaluationMethodDescriptionList = new ArrayList<>();
        }
    }

    @JsonIgnore
    public List<String> getEvaluationMethodDescriptionList() {
        initEvaluationMethodDescriptionList();
        return evaluationMethodDescriptionList;
    }

    @JsonProperty("evaluationMethodDescription")
    public String getEvaluationMethodDescription() {
        initEvaluationMethodDescriptionList();
        return String.join("\n", evaluationMethodDescriptionList);
    }

    public void setEvaluationMethodDescription(@JsonProperty("evaluationMethodDescription") String description) {
        initEvaluationMethodDescriptionList();
        evaluationMethodDescriptionList.clear();
        if (Objects.nonNull(description)) {
            String[] descArray = description.split("[\\r\\n]+");
            evaluationMethodDescriptionList.addAll(Arrays.asList(descArray));
        }
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public boolean isIndicator() {
        return indicator;
    }

}
