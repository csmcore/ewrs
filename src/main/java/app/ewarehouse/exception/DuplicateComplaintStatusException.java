package app.ewarehouse.exception;

public class DuplicateComplaintStatusException extends RuntimeException {
    public DuplicateComplaintStatusException(String message) {
        super(message);
    }
}

