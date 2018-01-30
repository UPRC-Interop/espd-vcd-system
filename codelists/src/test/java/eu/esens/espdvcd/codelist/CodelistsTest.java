package eu.esens.espdvcd.codelist;

import java.io.UnsupportedEncodingException;
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
    public void testGC() throws UnsupportedEncodingException {
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Main tenderer", CodelistsV1.TenderingRole.getValueForId("MT"));
        
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Sole contractor / Lead entity", CodelistsV2.EORoleType.getValueForId("SCLE"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getValueForId("LOT_ALL"));
        CodelistsV2.BidType.setLang("ell");
        Assert.assertEquals("Υποβολή για όλες τις παρτίδες", CodelistsV2.BidType.getValueForId("LOT_ALL"));
    }

    @Test
    public void testMap() {
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getDataMap().get("GR"));
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getDataMap().get("GR"));
    }

    @Test
    public void testAllGC() {

        String theCodelist = "";

        try {

            for (Codelists c : CodelistsV1.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getDataMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }

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
