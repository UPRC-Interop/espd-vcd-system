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
package eu.esens.espdvcd.codelist;

import eu.esens.espdvcd.codelist.enums.internal.PropertyKeyConfigEnum;
import org.junit.Assert;
import org.junit.Test;

public class PropertyKeyConfigEnumTest {

    @Test
    public void testGetNamePropertyKeyFor() {
        Assert.assertEquals("espd.part5.title.eo.declares.that",
                PropertyKeyConfigEnum.getInstance().getCriterionNamePropertyKey("9c70375e-1264-407e-8b50-b9736bc08901"));
    }

    @Test
    public void testGetDescriptionPropertyKeyFor() {
        Assert.assertEquals("espd.part5.text.eo.declares.that",
                PropertyKeyConfigEnum.getInstance().getCriterionDescriptionPropertyKey("9c70375e-1264-407e-8b50-b9736bc08901"));
    }

    @Test
    public void testGetRequirementDescriptionPropertyKey() {
        Assert.assertEquals("espd.crit.your.answer",
                PropertyKeyConfigEnum.getInstance().getRequirementDescriptionPropertyKey("Your answer"));

        Assert.assertEquals("espd.crit.information.available.electronically",
                // PropertyKeyConfigEnum.getInstance().getRequirementDescriptionPropertyKey("Is this information available electronically\"?\""));
                PropertyKeyConfigEnum.getInstance().getRequirementDescriptionPropertyKey("Is this information available electronically?"
                        .replaceAll("\\?", ""))); // workaround in oder to deal with ? special char
    }

}
