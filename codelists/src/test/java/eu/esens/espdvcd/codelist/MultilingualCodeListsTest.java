package eu.esens.espdvcd.codelist;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Konstantinos Raptis
 */
public class MultilingualCodeListsTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testMGC() throws UnsupportedEncodingException {
        Assert.assertEquals("Greece", MultilingualCodeList.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("GR", MultilingualCodeList.CountryIdentification.getIdForData("Greece"));

        Assert.assertEquals("SCLE", MultilingualCodeList.EORoleType.getIdForData("Sole contractor / Lead entity"));
        Assert.assertEquals("Sole contractor / Lead entity", MultilingualCodeList.EORoleType.getValueForId("SCLE"));

        Assert.assertEquals("LOT_ALL", MultilingualCodeList.BidType.getIdForData("Submission for all lots"));
        Assert.assertEquals("Submission for all lots", MultilingualCodeList.BidType.getValueForId("LOT_ALL"));

        MultilingualCodeList.BidType.setLang("ell");
        String valueInEll = MultilingualCodeList.BidType.getValueForId("LOT_ALL");
        Assert.assertEquals("LOT_ALL", MultilingualCodeList.BidType.getIdForData(valueInEll));
    }

    @Test
    public void testGetAllLangs() {
        Assert.assertTrue(MultilingualCodeList.CountryIdentification.getAllLangs().size() > 1);
    }

    @Test
    public void testCompatibilityBetweenV1AndMLCodelists() {

        // key = v1 constant name, value = ml constant name -> key is a subset of value
        Map<String, String> compatibilityMap = new HashMap<>();
        List<String> compatibilityList = new ArrayList<>();

        for (CodeListV1 cV1 : CodeListV1.values()) {
            boolean found = false;

            for (MultilingualCodeList cMl : MultilingualCodeList.values()) {
                if (cMl.getBiMap().keySet().containsAll(cV1.getBiMap().keySet())) {
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
