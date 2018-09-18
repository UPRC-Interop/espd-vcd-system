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
package eu.esens.espdvcd.validator;

import java.util.List;

/**
 * Generic edm validator interface.
 *
 * Created by Ulf Lotzmann on 11/05/2016.
 */
public interface ArtefactValidator {

    /**
     * Provides validation result.
     * @return true, if validation was successful
     */
    boolean isValid();

    /**
     * Provides list of validation events.
     * @return list of events where validation was not successful
     */
    List<ValidationResult> getValidationMessages();

    /**
     * Provides filtered list of validation events.
     * @param keyWord, for which the list entries are filtered
     * @return filtered list of validation events
     */
    List<ValidationResult> getValidationMessagesFiltered(String keyWord);

}
