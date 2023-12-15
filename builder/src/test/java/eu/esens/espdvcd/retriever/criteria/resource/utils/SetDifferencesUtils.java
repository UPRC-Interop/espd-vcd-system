/**
 * Copyright 2016-2020 University of Piraeus Research Center
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
package eu.esens.espdvcd.retriever.criteria.resource.utils;

import java.util.Set;

public class SetDifferencesUtils {

    public static void printAllDifferences(Set<String> set1, Set<String> set2) {
        System.out.println("Values that exist in set1 and does not exist in set2");
        printDifferences(set1, set2);
        System.out.println("Values that exist in set2 and does not exist in set1");
        printDifferences(set2, set1);
    }

    public static void printDifferences(Set<String> set1, Set<String> set2) {

        set1.forEach(String -> {

            if (!set2.contains(String)) {
                System.out.println(String);
            }

        });
    }

}
