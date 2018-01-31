package eu.esens.espdvcd.codelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodelistsTest {

    public CodelistsTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAllLangs() {
        Assert.assertTrue(CodelistsV2.CountryIdentification.getAllLangs().size() > 1);
        Assert.assertTrue(CodelistsV1.CountryIdentification.getAllLangs().size() == 1);
    }

    @Test
    public void testDataMapV1() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getDataMap().get("GR"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV1.CountryIdentification.getDataMap().get("lala"));
    }

    @Test
    public void testDataMapV2() {
        // valid input
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap().get("LOT_ALL"));
        Assert.assertEquals("Υποβολή για όλες τις παρτίδες", CodelistsV2.BidType.getDataMap("ell").get("LOT_ALL"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV2.BidType.getDataMap().get(null));
        Assert.assertEquals(null, CodelistsV2.BidType.getDataMap().get("lala"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap("lala").get("LOT_ALL"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap(null).get("LOT_ALL"));
    }
    
    @Test
    public void testGetValueForIdV1() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Main tenderer", CodelistsV1.TenderingRole.getValueForId("MT"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV1.CountryIdentification.getValueForId(null));
        Assert.assertEquals(null, CodelistsV1.TenderingRole.getValueForId("lala"));
    }
    
    @Test
    public void testGetValueForIdV2() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getValueForId("LOT_ALL"));
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR", "ell"));
        Assert.assertEquals("Υποβολή για όλες τις παρτίδες", CodelistsV2.BidType.getValueForId("LOT_ALL", "ell"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV2.CountryIdentification.getValueForId(null));
        Assert.assertEquals(null, CodelistsV2.BidType.getValueForId("lala"));
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR", null));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getValueForId("LOT_ALL", "lala"));
    }

    @Test
    public void testContainsIdV1() {
        // valid input
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsId("GR"));
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsId("GB"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsId(null));
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsId("lala"));
    }

    @Test
    public void testContainsIdV2() {
        // valid input
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GR"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GB"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GR", "ell"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GB", "ell"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId(null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("lala"));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("GR", null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("GR", "lala"));
    }
    
    @Test
    public void testContainsValueV1() {
        // valid input
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsValue("Greece"));
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsValue("France"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsValue(null));
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsValue("lala"));
    }

    @Test
    public void testContainsValueV2() {
        // valid input
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("Greece"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("France"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("Greece", "bul"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("France", "deu"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue(null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("lala"));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("Greece", null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("Greece", "lala"));
    }
    
    @Test
    public void testAllGCV1() {
        String theCodelist = "";

        try {

            for (Codelists c : CodelistsV1.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getDataMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }

    }

    @Test
    public void testAllGCV2() {
        String theCodelist = "";

        try {

            for (Codelists c : CodelistsV2.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getDataMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }
    }

    @Test
    public void testCompatibilityBetweenV1AndMLCodelists() {

        // key = v1 constant name, value = ml constant name -> key is a subset of value
        Map<String, String> compatibilityMap = new HashMap<>();
        List<String> compatibilityList = new ArrayList<>();

        for (CodelistsV1 cV1 : CodelistsV1.values()) {
            boolean found = false;

            for (CodelistsV2 cMl : CodelistsV2.values()) {
                if (cMl.getDataMap().keySet().containsAll(cV1.getDataMap().keySet())) {
                    compatibilityMap.put(cV1.getConstantName(), cMl.getConstantName());
                    found = true;
                    break;
                }
            }

            if (!found) {
                compatibilityList.add(cV1.getConstantName());
            }
        }

        System.out.println("CompatibilityBetweenV1AndMLCodelists -> Compatible\n");
        compatibilityMap.forEach((v1, ml) -> System.out.printf("%-35s is a subset of %-35s \n", v1 + "(V1)", ml + "(ML)"));
        System.out.println("\nCompatibilityBetweenV1AndMLCodelists -> Non Compatible\n");
        compatibilityList.forEach(v1 -> System.out.printf("%-35s is not a subset of any ML codelist\n", v1 + "(V1)"));
    }

}
