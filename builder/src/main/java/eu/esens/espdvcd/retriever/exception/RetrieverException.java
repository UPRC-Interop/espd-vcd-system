package eu.esens.espdvcd.retriever.exception;

/**
 *
 * @author Konstantinos Raptis
 */
public class RetrieverException extends Exception {
    
    public RetrieverException() {
    }
    
    public RetrieverException(String mes) {
        super(mes);
    }
    
    public RetrieverException(String mes, Throwable t) {
        super(mes,t);
    }
    
    public RetrieverException(Throwable t) {
        super(t);
    }
    
}
