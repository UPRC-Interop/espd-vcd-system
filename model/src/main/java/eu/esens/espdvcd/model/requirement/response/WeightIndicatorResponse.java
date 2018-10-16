package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

public class WeightIndicatorResponse extends Response implements Serializable {

    private String evaluationMethodType;
    private String evaluationMethodDescription;
    private float weight;

    public WeightIndicatorResponse() {
    }

    public WeightIndicatorResponse(String evaluationMethodType, String evaluationMethodDescription, float weight) {
        this.evaluationMethodType = evaluationMethodType;
        this.evaluationMethodDescription = evaluationMethodDescription;
        this.weight = weight;
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
}
