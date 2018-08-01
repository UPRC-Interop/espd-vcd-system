package eu.esens.espdvcd.retriever.criteria.newretriever.resource;

import eu.esens.espdvcd.model.LegislationReference;
import eu.esens.espdvcd.model.SelectableCriterion;
import eu.esens.espdvcd.model.requirement.RequirementGroup;

public class SelectableCriterionPrinter {

    public static void printSelectableCriterion(SelectableCriterion sc, int index) {

        if (sc != null) {
            printSelectableCriterionBasicInfo(sc, index);
            printLegislationReference(sc.getLegislationReference());
            sc.getRequirementGroups().forEach(rqg -> printRequirementGroup(rqg, 3));
        } else {
            System.out.println(sc);
        }
        System.out.println();
    }

    public static void printSelectableCriterionBasicInfo(SelectableCriterion sc, int index) {

        if (sc != null) {
            System.out.printf("#%-2d\nID: %s\nName: %s\nDesc: %s\nTypeCode: %s\n", index, sc.getID(), sc.getName(), sc.getDescription(), sc.getTypeCode());
        } else {
            System.out.println(sc);
        }

    }

    public static void printLegislationReference(LegislationReference lr) {

        String tab = "\t";

        if (lr != null) {
            System.out.println("\n" + tab + "Title: " + lr.getTitle());
            System.out.println(tab + "Description: " + lr.getDescription());
            System.out.println(tab + "JurisdictionLevelCode: " + lr.getJurisdictionLevelCode());
            System.out.println(tab + "Article: " + lr.getArticle());
            System.out.println(tab + "URI: " + lr.getURI() + "\n");
        } else {
            System.out.println(tab + lr);
        }
    }

    public static void printRequirementGroup(RequirementGroup rg, int depth) {

        String tabs = "";
        for (int i = 0; i < depth; i++) {
            tabs += "\t";
        }
        final String finalTabs = tabs;
        System.out.println(tabs + "RequirementGroup: " + rg.getID() + " Condition: " + rg.getCondition());
        System.out.println(tabs + "Requirements: ");
        rg.getRequirements().forEach(r -> {
            System.out.println(finalTabs + "\tReq ID: " + r.getID() + " Req Type:" + r.getResponseDataType() + " Req Desc:" + r.getDescription());
        });
        final int innerDepth = depth + 1;
        rg.getRequirementGroups().forEach(rg1 -> printRequirementGroup(rg1, innerDepth));
    }

}
