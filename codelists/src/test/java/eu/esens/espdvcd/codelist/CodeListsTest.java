package eu.esens.espdvcd.codelist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeListsTest {

    public CodeListsTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGC() {

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

//    @Test
//    public void testBiMap() {
//        Assert.assertEquals("Greece", MultilingualCodeList.CountryIdentification.getBiMap().get("GR"));
//        Assert.assertEquals("GR", MultilingualCodeList.CountryIdentification.getBiMap().inverse().get("Greece"));
//    }    
       
//    @Test
//    public void testProjectTypeGC() {
//        System.out.println(MultilingualCodeList.ProcedureType.getBiMap().isEmpty());
//    }

//    @Test
//    public void testAllGC() {
//
//        String theCodelist = "";
//        
//        try {
//
//            for (CodeList c : MultilingualCodeList.values()) {
//                theCodelist = c.getConstantName();
//                Assert.assertNotNull(!c.getBiMap().isEmpty());
//            }
//
//        } catch (Exception e) {
//            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
//        }
//    }

}
