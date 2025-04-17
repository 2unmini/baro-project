package sample.baro.exception;

import lombok.Getter;

@Getter
public class UserCustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public UserCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
