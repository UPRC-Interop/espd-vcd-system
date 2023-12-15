/**
 * Copyright 2016-2020 University of Piraeus Research Center
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.esens.espdvcd.codelist.enums;

/**
 * The ResponseTypeEnum provides a compile time enumeration of the available
 * response types.
 *
 * @version 2.0.1
 * @since 1.0
 */
public enum ResponseTypeEnum {

    INDICATOR,                          // ResponseDataType v1, v2 codelist - REGULATED
    DATE,                               // ResponseDataType v1, v2 codelist - REGULATED
    DESCRIPTION,                        // ResponseDataType v1, v2 codelist - REGULATED
    EVIDENCE_URL,                       // ResponseDataType v1     codelist - REGULATED
    QUANTITY,                           // ResponseDataType v1, v2 codelist - REGULATED
    QUANTITY_YEAR,                      // ResponseDataType v1, v2 codelist - REGULATED
    QUANTITY_INTEGER,                   // ResponseDataType v1, v2 codelist - REGULATED
    AMOUNT,                             // ResponseDataType v1, v2 codelist - REGULATED
    CODE_COUNTRY,                       // ResponseDataType v1, v2 codelist - REGULATED
    PERCENTAGE,                         // ResponseDataType v1, v2 codelist - REGULATED
    PERIOD,                             // ResponseDataType v1, v2 codelist - REGULATED
    CODE,                               // ResponseDataType v1, v2 codelist - REGULATED
    EVIDENCE_IDENTIFIER,                // ResponseDataType     v2 codelist - REGULATED (v1 {EVIDENCE_URL, CODE, DESCRIPTION})
    NONE,                               // ResponseDataType     v2 codelist - SELF CONTAINED
    IDENTIFIER,                         // ResponseDataType     v2 codelist - REGULATED
    URL,                                // ResponseDataType     v2 codelist - REGULATED
    MAXIMUM_AMOUNT,                     // ResponseDataType     v2 codelist - SELF CONTAINED
    MINIMUM_AMOUNT,                     // ResponseDataType     v2 codelist - SELF CONTAINED
    MAXIMUM_VALUE_NUMERIC,              // ResponseDataType     v2 codelist - SELF CONTAINED
    MINIMUM_VALUE_NUMERIC,              // ResponseDataType     v2 codelist - SELF CONTAINED
    TRANSLATION_TYPE_CODE,              // ResponseDataType     v2 codelist - SELF CONTAINED
    CERTIFICATION_LEVEL_DESCRIPTION,    // ResponseDataType     v2 codelist - SELF CONTAINED
    COPY_QUALITY_TYPE_CODE,             // ResponseDataType     v2 codelist - SELF CONTAINED
    TIME,                               // ResponseDataType     v2 codelist - SELF CONTAINED
    WEIGHT_INDICATOR,                   // EXTRA ResponseDataType     v2 codelist - SELF CONTAINED
    LOT_IDENTIFIER,                     // EXTRA ResponseDataType     v2 codelist - SELF CONTAINED
    ECONOMIC_OPERATOR_IDENTIFIER,       // EXTRA ResponseDataType     v2 codelist - SELF CONTAINED
    CODE_BOOLEAN,                       // EXTRA ResponseDataType     v2 codelist - SELF CONTAINED
}
