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
package eu.esens.espdvcd.schema.adapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by vigi on 11/12/15:3:08 PM.
 */
public final class LocalTimeAdapter {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalTimeAdapter() {

    }

    public static LocalTime unmarshal(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(v, TIME_FORMAT);
    }

    public static String marshal(LocalTime v) {
        if (v == null) {
            return null;
        }
        return v.format(TIME_FORMAT);
    }
}
