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
package eu.esens.espdvcd.designer.exception;

import eu.esens.espdvcd.validator.ValidationResult;

import java.util.List;

public class ValidationException extends Exception {

    private List<ValidationResult> validationResults;

    public List<ValidationResult> getResults() {
        return validationResults;
    }

    public ValidationException() {    }

    public ValidationException(String mes) {
        super(mes);
    }

    public ValidationException(String mes, List<ValidationResult> validationResults) {
        super(mes);
        this.validationResults = validationResults;
    }

    public ValidationException(String mes, Throwable t) {
        super(mes,t);
    }

    public ValidationException(Throwable t) {
        super(t);
    }
}
