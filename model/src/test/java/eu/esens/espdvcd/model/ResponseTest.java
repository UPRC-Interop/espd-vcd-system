/**
 * Copyright 2016-2019 University of Piraeus Research Center
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
package eu.esens.espdvcd.model;

import eu.esens.espdvcd.model.requirement.response.WeightIndicatorResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ResponseTest {

    private WeightIndicatorResponse weightIndicatorResponse;

    @Before
    public void setUp() {
        weightIndicatorResponse = new WeightIndicatorResponse();
    }

    @Test
    public void testGetEvaluationMethodDescription() {
        weightIndicatorResponse.getEvaluationMethodDescriptionList().add("This");
        weightIndicatorResponse.getEvaluationMethodDescriptionList().add("is");
        weightIndicatorResponse.getEvaluationMethodDescriptionList().add("a");
        weightIndicatorResponse.getEvaluationMethodDescriptionList().add("description");
        Assert.assertEquals("This\nis\na\ndescription", weightIndicatorResponse.getEvaluationMethodDescription());
    }

    @Test
    public void testSetEvaluationMethodDescription() {
        weightIndicatorResponse.setEvaluationMethodDescription("This\nis\na\ndescription");
        List<String> descList = new ArrayList<>();
        descList.add("This");
        descList.add("is");
        descList.add("a");
        descList.add("description");
        Assert.assertTrue(weightIndicatorResponse.getEvaluationMethodDescriptionList().containsAll(descList));
    }

}
