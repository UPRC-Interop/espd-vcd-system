package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.schema.SchemaVersion;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.espdrequest_1.ESPDRequestType;
import eu.espd.schema.v2.pre_award.commonaggregate.TenderingCriterionType;
import eu.espd.schema.v2.pre_award.qualificationapplicationrequest.QualificationApplicationRequestType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXB;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PredefinedESPDCriteriaExtractor implements CriteriaExtractor {

    private List<CriterionType> criterionTypeList;
    private Map<String, TenderingCriterionType> tenderingCriterionTypeMap;
    private List<SelectableCriterion> excelCriterionList;

    private static final String ESPD_REQUEST_V1_REGULATED_RESOURCE = "/templates/v1_regulated/espd-request-2018.03.xml";
    private static final String ESPD_REQUEST_V2_REGULATED_RESOURCE = "/templates/v2_regulated/espd-request-v2_2018-05-30a.xml";

    private final SchemaVersion version;

    Logger logger = Logger.getLogger(PredefinedESPDCriteriaExtractor.class.getName());

    public PredefinedESPDCriteriaExtractor(@NotNull SchemaVersion version) {
        this.version = version;

        switch (version) {
            case V1:
                ESPDRequestType requestV1Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V1_REGULATED_RESOURCE), ESPDRequestType.class);
                criterionTypeList = requestV1Template.getCriterion();
                break;
            case V2:
                QualificationApplicationRequestType requestV2Template = JAXB.unmarshal(CriteriaExtractor.class.getResourceAsStream(ESPD_REQUEST_V2_REGULATED_RESOURCE), QualificationApplicationRequestType.class);
                tenderingCriterionTypeMap = new HashMap<>();
                requestV2Template.getTenderingCriterion().forEach(tc -> tenderingCriterionTypeMap.put(tc.getID().getValue(), tc));
                PredefinedExcelCriteriaExtractor excelCriteriaExtractor = new PredefinedExcelCriteriaExtractor();
                excelCriterionList = excelCriteriaExtractor.getFullList();
                break;
            default:
                throw new IllegalArgumentException("Error... Invalid schema version value.");
        }

    }

    @Override
    public List<SelectableCriterion> getFullList() {

        List<SelectableCriterion> lc;

        switch (version) {

            case V1:
                lc = criterionTypeList.stream()
                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c))
                        .collect(Collectors.toList());
                break;

            case V2:
                lc = excelCriterionList.stream()
                        .map(sc -> applyDataFromV2Artefact(sc))
                        .collect(Collectors.toList());
                break;

            default:
                throw new IllegalArgumentException("Error... Invalid schema version value.");
        }

        return lc;
    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList) {
        return getFullList(initialList, false);
    }

//    @Override
//    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
//        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
//        initialSet.addAll(initialList);
//        Set<SelectableCriterion> fullSet;
//
//        switch (version) {
//
//            case V1:
//                fullSet = criterionTypeList.stream()
//                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
//                        .collect(Collectors.toSet());
//                break;
//
//            case V2:
//                fullSet = excelCriterionList.stream()
//                        .map(sc -> applyDataFromV2Artefact(sc, addAsSelected))
//                        .collect(Collectors.toSet());
//                break;
//
//            default:
//                throw new IllegalArgumentException("Error... Invalid schema version value.");
//        }
//
//        initialSet.addAll(fullSet);
//        Logger.getLogger(PredefinedESPDCriteriaExtractor.class.getName()).log(Level.INFO, "Criterion List Size in model: " + initialSet.size());
//        return new ArrayList<>(initialSet);
//    }

    @Override
    public List<SelectableCriterion> getFullList(List<SelectableCriterion> initialList, boolean addAsSelected) {
        Set<SelectableCriterion> initialSet = new LinkedHashSet<>();
        initialSet.addAll(initialList);
        logger.log(Level.INFO, "(1) InitialSet size: " + initialSet.size());
        Set<SelectableCriterion> fullSet;

        switch (version) {

            case V1:
                fullSet = criterionTypeList.stream()
                        .map(c -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(c, addAsSelected))
                        .collect(Collectors.toSet());
                break;

            case V2:
                fullSet = excelCriterionList.stream()
                        .map(sc -> applyDataFromV2Artefact(sc, addAsSelected))
                        .collect(Collectors.toSet());
                break;

            default:
                throw new IllegalArgumentException("Error... Invalid schema version value.");
        }

        logger.log(Level.INFO, "(2) fullSet size: " + fullSet.size());
        initialSet.addAll(fullSet);
        logger.log(Level.INFO, "(3) InitialSet size after adding fullSet: " + initialSet.size());
        return new ArrayList<>(initialSet);
    }

    private SelectableCriterion applyDataFromV2Artefact(SelectableCriterion excelCriterion) {
        return applyDataFromV2Artefact(excelCriterion, true);
    }

    private SelectableCriterion applyDataFromV2Artefact(SelectableCriterion excelCriterion, boolean isSelected) {

        TenderingCriterionType artefactCriterionType = tenderingCriterionTypeMap.get(excelCriterion.getID());

        if (artefactCriterionType != null) {
            SelectableCriterion artefactCriterion = ModelFactory.ESPD_REQUEST.extractSelectableCriterion(artefactCriterionType, isSelected);

            if (artefactCriterion.getLegislationReference() != null) {
                excelCriterion.setLegislationReference(artefactCriterion.getLegislationReference());
            }
        }

        return excelCriterion;
    }

}
