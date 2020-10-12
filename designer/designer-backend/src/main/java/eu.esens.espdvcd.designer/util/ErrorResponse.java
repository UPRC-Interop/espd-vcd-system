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
package eu.esens.espdvcd.designer.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.esens.espdvcd.validator.ValidationResult;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int code;
    private final String message;
    private final List<ValidationResult> validationResults;

    private ErrorResponse(ErrorBuilder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.validationResults = builder.validationResults;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public  List<ValidationResult> getValidationResults() {
        return validationResults;
    }

    static class ErrorBuilder {

        //required params
        private int code;
        private String message = "An error has occured. Please contact support providing your ESPD in XML. More info: \n";

        //optional params
        private List<ValidationResult> validationResults;

        private ErrorBuilder(){

        }

        ErrorBuilder(int code, String message) {
            this.code = code;
            this.message += message;
        }

        ErrorBuilder withValidationResults(List<ValidationResult>  validationResults) {
            this.validationResults = validationResults;
            return this;
        }

        ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
