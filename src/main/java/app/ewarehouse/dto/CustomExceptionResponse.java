package app.ewarehouse.dto;


import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomExceptionResponse {
    private String message;
    private boolean success;
    private HttpStatus status;
    private Map<String, String> errors;

}
