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
package eu.esens.espdvcd.designer.service;

import eu.esens.espdvcd.codelist.Codelists;
import eu.esens.espdvcd.codelist.CodelistsV1;
import eu.esens.espdvcd.designer.util.CodelistItem;

import java.util.*;

public enum CodelistsV1Service implements CodelistsService {
    INSTANCE;

    public static CodelistsService getInstance() {
        return INSTANCE;
    }

    private final Map<String, List<CodelistItem>> CODELISTS_MAP = new HashMap<>();

    @Override
    public List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException {
        if (CODELISTS_MAP.containsKey(codelist))
            return CODELISTS_MAP.get(codelist);
        else {
            Map<String, String> theCodelistMap = CodelistsV1.valueOf(codelist).getDataMap();
            List<CodelistItem> theCodelistList = new ArrayList<>(theCodelistMap.size());
            theCodelistMap.forEach((key, value) -> theCodelistList.add(new CodelistItem(key, value)));
            theCodelistList.sort(Comparator.comparing(CodelistItem::getName));
            CODELISTS_MAP.putIfAbsent(codelist, theCodelistList);
            return theCodelistList;
        }
    }

    @Override
    public List<CodelistItem> getTranslatedCodelist(String codelist, String language) {
        throw new UnsupportedOperationException("Not supported for V1 Codelists");
    }

    @Override
    public Codelists[] getAvailableCodelists() {
        return CodelistsV1.values();
    }

}
