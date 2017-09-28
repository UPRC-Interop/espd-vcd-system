package eu.esens.espdvcd.codelist;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
         Assert.assertEquals("PRIOR_WO_CALL", CodeListsTestImpl.NotyceType.getNameForIdV2("TD001"));
         Assert.assertEquals("TD001", CodeListsTestImpl.NotyceType.getIdForDataV2("PRIOR_WO_CALL"));
         Assert.assertEquals("Information notice without call for competition", CodeListsTestImpl.NotyceType.getDescriptionForIdV2("TD001"));
    }

}
