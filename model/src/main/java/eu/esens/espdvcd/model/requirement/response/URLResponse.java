package eu.esens.espdvcd.model.requirement.response;

import java.io.Serializable;

public class URLResponse extends Response implements Serializable {

    private static final long serialVersionUID = 331803496620396887L;

    /**
     * Criterion fulfillment URI
     * <p>
     * A URI used as a reply to the criterion property.
     * <p>
     * Data type: URI<br>
     * Cardinality: 0..1<br>
     * InfReqID: tir92-558<br>
     * BusReqID: tbr92-018<br>
     * UBL syntax path: ccv:Criterion.RequirementGroup.Requirement.Response.URL<br>
     */
    private String url;

    public URLResponse() {

    }

    public URLResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
