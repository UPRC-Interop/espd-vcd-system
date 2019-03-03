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

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.Requirement;
import eu.esens.espdvcd.model.requirement.RequirementGroup;
import eu.esens.espdvcd.retriever.criteria.resource.utils.TaxonomyDataUtils;

import java.util.List;

public class SelectableCriterionPrinter {

    public static void print(SelectableCriterion sc) {

        if (sc != null) {
            printSelectableCriterionBasicInfo(sc);
            printLegislationReference(sc.getLegislationReference());
            sc.getRequirementGroups().forEach(rqg -> printRequirementGroup(rqg, 3));
        } else {
            System.out.println(sc);
        }
        System.out.println();
    }

    public static void print(List<SelectableCriterion> criterionList) {

        int index = 1;

        for (SelectableCriterion sc : criterionList) {
            System.out.printf("CRITERION #%-2d\n", index++);
            print(sc);
        }

    }

    public static void printSelectableCriterionBasicInfo(SelectableCriterion sc) {

        if (sc != null) {
            System.out.printf("ID: %s\nName: %s\nDesc: %s\nTypeCode: %s\nProperty Keys: %s\n", sc.getID(), sc.getName(), sc.getDescription(), sc.getTypeCode(), sc.getPropertyKeyMap().toString());
        }
    }

    public static void printSelectableCriterionBasicInfo(SelectableCriterion sc, int index) {

        if (sc != null) {
            System.out.printf("#%-2d\nID: %s\nName: %s\nDesc: %s\nTypeCode: %s\n", index, sc.getID(), sc.getName(), sc.getDescription(), sc.getTypeCode());
        }
    }

    public static void printLegislationReference(LegislationReference lr) {

        String tab = "\t";

        if (lr != null) {
            System.out.println("\n" + tab + "Title: " + lr.getTitle());
            System.out.println(tab + "Description: " + lr.getDescription());
            System.out.println(tab + "JurisdictionLevelCode: " + lr.getJurisdictionLevelCode());
            System.out.println(tab + "Article: " + lr.getArticle());
            System.out.println(tab + "URI: " + lr.getURI());
        }
    }

    public static void printRequirementGroup(RequirementGroup rg, int depth) {

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println("\n" + tabs + "[" + rg.getType().name() + "] RequirementGroup: " + rg.getID()
                + " Cardinality: " + TaxonomyDataUtils.extractCardinality(rg.isMandatory(), rg.isMultiple())
                + " Condition: " + rg.getCondition());

        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\t[" + r.getType().name() + "] Req ID: " + r.getID()
                    + ", Cardinality: " + TaxonomyDataUtils.extractCardinality(r.isMandatory(), r.isMultiple())
                    + ", Req Type: " + r.getResponseDataType() + ", Req Desc: " + r.getDescription()
                    + ", Code Type: " + r.getResponseValuesRelatedArtefact()
                    + ", Property Keys: " + r.getPropertyKeyMap().toString());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> printRequirementGroup(rg1, innerDepth));
    }

    public static void printRequirement(Requirement rq) {
        System.out.println("\t[" + rq.getType().name() + "] Req ID: " + rq.getID()
                + ", Cardinality: " + TaxonomyDataUtils.extractCardinality(rq.isMandatory(), rq.isMultiple())
                + ", Req Type: " + rq.getResponseDataType() + ", Req Desc: " + rq.getDescription()
                + ", Code Type: " + rq.getResponseValuesRelatedArtefact());
    }


}
