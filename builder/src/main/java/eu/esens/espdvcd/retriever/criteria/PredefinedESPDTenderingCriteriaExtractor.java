package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.enums.QualificationApplicationTypeEnum;
import eu.esens.espdvcd.model.SelectableCriterion;
import test.x.ubl.pre_award.commonaggregate.TenderingCriterionType;
import test.x.ubl.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXB;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PredefinedESPDTenderingCriteriaExtractor implements CriteriaExtractor {

    private final List<TenderingCriterionType> criterionTypeList;

    private static final String ESPD_REQUEST_V2_REGULATED_RESOURCE = "/templates/v2_regulated/REGULATED-ESPDRequest-02.00.01.xml";
    private static final String ESPD_REQUEST_V2_SELF_CONTAINED_RESOURCE = "/templates/v2_self_contained/SELFCONTAINED-ESPDRequest-02.00.01.xml";

    public PredefinedESPDTenderingCriteriaExtractor(@NotNull QualificationApplicationTypeEnum type) {

        QualificationApplicationRequestType requestTemplate = null;

        switch (type) {
            case REGULATED:
                requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V2_REGULATED_RESOURCE), QualificationApplicationRequestType.class);
                break;
            case SELFCONTAINED:
                requestTemplate = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V2_SELF_CONTAINED_RESOURCE), QualificationApplicationRequestType.class);
                break;
            default:
                Logger.getLogger(PredefinedESPDTenderingCriteriaExtractor.class.getName())
                        .log(Level.SEVERE, "Error... Invalid QualificationApplicationType value, mandatory use of QualificationApplicationTypeEnum");
        }
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
        Logger.getLogger(PredefinedESPDTenderingCriteriaExtractor.class.getName()).log(Level.SEVERE, "Criterion List Size in model: " + initialSet.size());
        return new ArrayList<>(initialSet);
    }

}
