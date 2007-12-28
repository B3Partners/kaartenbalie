package nl.b3p.kaartenbalie.core.server.accounting;

public class TransactionDeniedException extends Exception{
    
    private static final long serialVersionUID = 9144089171508939394L;
    public TransactionDeniedException() {
        super();
    }
    
    public TransactionDeniedException(String message) {
        super(message);
    }
}
