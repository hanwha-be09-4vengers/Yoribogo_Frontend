package com.avengers.yoribogo.common;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.common.exception.ExceptionDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

//필기. 응답 DTO통일
@Data
public class ResponseDTO<T> {

    @JsonIgnore
    private HttpStatus httpStatus;

    @NotNull
    private boolean success;

    @Nullable
    private T data;

    @Nullable
    private ExceptionDTO error;

    // 필기. 기본 생성자
    public ResponseDTO() {
    }

    // 필기. 모든 필드를 받는 생성자
    public ResponseDTO(HttpStatus httpStatus, boolean success, @Nullable T data, @Nullable ExceptionDTO error) {
        this.httpStatus = httpStatus;
        this.success = success;
        this.data = data;
        this.error = error;
    }


    // 필기. static 팩토리 메소드
    public static <T> ResponseDTO<T> ok(T data) {
        return new ResponseDTO<>(
                HttpStatus.OK,
                true,
                data,
                null
        );
    }

    //필기. 에러 발생시의 메세지(Ad)
    public static ResponseDTO<Object> fail(@NotNull CommonException e) {
        return new ResponseDTO<>(
                e.getErrorCode().getHttpStatus(),
                false,
                null,
                ExceptionDTO.of(e.getErrorCode())
        );
    }

    //필기. 400번 에러 처리(프론트엔드)
    public static ResponseDTO<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDTO<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                ExceptionDTO.of(ErrorCode.MISSING_REQUEST_PARAMETER)
        );
    }

    //필기. 500번 에러 처리(서버 내부 오류)
    public static ResponseDTO<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseDTO<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                false,
                null,
                ExceptionDTO.of(ErrorCode.INVALID_PARAMETER_FORMAT)
        );
    }
}