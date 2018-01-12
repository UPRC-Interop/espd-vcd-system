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

        // v1
        Assert.assertEquals("Greece", CodeListV1.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("GR", CodeListV1.CountryIdentification.getIdForData("Greece"));

        Assert.assertEquals("MT", CodeListV1.TenderingRole.getIdForData("Main tenderer"));
        Assert.assertEquals("Main tenderer", CodeListV1.TenderingRole.getValueForId("MT"));

        // v2
        Assert.assertEquals("Andorra", CodeListV2.CountryIdentification.getValueForId("AD"));
        Assert.assertEquals("AD", CodeListV2.CountryIdentification.getIdForData("Andorra"));

        Assert.assertEquals("Group member", CodeListV2.EORoleType.getValueForId("GM"));
        Assert.assertEquals("GM", CodeListV2.EORoleType.getIdForData("Group member"));
    }

//    @Test
//    public void testBiMap() {
//        Assert.assertEquals("Greece", CodeListV1.CountryIdentification.getBiMap().get("GR"));
//        Assert.assertEquals("GR", CodeListV1.CountryIdentification.getBiMap().inverse().get("Greece"));
//    }    

    @Test
    public void testAllGC() {

        String theCodelist = "";
        
        // v1
        try {

            for (CodeList c : CodeListV1.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }
        
        //v2
        try {

            for (CodeList c : CodeListV2.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }
    }

}
