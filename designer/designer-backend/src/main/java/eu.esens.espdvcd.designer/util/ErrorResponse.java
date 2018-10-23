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
package eu.esens.espdvcd.designer.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int code;
    private final String message;
    private final Object responseObject;

    private ErrorResponse(ErrorBuilder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.responseObject = builder.responseObject;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    static class ErrorBuilder {

        //required params
        private int code;
        private String message;

        //optional params
        private Object responseObject;

        private ErrorBuilder(){

        }

        ErrorBuilder(int code, String message) {
            this.code = code;
            this.message = message;
        }

        ErrorBuilder withResponseObject(Object responseObject) {
            this.responseObject = responseObject;
            return this;
        }

        ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
