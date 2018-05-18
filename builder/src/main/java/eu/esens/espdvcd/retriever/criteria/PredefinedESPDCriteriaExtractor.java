package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.SelectableCriterion;
import grow.names.specification.ubl.schema.xsd.espdrequest_1.ESPDRequestType;
import isa.names.specification.ubl.schema.xsd.ccv_commonaggregatecomponents_1.CriterionType;
import test.x.ubl.pre_award.commonaggregate.TenderingCriterionType;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXB;

public class PredefinedESPDCriteriaExtractor implements CriteriaExtractor {

//    private final List<CriterionType> criterionTypeList;
    private final List<TenderingCriterionType> criterionTypeList;

    private static final String ESPD_REQUEST_V1_REGULATED_RESOURCE = "/templates/v1_regulated/espd-request-2018.03.xml";
    private static final String ESPD_REQUEST_V2_REGULATED_RESOURCE = "/templates/v2_regulated/REGULATED_ESPD-Request_2.0.2.xml";
    private static final String ESPD_REQUEST_V2_SELF_CONTAINED_RESOURCE = "/templates/v2_self_contained/SELFCONTAINED_ESPD-Request.V2.0.2.xml";

    public PredefinedESPDCriteriaExtractor() {
//        ESPDRequestType requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V1_REGULATED_RESOURCE), ESPDRequestType.class);
//        criterionTypeList = requestTemplate.getCriterion();
        QualificationApplicationRequestType requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V2_REGULATED_RESOURCE), QualificationApplicationRequestType.class);
        criterionTypeList = requestTemplate.getTenderingCriterion();
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

        System.out.println("Criterion List Size:" + criterionTypeList.size());
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        Set<SelectableCriterion> fullSet =
                criterionTypeList.stream()
                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
                        .collect(Collectors.toSet());
        initialSet.addAll(fullSet);
        Logger.getLogger(PredefinedESPDCriteriaExtractor.class.getName()).log(Level.SEVERE, "Criterion List Size in model: " + initialSet.size());
        return new ArrayList<>(initialSet);
    }

}
