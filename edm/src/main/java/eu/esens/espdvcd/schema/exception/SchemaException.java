package eu.esens.espdvcd.schema.exception;

public class SchemaException extends Exception {

    public SchemaException() {
    }

    public SchemaException(String mes) {
        super(mes);
    }

    public SchemaException(String mes, Throwable t) {
        super(mes,t);
    }

    public SchemaException(Throwable t) {
        super(t);
    }

}
