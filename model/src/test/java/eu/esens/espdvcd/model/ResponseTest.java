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
