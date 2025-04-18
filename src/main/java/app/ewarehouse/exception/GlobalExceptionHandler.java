package app.ewarehouse.exception;

import app.ewarehouse.dto.CustomExceptionResponse;
import app.ewarehouse.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMessages errorMessages;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleException(Exception e) {
        log.error("Unexpected Error Occurred: {}", e.getMessage(), e);
        CustomExceptionResponse response = CustomExceptionResponse.builder()
                .message(errorMessages.getInternalServerError())
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        CustomExceptionResponse response = CustomExceptionResponse.builder().message(e.getMessage())
                .success(false).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleEntityNotFoundException(CustomEntityNotFoundException e) {
        CustomExceptionResponse response = CustomExceptionResponse.builder()
                .message(e.getMessage()).success(false).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException e) {
        CustomExceptionResponse response = CustomExceptionResponse.builder()
                .message(e.getMessage()).success(false).status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomGeneralException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleValidationExceptions(CustomGeneralException ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex.getViolations() != null) {
            ex.getViolations().forEach(violation -> {
                String fieldName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                errors.put(fieldName, errorMessage);
            });
        } else {
            errors.put("error", ex.getMessage());
            System.out.println(ex.getMessage());
        }
        CustomExceptionResponse response = CustomExceptionResponse.builder().success(false).message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).errors(errors).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(CustomGeneralTokenException.class)
    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ResponseBody
    public ResponseEntity<CustomExceptionResponse> handleAuthenticationRequiredException(JwtTimeOutException e) {
        CustomExceptionResponse response = CustomExceptionResponse.builder()
                .message(e.getMessage()).success(false).status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        return new ResponseEntity<>(response, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
}
