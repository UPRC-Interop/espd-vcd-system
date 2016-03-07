package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.util.ArrayList;
import java.util.List;


/**
 * JPA implementation of {@link ESPDRequest}.
 *
 * @author Jerry Dimitriou <jerouris@unipi.gr>
 */
public class SimpleESPDRequest implements ESPDRequest {

    private static final long serialVersionUID = -8366478868586274094L;

    private Long id;

    private ESPDRequestModelType modelType = ESPDRequestModelType.ESPD_REQUEST_DRAFT;
    private ESPDRequestType espdRequestType;
    private CADetails caDetails;
    private List<SelectableCriterion> criterionList;

    public SimpleESPDRequest() {
        this.espdRequestType = new ESPDRequestType();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public CADetails getCADetails() {
       return this.caDetails;
    }

    @Override
    public void setCADetails(CADetails cd) {
        this.caDetails = cd;
    }

    /** Not used at the moment */
    @Override
    public ESPDRequestModelType getModelType() {
        return modelType;
    }

    @Override
    public void setModelType(ESPDRequestModelType modelType) {
        this.modelType = modelType;
    }

    @Override
    public ESPDRequestType getEspdRequestType() {
        return espdRequestType;
    }

    @Override
    public void setEspdRequestType(ESPDRequestType espdRequestType) {
        this.espdRequestType = espdRequestType;
    }

    @Override
    public List<SelectableCriterion> getCriterionList() {
        if (criterionList == null) {
            criterionList = new ArrayList<>();
        }
        return criterionList;
    }

    @Override
    public void setCriterionList(List<SelectableCriterion> criterionList) {
        this.criterionList = criterionList;
    }
}
