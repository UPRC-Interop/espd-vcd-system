package eu.esens.espdvcd.model.persisted;

import eu.esens.espdvcd.model.CADetails;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import java.util.List;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

/**
 * JPA implementation of {@link ESPDRequest}.
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(schema = "espd_vcd", name = "espd_requests")
public class PersistedESPDRequest implements ESPDRequest {

    private static final long serialVersionUID = 880277112847573817L;

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

    public PersistedESPDRequest(ESPDRequestModelType modelType, ESPDRequestType espdRequestType) {
        super();
        this.modelType = modelType;
        this.espdRequestType = espdRequestType;
    }

    public PersistedESPDRequest(ESPDRequestModelType modelType) {
        super();
        this.modelType = modelType;
        this.espdRequestType = new ESPDRequestType();
    }

    public PersistedESPDRequest() {
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
    public List<SelectableCriterion> getFullCriterionList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCriterionList(List<SelectableCriterion> criterionList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getSelectionCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getExclusionCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectableCriterion> getEORelatedCriteriaList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
