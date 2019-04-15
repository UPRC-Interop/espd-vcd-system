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
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.codelist.enums.internal.ContractingOperatorEnum;
import eu.esens.espdvcd.retriever.criteria.resource.enums.CriteriaConfig;
import org.junit.Assert;
import org.junit.Test;

public class CriteriaConfigTest {

    @Test
    public void testCompulsoriness() {
        Assert.assertTrue(CriteriaConfig.getInstance().isCompulsory(ContractingOperatorEnum.CONTRACTING_AUTHORITY, "005eb9ed-1347-4ca3-bb29-9bc0db64e1ab"));
    }

    @Test
    public void testPreSelected() {
        Assert.assertTrue(CriteriaConfig.getInstance().isPreSelected(ContractingOperatorEnum.CONTRACTING_ENTITY, "d486fb70-86b3-4e75-97f2-0d71b5697c7d"));
    }

}
