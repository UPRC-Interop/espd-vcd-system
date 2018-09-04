package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.util.CodelistItem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

public class CodelistsServiceTest {
    CodelistsService codelistsV1Service, codelistsV2Service;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        codelistsV1Service = new CodelistsV1Service();
        codelistsV2Service = new CodelistsV2Service();
    }

    @Test
    public void testV1CodelistList() {
        Assert.assertArrayEquals(CodelistsV1.values(), codelistsV1Service.getAvailableCodelists());
    }

    @Test
    public void testV2CodelistList() {
        Assert.assertArrayEquals(CodelistsV2.values(), codelistsV2Service.getAvailableCodelists());
    }

    @Test
    public void testNonExistingV1Codelist() {
        expectedException.expect(IllegalArgumentException.class);
        codelistsV1Service.getCodelist("blahBlah");
    }

    @Test
    public void testNonExistingV2Codelist() {
        expectedException.expect(IllegalArgumentException.class);
        codelistsV2Service.getCodelist("blahBlah");
    }

    @Test
    public void testV1Codelist() {
        Map<String, String> theV1Codelist = CodelistsV1.Currency.getDataMap();
        List<CodelistItem> theCodelistItemList = new ArrayList<>(theV1Codelist.size());
        theV1Codelist.forEach((key, value) -> theCodelistItemList.add(new CodelistItem(key, value)));

        Assert.assertArrayEquals(theCodelistItemList.toArray(), codelistsV1Service.getCodelist("Currency").toArray());
    }

    @Test
    public void testV2TranslatedCodelist() {
        Map<String, String> theV2Codelist = CodelistsV2.Currency.getDataMap("ell");
        List<CodelistItem> theCodelistItemList = new ArrayList<>(theV2Codelist.size());
        theV2Codelist.forEach((key, value) -> theCodelistItemList.add(new CodelistItem(key, value)));

        Assert.assertArrayEquals(theCodelistItemList.toArray(), codelistsV2Service.getTranslatedCodelist("Currency", "ell").toArray());
    }

    @Test
    public void testV2Codelist() {
        Map<String, String> theV2Codelist = CodelistsV2.Currency.getDataMap();
        List<CodelistItem> theCodelistItemList = new ArrayList<>(theV2Codelist.size());
        theV2Codelist.forEach((key, value) -> theCodelistItemList.add(new CodelistItem(key, value)));

        Assert.assertArrayEquals(theCodelistItemList.toArray(), codelistsV2Service.getCodelist("Currency").toArray());    }
}