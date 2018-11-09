/**
 * Copyright 2016-2018 University of Piraeus Research Center
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
import eu.esens.espdvcd.designer.exception.LanguageNotExistsException;
import eu.esens.espdvcd.designer.util.CodelistItem;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface CodelistsService {
    List<CodelistItem> getCodelist(String codelist) throws IllegalArgumentException;

    List<CodelistItem> getTranslatedCodelist(String codelist, String language) throws IllegalArgumentException, LanguageNotExistsException;

    Codelists[] getAvailableCodelists();

    default String findISO6393LangCode(String ISO6391LangCode) throws LanguageNotExistsException {
        for (Locale locale : Locale.getAvailableLocales()){
            if(locale.getLanguage().equals(ISO6391LangCode)){
                return locale.getISO3Language();
            }
        }
        throw new LanguageNotExistsException("Language does not exist.");
    }
}