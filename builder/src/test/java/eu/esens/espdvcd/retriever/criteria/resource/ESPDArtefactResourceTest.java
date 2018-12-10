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
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ESPDArtefactResourceTest {

    private ESPDArtefactResource r1;
    private ESPDArtefactResource r2;

    @Before
    public void setUp() {
        r1 = new ESPDArtefactResource(EDMVersion.V1);
        Assert.assertNotNull(r1);
        r2 = new ESPDArtefactResource(EDMVersion.V2);
        Assert.assertNotNull(r2);
    }

    @Test
    public void testGetFullListV1() {
        SelectableCriterionPrinter.print(r1.getCriterionList());
    }

    @Test
    public void testGetFullListV2() {
        SelectableCriterionPrinter.print(r2.getCriterionList());
    }

}
