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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.codelist.CodelistsV2;
import eu.esens.espdvcd.designer.exception.LanguageNotExistsException;
import eu.esens.espdvcd.designer.util.CodelistItem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CodelistsV2Service implements CodelistsService {
    INSTANCE;

    public static CodelistsService getInstance() {
        return INSTANCE;
    }
    private final Map<String, List<CodelistItem>> CODELISTS_MAP = new HashMap<>();
    private static final Pattern pattern = Pattern.compile("\\|(.*?)\\)");

    @Override
    public List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException {
        if (CODELISTS_MAP.containsKey(codelist + ".eng"))
            return CODELISTS_MAP.get(codelist + ".eng");
        else {
            Map<String, String> theCodelistMap = CodelistsV2.valueOf(codelist).getDataMap();
            List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
            putToList(theCodelistMap, theCodelistList);
            theCodelistList.sort(Comparator.comparing(CodelistItem::getName));
            CODELISTS_MAP.putIfAbsent(codelist, theCodelistList);
            return theCodelistList;
        }
    }

    @Override
    public Codelists[] getAvailableCodelists() {
        return CodelistsV2.values();
    }

    @Override
    public List<CodelistItem> getTranslatedCodelist(String codelist, String ISO6391Lang) throws IllegalArgumentException, LanguageNotExistsException {
        String language = findISO6393LangCode(ISO6391Lang.toLowerCase());
        if (CODELISTS_MAP.containsKey(codelist + '.' + language))
            return CODELISTS_MAP.get(codelist + '.' + language);
        else {
            Map<String, String> theCodelistMap = CodelistsV2.valueOf(codelist).getDataMap(language);
            List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
            putToList(theCodelistMap, theCodelistList);
            theCodelistList.sort(Comparator.comparing(CodelistItem::getName));
            CODELISTS_MAP.putIfAbsent(codelist + '.' + language, theCodelistList);
            return theCodelistList;
        }
    }

    private void putToList(Map<String, String> theCodelistMap, List<CodelistItem> theCodelistList) {
        theCodelistMap.forEach((key, value) -> {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find())
                theCodelistList.add(new CodelistItem(key, matcher.group(1).trim()));
            else
                theCodelistList.add(new CodelistItem(key, value));
        });
    }
}
