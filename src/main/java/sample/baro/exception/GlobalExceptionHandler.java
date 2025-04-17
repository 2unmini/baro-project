package sample.baro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.baro.dto.response.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ExceptionResponse> handleUserCustomException(UserCustomException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ExceptionResponse.of(e.getErrorCode()));
    }
}
