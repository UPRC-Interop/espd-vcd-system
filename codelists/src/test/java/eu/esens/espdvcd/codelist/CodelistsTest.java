/**
 * Copyright 2016-2019 University of Piraeus Research Center
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.codelist;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class CodelistsTest {

    public CodelistsTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAllLang() {
        Assert.assertTrue(CodelistsV2.CountryIdentification.getAllLangs().size() > 1);
        Assert.assertTrue(CodelistsV1.CountryIdentification.getAllLang().size() == 1);
    }

    @Test
    public void testDataMapV1() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getDataMap().get("GR"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV1.CountryIdentification.getDataMap().get("lala"));
    }

    @Test
    public void testDataMapV2() {
        // valid input
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap().get("LOT_ALL"));
        Assert.assertEquals("Υποβολή για �?λες τις πα�?τίδες", CodelistsV2.BidType.getDataMap("ell").get("LOT_ALL"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV2.BidType.getDataMap().get(null));
        Assert.assertEquals(null, CodelistsV2.BidType.getDataMap().get("lala"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap("lala").get("LOT_ALL"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getDataMap(null).get("LOT_ALL"));
    }

    @Test
    public void testGetValueForIdV1() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Main tenderer", CodelistsV1.TenderingRole.getValueForId("MT"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV1.CountryIdentification.getValueForId(null));
        Assert.assertEquals(null, CodelistsV1.TenderingRole.getValueForId("lala"));
    }

    @Test
    public void testGetValueForIdV2() {
        // valid input
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getValueForId("LOT_ALL"));
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR", "ell"));
        Assert.assertEquals("Υποβολή για �?λες τις πα�?τίδες", CodelistsV2.BidType.getValueForId("LOT_ALL", "ell"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV2.CountryIdentification.getValueForId(null));
        Assert.assertEquals(null, CodelistsV2.BidType.getValueForId("lala"));
        Assert.assertEquals("Greece", CodelistsV2.CountryIdentification.getValueForId("GR", null));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType.getValueForId("LOT_ALL", "lala"));
    }

    @Test
    public void testContainsIdV1() {
        // valid input
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsId("GR"));
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsId("GB"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsId(null));
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsId("lala"));
    }

    @Test
    public void testContainsIdV2() {
        // valid input
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GR"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GB"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GR", "ell"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsId("GB", "ell"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId(null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("lala"));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("GR", null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsId("GR", "lala"));
    }

    @Test
    public void testContainsValueV1() {
        // valid input
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsValue("Greece"));
        Assert.assertEquals(true, CodelistsV1.CountryIdentification.containsValue("France"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsValue(null));
        Assert.assertEquals(false, CodelistsV1.CountryIdentification.containsValue("lala"));
    }

    @Test
    public void testContainsValueV2() {
        // valid input
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("Greece"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("France"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("Greece", "bul"));
        Assert.assertEquals(true, CodelistsV2.CountryIdentification.containsValue("France", "deu"));
        // Invalid input
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue(null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("lala"));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("Greece", null));
        Assert.assertEquals(false, CodelistsV2.CountryIdentification.containsValue("Greece", "lala"));
    }

    @Test
    public void testAllGCV1() {
        String theCodelist = "";

        try {

            for (Codelists c : CodelistsV1.values()) {
                theCodelist = c.getConstantName();
                Assert.assertNotNull(!c.getDataMap().isEmpty());
            }

        } catch (Exception e) {
            Assert.fail("Error in CodeList " + theCodelist + ": " + e.getMessage());
        }

    }

    @Test
    public void testAllGCV2() {
        String theCodelist = "";

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

    @Test
    public void test_getDataMapV1() {
        // valid input
        Assert.assertTrue(CodelistsV1.CountryIdentification._getDataMap().isPresent());
        Assert.assertEquals("Greece", CodelistsV1.CountryIdentification._getDataMap().get().get("GR"));
        // Invalid input
        Assert.assertEquals(null, CodelistsV1.CountryIdentification._getDataMap().get().get("lala"));
        Assert.assertEquals(null, CodelistsV1.CountryIdentification._getDataMap().get().get(null));
    }

    @Test
    public void test_getDataMapV2() {
        // valid input
        Assert.assertTrue(CodelistsV2.BidType._getDataMap("ell").isPresent());
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType._getDataMap().get().get("LOT_ALL"));
        Assert.assertEquals("Υποβολή για �?λες τις πα�?τίδες", CodelistsV2.BidType._getDataMap("ell").get().get("LOT_ALL"));

        // Invalid input
        Assert.assertTrue(CodelistsV2.BidType._getDataMap(null).isPresent());
        Assert.assertTrue(CodelistsV2.BidType._getDataMap("lala").isPresent());
        Assert.assertEquals(null, CodelistsV2.BidType._getDataMap().get().get(null));
        Assert.assertEquals(null, CodelistsV2.BidType._getDataMap().get().get("lala"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType._getDataMap("lala").get().get("LOT_ALL"));
        Assert.assertEquals("Submission for all lots", CodelistsV2.BidType._getDataMap(null).get().get("LOT_ALL"));
    }

    @Test
    public void test_getValueForIdV1() {
        // valid input
        Assert.assertEquals(Optional.of("Greece"), CodelistsV1.CountryIdentification._getValueForId("GR"));
        Assert.assertEquals(Optional.of("Main tenderer"), CodelistsV1.TenderingRole._getValueForId("MT"));
        // Invalid input
        Assert.assertEquals(Optional.empty(), CodelistsV1.CountryIdentification._getValueForId(null));
        Assert.assertEquals(Optional.empty(), CodelistsV1.TenderingRole._getValueForId("lala"));
    }

    @Test
    public void test_getValueForIdV2() {
        // valid input
        Assert.assertEquals(Optional.of("Greece"), CodelistsV2.CountryIdentification._getValueForId("GR"));
        Assert.assertEquals(Optional.of("Submission for all lots"), CodelistsV2.BidType._getValueForId("LOT_ALL"));
        Assert.assertEquals(Optional.of("Greece"), CodelistsV2.CountryIdentification._getValueForId("GR", "ell"));
        Assert.assertEquals(Optional.of("Υποβολή για �?λες τις πα�?τίδες"), CodelistsV2.BidType._getValueForId("LOT_ALL", "ell"));
        // Invalid input
        Assert.assertEquals(Optional.empty(), CodelistsV2.CountryIdentification._getValueForId(null));
        Assert.assertEquals(Optional.empty(), CodelistsV2.BidType._getValueForId("lala"));
        Assert.assertEquals(Optional.of("Greece"), CodelistsV2.CountryIdentification._getValueForId("GR", null));
        Assert.assertEquals(Optional.of("Submission for all lots"), CodelistsV2.BidType._getValueForId("LOT_ALL", "lala"));
    }

}
