package eu.esens.espdvcd.builder;

import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXB;

public class ESPDCriteriaExtractor implements CriteriaExtractor {

    private final List<CriterionType> criterionTypeList;
    private static final String ESPDREQUEST_RESOURCE = "/espd-request.xml";
    private static final ESPDRequestModelFactory MODEL_FACTORY = new ESPDRequestModelFactory();
    
    public ESPDCriteriaExtractor() {
        ESPDRequestType requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPDREQUEST_RESOURCE), ESPDRequestType.class);    
        criterionTypeList = requestTemplate.getCriterion();
        
        
    }
    
    @Override
    public List<SelectableCriterion> getFullList() {
        return criterionTypeList.stream()
                .map(c -> MODEL_FACTORY.extractSelectableCriterion(c))
                .collect(Collectors.toList());                
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
