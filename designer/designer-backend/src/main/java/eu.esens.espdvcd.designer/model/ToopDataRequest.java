package eu.esens.espdvcd.designer.model;

public class ToopDataRequest {
    private final String companyID;


    public ToopDataRequest(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyID() {
        return companyID;
    }
}
