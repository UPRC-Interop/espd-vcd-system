package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * JPA implementation of {@link ESPDRequest}.
 *
 * @author Panagiotis NICOLAOU <pnikola@unipi.gr>
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(schema = "espd_vcd", name = "espd_requests")
public class ESPDRequestImpl implements ESPDRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "model_type")
    private ESPDRequestModelType modelType = ESPDRequestModelType.ESPD_REQUEST_DRAFT;

    @OneToOne(cascade = CascadeType.ALL)
//    @Column(name = "espd_request")
    private ESPDRequestType espdRequestType;
    
    private CADetails caDetails;

    private List<SelectableCriterion> exclusionCriteria;

    private List<SelectableCriterion> selectionCriteria;

    public ESPDRequestImpl(ESPDRequestModelType modelType, ESPDRequestType espdRequestType) {
        super();
        this.modelType = modelType;
        this.espdRequestType = espdRequestType;
    }

    public ESPDRequestImpl(ESPDRequestModelType modelType) {
        super();
        this.modelType = modelType;
        this.espdRequestType = new ESPDRequestType();
    }

    public ESPDRequestImpl() {
        this.espdRequestType = new ESPDRequestType();
    }

    @Override
    public Long getId() {
        return id;
    }

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
    public CADetails getCADetails() {
       return this.caDetails;
    }

    @Override
    public void setCADetails(CADetails cd) {
        this.caDetails = cd;
    }

    @Override
    public List<SelectableCriterion> getExclusionCriteria() {
        return this.exclusionCriteria;
    }

    @Override
    public void setExclusionCriteria(List<SelectableCriterion> exclusionCriteria) {
        this.exclusionCriteria = exclusionCriteria;
    }

    @Override
    public List<SelectableCriterion> getSelectionCriteria() {
        return this.selectionCriteria;
    }

    @Override
    public void setSelectionCriteria(List<SelectableCriterion> selectionCriteria) {
        this.selectionCriteria = selectionCriteria;
    }
}
