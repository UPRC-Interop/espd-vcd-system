package eu.esens.espdvcd.model;

import java.io.Serializable;

/**
 *
 */
public class CADetails implements Serializable{

    private static final long serialVersionUID = -2251052431953226768L;
    
    private String caOfficialName;
    private String procurementProcedureTitle;
    private String procurementProcedureDesc;
    private String procurementProcedureFileReferenceNo;
   
    //This could be changed to something that comes from an enumeration
    private String caCountry;

    public String getCAOfficialName() {
        return caOfficialName;
    }

    public void setCAOfficialName(String caOfficialName) {
        this.caOfficialName = caOfficialName;
    }

    public String getProcurementProcedureTitle() {
        return procurementProcedureTitle;
    }

    public void setProcurementProcedureTitle(String procurementProcedureTitle) {
        this.procurementProcedureTitle = procurementProcedureTitle;
    }

    public String getProcurementProcedureDesc() {
        return procurementProcedureDesc;
    }

    public void setProcurementProcedureDesc(String procurementProcedureDesc) {
        this.procurementProcedureDesc = procurementProcedureDesc;
    }

    public String getProcurementProcedureFileReferenceNo() {
        return procurementProcedureFileReferenceNo;
    }

    public void setProcurementProcedureFileReferenceNo(String procurementProcedureFileReferenceNo) {
        this.procurementProcedureFileReferenceNo = procurementProcedureFileReferenceNo;
    }

    public String getCACountry() {
        return caCountry;
    }

    public void setCACountry(String caCountry) {
        this.caCountry = caCountry;
    }
   
}
