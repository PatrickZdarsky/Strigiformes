package at.rxcki.strigiformes.exception;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class NamespaceNotFoundException extends RuntimeException {
    public NamespaceNotFoundException(String message) {
        super(message);
    }
}
