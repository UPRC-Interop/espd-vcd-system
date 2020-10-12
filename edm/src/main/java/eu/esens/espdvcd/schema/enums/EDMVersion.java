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

public enum EDMVersion {

    V1(UBLVersion.UBL_V21, "1.x.x"), V2(UBLVersion.UBL_V22, "2.x.x");

    private UBLVersion ublVersion;
    private String tag;


    EDMVersion(UBLVersion ublVersion, String tag) {
        this.ublVersion = ublVersion;
        this.tag = tag;
    }

    public UBLVersion getUblVersion() {
        return ublVersion;
    }

    public String getTag() {
        return tag;
    }

}
