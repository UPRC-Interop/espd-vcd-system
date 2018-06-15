package eu.esens.espdvcd.retriever.criteria;

import eu.esens.espdvcd.retriever.exception.RetrieverException;

public interface MultilingualCriteriaExtractor extends CriteriaExtractor {

    void setLang(String lang) throws RetrieverException;

    String getLang();

    void initLang();

}
