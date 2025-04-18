package app.ewarehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import app.ewarehouse.dto.CustomExceptionResponse;



@RestControllerAdvice
public class CustomException {
	
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomExceptionResponse> handlerResourceNotFoundException(ResourceNotFoundException exception) {

          String message = exception.getMessage();
          CustomExceptionResponse response=CustomExceptionResponse.builder().message(message).success(true).status(HttpStatus.NOT_FOUND).build();
       return new ResponseEntity<CustomExceptionResponse>(response,HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(JwtTimeOutException.class)
    public ResponseEntity<CustomExceptionResponse> handlerResourceNotFoundException(JwtTimeOutException exception) {

          String message = exception.getMessage();
          CustomExceptionResponse response=CustomExceptionResponse.builder().message(message).success(true).status(HttpStatus.REQUEST_TIMEOUT).build();
       return new ResponseEntity<CustomExceptionResponse>(response,HttpStatus.REQUEST_TIMEOUT);
    }
    
}


