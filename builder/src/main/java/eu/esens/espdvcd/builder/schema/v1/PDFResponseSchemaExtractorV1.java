/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.builder.schema.v1;

import eu.esens.espdvcd.codelist.enums.ResponseTypeEnum;
import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.response.PercentageResponse;
import eu.esens.espdvcd.model.requirement.response.Response;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.CriterionType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.RequirementType;
import eu.espd.schema.v1.ccv_commonaggregatecomponents_1.ResponseType;
import eu.espd.schema.v1.commonbasiccomponents_2.PercentType;

public class PDFResponseSchemaExtractorV1 extends ESPDResponseSchemaExtractorV1 {

    @Override
    public CriterionType extractCriterion(Criterion c) {
        c.setKeyAsResponseValue(true);
        return super.extractCriterion(c);
    }

    @Override
    public RequirementType extractRequirementType(Requirement r) {
        r.setKeyAsResponseValue(true);
        return super.extractRequirementType(r);
    }

    @Override
    protected ResponseType extractResponse(Response response, ResponseTypeEnum respType) {

        ResponseType theRespType = super.extractResponse(response, respType);

        if (respType == ResponseTypeEnum.PERCENTAGE && theRespType != null &&
                response != null &&
                ((PercentageResponse) response).getPercentage() != null) {
            theRespType.setPercent(new PercentType());
            theRespType.getPercent().setValue(((PercentageResponse) response).getPercentage());
        }

        return theRespType;
    }

}
