/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.builder.schema.v2;

import eu.esens.espdvcd.model.Criterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.espd.schema.v2.v210.commonaggregate.TenderingCriterionPropertyType;
import eu.espd.schema.v2.v210.commonaggregate.TenderingCriterionType;

public class PDFRequestSchemaExtractorV2 extends ESPDRequestSchemaExtractorV2 {

    @Override
    public TenderingCriterionType extractTenderingCriterion(Criterion c) {
        c.setKeyAsResponseValue(true);
        return super.extractTenderingCriterion(c);
    }

    @Override
    public TenderingCriterionPropertyType extractTenderingCriterionPropertyType(Requirement rq) {
        rq.setKeyAsResponseValue(true);
        return super.extractTenderingCriterionPropertyType(rq);
    }

}
