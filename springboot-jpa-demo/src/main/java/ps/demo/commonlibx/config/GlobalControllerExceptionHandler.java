package ps.demo.commonlibx.config;


import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import ps.demo.commonlibx.common.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    public ResponseEntity<BaseResponse> constructResponseEntity(CodeEnum codeEnum, Exception e) {
        BaseResponse errorResponse = BaseResponse.of(codeEnum);
        errorResponse.setDetail(e.getMessage());
        errorResponse.setTraceId(MDC.get(ProjConstant.traceId));
        errorResponse.setSpanId(MDC.get(ProjConstant.spanId));
        HttpStatus httpStatus = HttpStatus.valueOf(codeEnum.getHttpCode());
        return new ResponseEntity<BaseResponse>(errorResponse, httpStatus);
    }

    public ResponseEntity<BaseResponse> constructResponseEntity(Exception e) {
        if (e instanceof ClientErrorException) {
            return constructResponseEntity(((ClientErrorException)e).getCodeEnum(), e);
        } else if (e instanceof ServerErrorException) {
            return constructResponseEntity(((ServerErrorException)e).getCodeEnum(), e);
        }
        return constructResponseEntity(CodeEnum.INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler(value = ServletRequestBindingException.class)
    public ResponseEntity<BaseResponse> handleException(ServletRequestBindingException e) {
        log.error("ServletRequestBindingException handling, message={}", e.getMessage(), e);
        return constructResponseEntity(CodeEnum.BAD_REQUEST, e);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse> handleException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException handling, message={}", e.getMessage(), e);
        return constructResponseEntity(CodeEnum.BAD_REQUEST, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("MethodArgumentNotValidException handling, message={}", ex.getMessage(), ex);
        BaseResponse errorResponse = BaseResponse.of(CodeEnum.BAD_REQUEST);
        HttpStatus httpStatus = HttpStatus.valueOf(CodeEnum.BAD_REQUEST.getHttpCode());
        errorResponse.setMessage(errors.toString());
        return new ResponseEntity<BaseResponse>(errorResponse, httpStatus);
    }


    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<BaseResponse> handleThrowable(RequestNotPermitted e) {
        log.error("Exception handleThrowable, RequestNotPermitted message={}", e.getMessage(), e);
        return constructResponseEntity(CodeEnum.CALL_LATER, e);
    }

    @ExceptionHandler(TaskRejectedException.class)
    public ResponseEntity<BaseResponse> handleThrowable(TaskRejectedException e) {
        log.error("Exception handleThrowable, TaskRejectedException message={}", e.getMessage(), e);
        return constructResponseEntity(CodeEnum.CALL_LATER, e);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleThrowable(Exception e) {
        log.error("Exception handleThrowable, message={}", e.getMessage(), e);
        return constructResponseEntity(e);
    }

}