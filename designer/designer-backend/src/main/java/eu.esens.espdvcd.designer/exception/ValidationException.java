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
