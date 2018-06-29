package eu.esens.espdvcd.designer.model;

public class ToopDataRequest {
    private String companyID;
    private String countryCode;

    public ToopDataRequest(){

    }

    public ToopDataRequest(String companyID, String countryCode) {
        this.companyID = companyID;
        this.countryCode = countryCode;

    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCompanyID() {
        return companyID;
    }
}
