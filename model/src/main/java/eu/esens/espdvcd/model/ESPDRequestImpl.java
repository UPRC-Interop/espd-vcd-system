package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.dao.GenericDAO;
import eu.esens.espdvcd.model.persistence.DB;
import eu.esens.espdvcd.model.types.ESPDRequestModelType;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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
}
