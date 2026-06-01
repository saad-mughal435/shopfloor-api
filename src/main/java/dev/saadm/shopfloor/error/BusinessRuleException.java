package dev.saadm.shopfloor.error;

/** Thrown when a request is well-formed but violates a domain rule → HTTP 409. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
