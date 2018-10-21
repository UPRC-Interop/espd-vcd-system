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

import eu.esens.espdvcd.codelist.enums.EULanguageCodeEnum;
import eu.esens.espdvcd.designer.exception.ValidationException;
import eu.esens.espdvcd.model.ESPDRequest;
import eu.esens.espdvcd.model.ESPDResponse;
import eu.esens.espdvcd.model.SelectableCriterion;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public interface ExportESPDService {
    InputStream exportESPDRequestAsInputStream(ESPDRequest model) throws ValidationException;

    InputStream exportESPDRequestHtmlAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException;

    InputStream exportESPDRequestPdfAsInputStream(ESPDRequest model, EULanguageCodeEnum languageCodeEnum) throws ValidationException;

    String exportESPDRequestAsString(ESPDRequest model) throws ValidationException;

    InputStream exportESPDResponseAsInputStream(ESPDResponse model) throws ValidationException;

    InputStream exportESPDResponseHtmlAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException;

    InputStream exportESPDResponsePdfAsInputStream(ESPDResponse model, EULanguageCodeEnum languageCodeEnum) throws ValidationException;
    
    String exportESPDResponseAsString(ESPDResponse model) throws ValidationException;

    default boolean hasNullCriterion(final List<SelectableCriterion> criteria) {
        return criteria.stream()
                .anyMatch(Objects::isNull);
    }
}
