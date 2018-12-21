/**
 * Copyright 2016-2018 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.retriever.criteria.resource;

import eu.esens.espdvcd.retriever.criteria.resource.utils.TypeCodeUtils;
import eu.esens.espdvcd.schema.enums.EDMVersion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ESPDArtefactResourceTest {

    private ESPDArtefactResource r1;
    private ESPDArtefactResource r2;

    @Before
    public void setUp() {
        r1 = new ESPDArtefactResource(EDMVersion.V1);
        Assert.assertNotNull(r1);
        r2 = new ESPDArtefactResource(EDMVersion.V2);
        Assert.assertNotNull(r2);
    }

    @Ignore
    @Test
    public void testGetFullListV1() {
        SelectableCriterionPrinter.print(r1.getCriterionList());
    }

    @Ignore
    @Test
    public void testGetFullListV2() {
        SelectableCriterionPrinter.print(r2.getCriterionList());
    }

    @Test
    public void testCompareRegulatedCriteriaTypeCodes() {

        Set<String> typeCodeExceptionV1 = new HashSet<>();
        typeCodeExceptionV1.add("CRITERION.SELECTION.ALL_SATISFIED");
        typeCodeExceptionV1.add("CRITERION.OTHER.EO_DATA.MEETS_THE_OBJECTIVE");

        Set<String> artefactResourceV1 = r1.getCriterionList().stream()
                .filter(sc -> !typeCodeExceptionV1.contains(sc.getTypeCode()))
                .map(sc -> sc.getTypeCode())
                .collect(Collectors.toSet());

        Set<String> typeCodeExceptionV2 = new HashSet<>();
        typeCodeExceptionV2.add("CRITERION.OTHER.EO_DATA.REDUCTION_OF_CANDIDATES");
        typeCodeExceptionV2.add("CRITERION.OTHER.EO_DATA.LOTS_TENDERED");

        Set<String> artefactResourceV2 = r2.getCriterionList().stream()
                .filter(sc -> !typeCodeExceptionV2.contains(sc.getTypeCode()))
                .map(sc -> sc.getTypeCode())
                .collect(Collectors.toSet());

        boolean isSame = artefactResourceV1.containsAll(artefactResourceV2)
                && artefactResourceV2.containsAll(artefactResourceV1);

        if (!isSame) {
            System.out.println("TypeCodes that exist in Resource V1 Set and does not exist in Resource V2 Set");
            TypeCodeUtils.printDifferences(artefactResourceV1, artefactResourceV2);
            System.out.println("TypeCodes that exist in Resource V2 Set and does not exist in Resource V1 Set");
            TypeCodeUtils.printDifferences(artefactResourceV2, artefactResourceV1);
        }

        Assert.assertTrue(isSame);
    }

    @Test
    public void testCompareRegulatedCriteriaIDs() {

        Set<String> idExceptionV1 = new HashSet<>();
        idExceptionV1.add("9c70375e-1264-407e-8b50-b9736bc08901");
        idExceptionV1.add("7e7db838-eeac-46d9-ab39-42927486f22d");

        Set<String> artefactResourceV1 = r1.getCriterionList().stream()
                .filter(sc -> !idExceptionV1.contains(sc.getID()))
                .map(sc -> sc.getID())
                .collect(Collectors.toSet());

        Set<String> idExceptionV2 = new HashSet<>();
        idExceptionV2.add("51c39ba9-0444-4967-afe9-36f753b30175");
        idExceptionV2.add("8b9700b7-b13c-41e6-a220-6bbf8d5fab31");

        Set<String> artefactResourceV2 = r2.getCriterionList().stream()
                .filter(sc -> !idExceptionV2.contains(sc.getID()))
                .map(sc -> sc.getID())
                .collect(Collectors.toSet());

        boolean isSame = artefactResourceV1.containsAll(artefactResourceV2)
                && artefactResourceV2.containsAll(artefactResourceV1);

        if (!isSame) {
            System.out.println("IDs that exist in Resource V1 Set and does not exist in Resource V2 Set");
            TypeCodeUtils.printDifferences(artefactResourceV1, artefactResourceV2);
            System.out.println("IDs that exist in Resource V2 Set and does not exist in Resource V1 Set");
            TypeCodeUtils.printDifferences(artefactResourceV2, artefactResourceV1);
        }

        Assert.assertTrue(isSame);
    }

}
