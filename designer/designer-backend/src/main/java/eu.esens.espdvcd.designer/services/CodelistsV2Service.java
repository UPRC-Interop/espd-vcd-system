package eu.esens.espdvcd.designer.services;

import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.models.CodelistItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodelistsV2Service implements CodelistsService {
    private final Map<String, List<CodelistItem>> CODELISTS_MAP;

    public CodelistsV2Service() {
        CODELISTS_MAP = new HashMap<>();
    }

    @Override
    public List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException {
        if (CODELISTS_MAP.containsKey(codelist + ".eng"))
            return CODELISTS_MAP.get(codelist + ".eng");
        else {
            Map<String, String> theCodelistMap = CodelistsV2.valueOf(codelist).getDataMap();
            List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
            theCodelistMap.forEach((key, value) -> {
                theCodelistList.add(new CodelistItem(key, value));
            });
            CODELISTS_MAP.putIfAbsent(codelist, theCodelistList);
            return theCodelistList;
        }
    }

    @Override
    public List<CodelistItem> getTranslatedCodelist(String codelist, String language) throws IllegalArgumentException {
        if (CODELISTS_MAP.containsKey(codelist + '.' + language))
            return CODELISTS_MAP.get(codelist + '.' + language);
        else {
            //TODO: CHECK IF LANGUAGE CODE EXISTS BEFORE ADDING TO CODELIST MAP
            Map<String, String> theCodelistMap = CodelistsV2.valueOf(codelist).getDataMap(language);
            List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
            theCodelistMap.forEach((key, value) -> {
                theCodelistList.add(new CodelistItem(key, value));
            });
            CODELISTS_MAP.putIfAbsent(codelist + '.' + language, theCodelistList);
            return theCodelistList;
        }
    }
}
