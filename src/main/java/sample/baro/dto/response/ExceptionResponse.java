package sample.baro.dto.response;

import sample.baro.exception.ErrorCode;

public record ExceptionResponse(ErrorDetail error) {
    public static ExceptionResponse of(ErrorCode errorCode) {
        return new ExceptionResponse(new ErrorDetail(errorCode.name(), errorCode.getMessage()));
    }

    public record ErrorDetail(String code, String message) {

    }
}
