package app.ewarehouse.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

@Getter
public class CustomGeneralTokenException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    public CustomGeneralTokenException(Set<? extends ConstraintViolation<?>> violations) {
        super("Validation failed");
        this.violations = violations;
    }

    public CustomGeneralTokenException(String message) {
        super(message);
        this.violations = null;
    }

}