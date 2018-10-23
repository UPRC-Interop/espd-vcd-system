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
package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

public class WeightIndicatorResponse extends Response implements Serializable {

    private static final long serialVersionUID = -3204508364109778286L;
    private String evaluationMethodType;
    private String evaluationMethodDescription;
    private float weight;
    private boolean indicator;

    public WeightIndicatorResponse() {
    }

    public String getEvaluationMethodType() {
        return evaluationMethodType;
    }

    public void setEvaluationMethodType(String evaluationMethodType) {
        this.evaluationMethodType = evaluationMethodType;
    }

    public String getEvaluationMethodDescription() {
        return evaluationMethodDescription;
    }

    public void setEvaluationMethodDescription(String evaluationMethodDescription) {
        this.evaluationMethodDescription = evaluationMethodDescription;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }
}
