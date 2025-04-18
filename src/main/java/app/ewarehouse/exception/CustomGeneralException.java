package app.ewarehouse.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;

@Getter
public class CustomGeneralException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    public CustomGeneralException(Set<? extends ConstraintViolation<?>> violations) {
        super("Validation failed");
        this.violations = violations;
    }

    public CustomGeneralException(String message) {
        super(message);
        this.violations = null;
    }

}