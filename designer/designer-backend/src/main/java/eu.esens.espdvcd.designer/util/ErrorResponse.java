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

    public static class ErrorBuilder {

        //required params
        private int code;
        private String message;

        //optional params
        private Object responseObject;

        private ErrorBuilder(){

        }

        public ErrorBuilder(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public ErrorBuilder withResponseObject(Object responseObject) {
            this.responseObject = responseObject;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
