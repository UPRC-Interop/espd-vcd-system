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
package eu.esens.espdvcd.retriever.criteria.resource.enums;

/**
 * Enum type for cardinality values contained in
 * Exchange Data Model (EDM). Mapping is as follows:
 * <p>
 * 1    : Mandatory = true,  Multiple = false
 * <p>
 * 0..n : Mandatory = false, Multiple = true
 * <p>
 * 0..1 : Mandatory = false, Multiple = false
 * <p>
 * 1..n : Mandatory = true,  Multiple = true
 *
 * @version 2.0.2
 */
public enum CardinalityEnum {

    ONE(true, false),

    ZERO_OR_ONE(false, false),

    ZERO_TO_MANY(false, true),

    ONE_TO_MANY(true, true);

    private final boolean mandatory;
    private final boolean multiple;

    CardinalityEnum(boolean mandatory, boolean multiple) {
        this.mandatory = mandatory;
        this.multiple = multiple;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMultiple() {
        return multiple;
    }

}
