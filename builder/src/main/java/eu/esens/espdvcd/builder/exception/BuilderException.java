package eu.esens.espdvcd.builder.exception;


public class BuilderException extends Exception {

    private static final long serialVersionUID = -555582912752212641L;

    public BuilderException() {
    }
    
    public BuilderException(String mes) {
        super(mes);
    }
    
    public BuilderException(String mes, Throwable t) {
        super(mes,t);
    }
    
    public BuilderException(Throwable t) {
        super(t);
    }
    
}
