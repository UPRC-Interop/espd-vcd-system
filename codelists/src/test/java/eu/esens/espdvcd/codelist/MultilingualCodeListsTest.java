package eu.esens.espdvcd.codelist;

import java.io.UnsupportedEncodingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Konstantinos Raptis
 */
public class MultilingualCodeListsTest {

    @Before
    public void setUp() {
    }
    
    @Ignore
    @Test
    public void testMGC() throws UnsupportedEncodingException {
        Assert.assertEquals("Greece", MultilingualCodeList.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("GR", MultilingualCodeList.CountryIdentification.getIdForData("Greece"));

        Assert.assertEquals("SCLE", MultilingualCodeList.EORoleType.getIdForData("Sole contractor / Lead entity"));
        Assert.assertEquals("Sole contractor / Lead entity", MultilingualCodeList.EORoleType.getValueForId("SCLE"));

        Assert.assertEquals("LOT_ALL", MultilingualCodeList.BidType.getIdForData("Submission for all lots"));
        Assert.assertEquals("Submission for all lots", MultilingualCodeList.BidType.getValueForId("LOT_ALL"));

        MultilingualCodeList.BidType.setLang("ell");

        String nameInEll;

        System.out.println(nameInEll = MultilingualCodeList.BidType.getValueForId("LOT_ALL"));
        System.out.println(MultilingualCodeList.BidType.getIdForData(nameInEll));
    }

    @Test
    public void testV1Compatibility() {

        // ActivityType
        //Assert.assertTrue("ActivityType multilingual not compatible with ActivityType V1", 
        //        MultilingualCodeList.ActivityType.getBiMap().keySet().containsAll(CodeListV1.ActivityType.getBiMap().keySet()));
        // AmountType
        // Assert.assertTrue("AmountType multilingual not compatible with AmountType V1",
        //        MultilingualCodeList.AmountType.getBiMap().keySet().containsAll(CodeListV1.AmountType.getBiMap().keySet()));
        // BitType -> Not an equivelent gc file in v1 codelists (NEW)

        // ConfidentialityLevel -> Not an equivelent gc file in v1 codelists (NEW)
        // ContractType
        // Assert.assertTrue("ContractType multilingual not compatible with ContractType V1",
        //        MultilingualCodeList.ContractType.getBiMap().keySet().containsAll(CodeListV1.ContractType.getBiMap().keySet()));
        // CountryCodeIdentifier
        //Assert.assertTrue("CountryIdentification multilingual not compatible with CountryIdentification V1",
        //        MultilingualCodeList.CountryIdentification.getBiMap().keySet().containsAll(CodeListV1.CountryIdentification.getBiMap().keySet()));
        // CriterionElementType
        for (CodeListV1 cV1 : CodeListV1.values()) {
            boolean found = false;
            
            for (MultilingualCodeList cMl : MultilingualCodeList.values()) {
                if (cMl.getBiMap().keySet().containsAll(cV1.getBiMap().keySet())) {
                    System.out.println(cV1.getConstantName() + " V1 is a subset of " + cMl.getConstantName() + " ML");
                    found = true;
                    break;
                }
            }
            
            if (!found) System.err.println(cV1.getConstantName() + " V1 is not a subset of any ML codelist");
        }

    }

}
