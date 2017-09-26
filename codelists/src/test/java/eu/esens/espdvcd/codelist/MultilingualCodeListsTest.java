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

        CodeListsTestImpl.NotyceType.setLang("el");
        String id = "TD001";
        
        Assert.assertEquals("PRIOR_WO_CALL", CodeListsTestImpl.NotyceType.getNameForIdV2(id));
        System.out.println(CodeListsTestImpl.NotyceType.getDescriptionForIdV2(id));
        
    }

}
