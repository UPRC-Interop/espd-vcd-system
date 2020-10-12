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
package eu.esens.espdvcd.schema.enums;

/**
 * {@link EDMSubVersion#V102} refers to ESPD Exchange Data Model (EDM) version 1.0.2
 * {@link EDMSubVersion#V200} refers to ESPD Exchange Data Model (EDM) version 2.0.0
 * {@link EDMSubVersion#V201} refers to ESPD Exchange Data Model (EDM) version 2.0.1
 * {@link EDMSubVersion#V202} refers to ESPD Exchange Data Model (EDM) version 2.0.2
 * {@link EDMSubVersion#V210} refers to ESPD Exchange Data Model (EDM) version 2.1.0
 */
public enum EDMSubVersion {

    V102(EDMVersion.V1, "1.0.2"),

    V210(EDMVersion.V2, "2.1.0"),

    V200(EDMVersion.V2, "2.0.0"),

    V201(EDMVersion.V2, "2.0.1"),

    V202(EDMVersion.V2, "2.0.2");

    private EDMVersion version;
    private String tag;

    EDMSubVersion(EDMVersion version, String tag) {
        this.version = version;
        this.tag = tag;
    }

    public EDMVersion getVersion() {
        return version;
    }

    public String getTag() {
        return tag;
    }

}
