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

import eu.esens.espdvcd.validator.ValidationResult;

import java.util.List;

public final class Errors {

    //Prevent Class Instantiation
    private Errors() {
    }

    /**
     * Creates a basic error
     *
     * @param code    error code
     * @param message error message
     * @return ErrorResponse object to be serialized in JSON
     */
    public static ErrorResponse standardError(int code, String message) {
        return new ErrorResponse.ErrorBuilder(code, message).build();
    }

//    /**
//     * Creates an error with additional information
//     *
//     * @param code    error code
//     * @param message error message
//     * @param object  error object
//     * @return ErrorResponse object to be serialized in JSON
//     */
//    public static ErrorResponse objectError(int code, String message, Object object) {
//        return new ErrorResponse.ErrorBuilder(code, message).withValidationResults(object).build();
//    }

    /**
     * Creates a 404 - Resource not found error
     *
     * @param message custom message
     * @return ErrorResponse object to be serialized in JSON
     */
    public static ErrorResponse notFoundError(String message) {
        return new ErrorResponse.ErrorBuilder(404, message).build();
    }

    /**
     * Creates a 406 - Input not acceptable error
     *
     * @param message custom message
     * @return ErrorResponse object to be serialized in JSON
     */
    public static ErrorResponse notAcceptableError(String message) {
        return new ErrorResponse.ErrorBuilder(406, message).build();
    }

    /**
     * Codelist not found error
     *
     * @return ErrorResponse with 404 code and message "Codelist not found"
     */
    public static ErrorResponse codelistNotFoundError() {
        return new ErrorResponse.ErrorBuilder(404, "Codelist not found.").build();
    }

    public static ErrorResponse artefactInWrongFormatError() {
        return new ErrorResponse.ErrorBuilder(405, "You need to POST an artefact in json format.").build();
    }

    public static ErrorResponse artefactDeserialisationError() {
        return new ErrorResponse.ErrorBuilder(400, "The provided JSON document was not valid and could not be converted to an object. Did you provide the correct format?").build();
    }

    public static ErrorResponse artefactDeserialisationError(String message) {
        return new ErrorResponse.ErrorBuilder(400, "The provided JSON document was not valid and could not be converted to an object. Did you provide the correct format? \nThis could help you:\n" + message).build();
    }

    public static ErrorResponse unacceptableContentType() {
        return new ErrorResponse.ErrorBuilder(406, "Unacceptable content-type specified.").build();
    }

    public static ErrorResponse retrieverError(String message) {
        return new ErrorResponse.ErrorBuilder(502, "Retriever failed to get criteria. Additional info: \n" + message).build();
    }

    public static ErrorResponse retrieverError() {
        return new ErrorResponse.ErrorBuilder(502, "Retriever failed to get criteria.").build();
    }

    public static ErrorResponse criteriaNotFoundError() {
        return new ErrorResponse.ErrorBuilder(404, "Criteria requested not found.").build();
    }

    public static ErrorResponse validationError(List<ValidationResult> errorList) {
        return new ErrorResponse.ErrorBuilder(406, "Validation failed on the artefact.").withValidationResults(errorList).build();
    }

    public static ErrorResponse validationError(String message, List<ValidationResult> errorList) {
        return new ErrorResponse.ErrorBuilder(406, message).withValidationResults(errorList).build();
    }
}
