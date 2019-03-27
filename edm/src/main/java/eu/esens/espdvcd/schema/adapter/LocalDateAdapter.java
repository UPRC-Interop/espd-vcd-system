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
package eu.esens.espdvcd.schema.adapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author vigi
 */
public final class LocalDateAdapter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private LocalDateAdapter() {

    }

    public static LocalDate unmarshal(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(v, DATE_FORMAT);
    }

    public static String marshal(LocalDate v) {
        if (v == null) {
            return null;
        }
        return v.format(DATE_FORMAT);
    }
}
