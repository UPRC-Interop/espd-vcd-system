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
        Assert.assertEquals("Greece", CodeListsVersioner.ForVersion1.COUNTRY_IDENTIFICATION.getValueForId("GR"));
        Assert.assertEquals("GR", CodeListsVersioner.ForVersion1.COUNTRY_IDENTIFICATION.getIdForData("Greece"));

        Assert.assertEquals("MT", CodeListsVersioner.ForVersion1.TENDERING_ROLE.getIdForData("Main tenderer"));
        Assert.assertEquals("Main tenderer", CodeListsVersioner.ForVersion1.TENDERING_ROLE.getValueForId("MT"));

        // v2
        Assert.assertEquals("Andorra", CodeListsVersioner.ForVersion2.COUNTRY_IDENTIFICATION.getValueForId("AD"));
        Assert.assertEquals("AD", CodeListsVersioner.ForVersion2.COUNTRY_IDENTIFICATION.getIdForData("Andorra"));

        Assert.assertEquals("Group member", CodeListsVersioner.ForVersion2.EO_ROLE_TYPE.getValueForId("GM"));
        Assert.assertEquals("GM", CodeListsVersioner.ForVersion2.EO_ROLE_TYPE.getIdForData("Group member"));
    }

    @Test
    public void testBiMap() {

        // v1
        Assert.assertEquals("Greece", CodeListsVersioner.ForVersion1.COUNTRY_IDENTIFICATION.getBiMap().get("GR"));
        Assert.assertEquals("GR", CodeListsVersioner.ForVersion1.COUNTRY_IDENTIFICATION.getBiMap().inverse().get("Greece"));

        // v2
        Assert.assertEquals("Andorra", CodeListsVersioner.ForVersion2.COUNTRY_IDENTIFICATION.getBiMap().get("AD"));
        Assert.assertEquals("AD", CodeListsVersioner.ForVersion2.COUNTRY_IDENTIFICATION.getBiMap().inverse().get("Andorra"));
    }

    @Test
    public void testAllGC() {

        String theCodelist = "";

        // v1
        try {

            for (CodeLists c : CodeListsVersioner.ForVersion1.getCodeLists()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }

        //v2
        try {

            for (CodeLists c : CodeListsVersioner.ForVersion2.getCodeLists()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getBiMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }
    }

}
