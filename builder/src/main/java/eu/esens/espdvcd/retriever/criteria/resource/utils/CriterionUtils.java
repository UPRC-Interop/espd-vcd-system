/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource.utils;

import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.retriever.ECertisCriterion;
import eu.esens.espdvcd.retriever.exception.RetrieverException;

import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 5/11/2020.
 */
public class CriterionUtils {

    private static final Logger LOGGER = Logger.getLogger(CriterionUtils.class.getName());

    /**
     * Check if it is a European Criterion. If not, its a National.
     *
     * @param criterion
     * @return
     * @throws RetrieverException In case the Criterion cannot been classified as European or National.
     */
    public static boolean isEuropean(@NotNull ECertisCriterion criterion) throws RetrieverException {

        if (criterion.getLegislationReference() != null
                && criterion.getLegislationReference().getJurisdictionLevelCode() != null) {

            return criterion.getLegislationReference()
                    .getJurisdictionLevelCode().equals("eu");
        } else {
            throw new RetrieverException("Error... Criterion " + criterion.getID()
                    + " cannot be classified as European or National.");
        }
    }

    /**
     * Check if it is a European Criterion. If not, its a National.
     *
     * @param criterion
     * @return
     * @throws RetrieverException In case the Criterion cannot been classified as European or National.
     */
    public static boolean isEuropean(@NotNull SelectableCriterion criterion) throws RetrieverException {

        if (criterion.getLegislationReference() != null
                && criterion.getLegislationReference().getJurisdictionLevelCode() != null) {

            return criterion.getLegislationReference()
                    .getJurisdictionLevelCode().equals("eu");
        } else {
            throw new RetrieverException("Error... Criterion " + criterion.getID()
                    + " cannot be classified as European or National.");
        }
    }

}
