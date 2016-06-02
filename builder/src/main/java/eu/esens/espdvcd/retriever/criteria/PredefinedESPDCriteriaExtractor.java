package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXB;

public class PredefinedESPDCriteriaExtractor implements CriteriaExtractor {

    private final List<CriterionType> criterionTypeList;
    private static final String ESPDREQUEST_RESOURCE = "/espd-request-2016.06.xml";
    
    public PredefinedESPDCriteriaExtractor() {
        ESPDRequestType requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPDREQUEST_RESOURCE), ESPDRequestType.class);    
        criterionTypeList = requestTemplate.getCriterion();
    }
    
    @Override
    public List<SelectableCriterion> getFullList() {
        List<SelectableCriterion> lc = 
        criterionTypeList.stream()
                .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                .collect(Collectors.toList());               
        return lc;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }
    
     @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {

        System.out.println("Criterion List Size:"+criterionTypeList.size());
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet = 
        criterionTypeList.stream()
                .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
                .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
          System.out.println("Criterion List Size in model:"+initialSet.size());
        return new ArrayList<>(initialSet);
    }
    
}
