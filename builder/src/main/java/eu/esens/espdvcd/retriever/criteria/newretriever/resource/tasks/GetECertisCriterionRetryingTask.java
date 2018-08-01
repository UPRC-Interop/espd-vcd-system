package eu.esens.espdvcd.retriever.criteria.newretriever.resource.tasks;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterionImpl;

import java.util.concurrent.Callable;

public class GetECertisCriterionRetryingTask implements Callable<ECertisCriterion> {

    private static final String ECERTIS_URL = "https://ec.europa.eu/growth/tools-databases/ecertisrest";
    private static final String ALL_CRITERIA_URL = ECERTIS_URL + "/criteria";

    private String ID;
    private EULanguageCodeEnum lang;

    public GetECertisCriterionRetryingTask(String ID) {
        this(ID, EULanguageCodeEnum.EN);
    }

    public GetECertisCriterionRetryingTask(String ID, EULanguageCodeEnum lang) {
        this.ID = ID;
        this.lang = lang;
    }

    @Override
    public ECertisCriterion call() throws Exception {

        GetFromECertisTask task = new GetFromECertisTask(ALL_CRITERIA_URL + "/" + ID + "?lang=" + lang.name().toLowerCase());
        GetFromECertisRetryingTask rTask = new GetFromECertisRetryingTask(task);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper.readValue(rTask.call(), ECertisCriterionImpl.class);
    }

}
