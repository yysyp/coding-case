package com.poc.mpt.error;

import com.poc.mpt.common.GenericApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericApiResponse> handleException(Exception ex) {
        log.error("Handle exception, ex={}", ex.getMessage(), ex);

        Class<? extends Throwable> clazz = ex.getClass();
        ExceptionMapping exmp = clazz.getAnnotation(ExceptionMapping.class);
        if (exmp != null) {
            GenericApiResponse resp = GenericApiResponse.error(exmp.code(),
                    exmp.msgReplaceable() && StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : exmp.msg());
            resp.setData(ex.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.valueOf(exmp.httpStatus()));
        }

        GenericApiResponse resp = GenericApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value()+"",
                ex.getMessage());
        resp.setData(ex);
        return new ResponseEntity<>(resp, HttpStatus.valueOf(resp.getCode()));
    }

}
