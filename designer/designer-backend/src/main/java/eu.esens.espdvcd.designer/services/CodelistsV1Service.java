package eu.esens.espdvcd.designer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.designer.models.CodelistItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CodelistsV1Service implements CodelistsService {
    private final Map<String, List<CodelistItem>> CODELISTS_MAP;

    public CodelistsV1Service() {
        CODELISTS_MAP = new HashMap<>();
    }

    @Override
    public List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException {
        if (CODELISTS_MAP.containsKey(codelist))
            return CODELISTS_MAP.get(codelist);
        else {
                Map<String, String> theCodelistMap = CodelistsV1.valueOf(codelist).getDataMap();
                List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
                theCodelistMap.forEach((key, value) -> {
                    theCodelistList.add(new CodelistItem(key, value));
                });
                CODELISTS_MAP.putIfAbsent(codelist, theCodelistList);
                return theCodelistList;
        }
    }

    @Override
    public List<CodelistItem> getTranslatedCodelist(String codelist, String language) {
        throw new UnsupportedOperationException("Not supported for V1 Codelists");
    }
}
