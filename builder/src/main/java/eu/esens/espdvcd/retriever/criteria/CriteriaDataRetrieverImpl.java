/**
 * Copyright 2016-2018 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria;

import com.github.rholder.retry.RetryException;
import eu.esens.espdvcd.builder.model.ModelFactory;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.criteria.resource.ECertisResource;
import eu.esens.espdvcd.retriever.criteria.resource.EvidencesResource;
import eu.esens.espdvcd.retriever.criteria.resource.tasks.GetECertisCriterionRetryingTask;
import eu.esens.espdvcd.retriever.exception.RetrieverException;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author konstantinos Raptis
 */
public class CriteriaDataRetrieverImpl implements CriteriaDataRetriever {

    private static final Logger LOGGER = Logger.getLogger(CriteriaDataRetrieverImpl.class.getName());

    private List<EvidencesResource> eResourceList;
    private ECertisResource eCertisResource;

    private EULanguageCodeEnum lang;

    private enum CriterionOrigin {EUROPEAN, NATIONAL}

    /* package private constructor. Create only through factory */
    CriteriaDataRetrieverImpl(@NotEmpty List<EvidencesResource> eResourceList) {
        this.eResourceList = eResourceList;
        this.lang = EULanguageCodeEnum.EN; // default language is English
        eCertisResource = new ECertisResource();
    }

    /**
     * Specifies the language of the retrieved data.
     *
     * @param lang The language code (ISO 639-1:2002)
     * @throws RetrieverException In case language code does not exist in relevant codelists.
     */
    @Override
    public void setLang(EULanguageCodeEnum lang) throws RetrieverException {

        if (isIdentificationCodeExist(lang)) {
            this.lang = lang;
        } else {
            throw new RetrieverException(String.format("Error... Provided language Code %s is not Included in codelists", lang));
        }
    }

    /**
     * Specifies if the given legislation reference belongs to
     * a European or National Criterion.
     *
     * @param lr The legislation reference of the Criterion
     * @return
     */
    private CriterionOrigin extractCriterionOrigin(@NotNull LegislationReference lr) {

        if (lr != null && lr.getJurisdictionLevelCode() != null) {

            String jlcValue = lr.getJurisdictionLevelCode();

            if (jlcValue.equals("eu")) {
                return CriterionOrigin.EUROPEAN;
            } else {
                return CriterionOrigin.NATIONAL;
            }

        }

        return null;
    }

    /**
     * Check if the given identification code, exist in the codelists.
     *
     * @param code The identification code (ISO 639-1:2002)
     * @return true if exists, false if not
     */
    private boolean isIdentificationCodeExist(String code) {
        String codeUpperCase = code.toUpperCase();

        return CodelistsV2.LanguageCodeEU
                .containsId(codeUpperCase);
    }

    /**
     * Check if the given identification code, exist in the codelists.
     *
     * @param code The identification code (ISO 639-1:2002)
     * @return true if exists, false if not
     */
    private boolean isIdentificationCodeExist(EULanguageCodeEnum code) {
        return CodelistsV2.LanguageCodeEU.containsId(code.name());
    }

    /**
     * Identifies the origin of given criterion ID (European or National). If
     * the criterion ID found to belong to a European criterion, then method
     * return all its National sub-Criteria, filtered by given country code. If
     * the criterion ID found to belong to a National criterion, then method
     * first searches for the parent European Criterion and then return parent
     * European Criterion National sub-Criteria, filtered again by given country
     * code.
     *
     * @param ID   The source Criterion ID (European or National)
     * @param code The country identification Code (ISO 639-1:2002)
     * @return All National Criteria, which mapped with source Criterion
     * @throws RetrieverException
     */
    @Override
    public List<SelectableCriterion> getNationalCriterionMapping(String ID, String code) throws RetrieverException {

        List<ECertisCriterion> nationalCriterionTypeList;

        if (isIdentificationCodeExist(code)) {

            GetECertisCriterionRetryingTask task = new GetECertisCriterionRetryingTask(ID);
            String codeLowerCase = code.toLowerCase();

            try {
                ECertisCriterion source = task.call();
                CriterionOrigin origin = extractCriterionOrigin(source.getLegislationReference());

                switch (origin) {
                    case EUROPEAN:
                        // Extract National Criteria
                        nationalCriterionTypeList = eCertisResource.getSubCriterionList(source, codeLowerCase);
                        break;
                    case NATIONAL:
                        // Get the EU Parent Criterion
                        ECertisCriterion parent = eCertisResource.getParentCriterion(source);
                        // Extract National Criteria
                        nationalCriterionTypeList = eCertisResource.getSubCriterionList(parent, codeLowerCase);
                        break;
                    default:
                        throw new RetrieverException("Error... Criterion " + ID + " cannot be Classified as European or National");
                }

            } catch (ExecutionException | RetryException | IOException e) {
                throw new RetrieverException(e);
            }

        } else {
            throw new RetrieverException(String.format("Error... Provided Country Code %s is not Included in codelists", code));
        }

        return nationalCriterionTypeList.stream()
                .map(ec -> ModelFactory.ESPD_REQUEST.extractSelectableCriterion(ec))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an e-Certis Criterion, which maps to
     * the related {@link SelectableCriterion} model class.
     *
     * @param ID The Criterion ID (European or National).
     * @return The Criterion
     * @throws RetrieverException
     */
    @Override
    public SelectableCriterion getCriterion(String ID) throws RetrieverException {
        return ModelFactory.ESPD_REQUEST.extractSelectableCriterion(eCertisResource.getECertisCriterion(ID, lang), true);
    }

    /**
     * Retrieves all the Evidences of criterion with given ID.
     *
     * @param ID The Criterion ID (European or National).
     * @return The Evidences
     * @throws RetrieverException
     */
    @Override
    public List<Evidence> getEvidences(String ID) throws RetrieverException {
        List<Evidence> evidenceList = new ArrayList<>();

        for (EvidencesResource eResource : eResourceList) {
            evidenceList.addAll(eResource.getEvidencesForCriterion(ID, lang));
        }

        return evidenceList;
    }

}
