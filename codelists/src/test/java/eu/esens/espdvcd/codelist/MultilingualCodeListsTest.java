package eu.esens.espdvcd.codelist;

import java.io.UnsupportedEncodingException;
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
        
        String nameInEll;
        
        System.out.println(nameInEll = MultilingualCodeList.BidType.getValueForId("LOT_ALL"));
        System.out.println(MultilingualCodeList.BidType.getIdForData(nameInEll));
    }

}
