package sample.baro.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.baro.dto.response.ExceptionResponse;

import static sample.baro.exception.ErrorCode.INVALID_CREDENTIALS;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ExceptionResponse> handleUserCustomException(UserCustomException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ExceptionResponse.of(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleUserCustomException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(e.getStatusCode()).body(ExceptionResponse.of(INVALID_CREDENTIALS));
    }
}
