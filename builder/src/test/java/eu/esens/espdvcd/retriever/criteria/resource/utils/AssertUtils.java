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

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.requirement.response.evidence.Evidence;
import org.junit.Assert;

/**
 * @author Konstantinos Raptis [kraptis at unipi.gr] on 11/11/2020.
 */
public class AssertUtils {

    public static void assertLegislationAndItsFieldsNotNull(LegislationReference lr) {
        Assert.assertNotNull(lr);
        Assert.assertNotNull(lr.getTitle());
        Assert.assertNotNull(lr.getArticle());
        Assert.assertNotNull(lr.getDescription());
        Assert.assertNotNull(lr.getURI());
        Assert.assertNotNull(lr.getJurisdictionLevelCode());
    }

    public static void assertEvidenceAndItsFieldsNotNull(Evidence e) {
        Assert.assertNotNull(e);
        Assert.assertNotNull(e.getID());
        Assert.assertNotNull(e.getName());
        Assert.assertNotNull(e.getDescription());
        Assert.assertNotNull(e.getEvidenceURL());
    }

}
