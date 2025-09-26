package ps.demo.jpademo.error;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ps.demo.commonlibx.common.CodeEnum;
import ps.demo.jpademo.dto.BaseErrorResp;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler {//extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseErrorException.class)
    public ResponseEntity<BaseErrorResp> handleDemoServiceException(BaseErrorException ex) {
        log.error("Handle exception, ex={}", ex.getMessage(), ex);
        BaseErrorResp baseErrorResp = new BaseErrorResp(ex);

        return new ResponseEntity<>(baseErrorResp, HttpStatus.valueOf(ex.getCodeEnum().getHttpCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseErrorResp> handleException(Exception ex) {
        log.error("Handle exception, ex={}", ex.getMessage(), ex);
        BaseErrorException baseErrorException = new BaseErrorException(CodeEnum.INTERNAL_SERVER_ERROR, ex);

        BaseErrorResp baseErrorResp = new BaseErrorResp(baseErrorException);

        return new ResponseEntity<>(baseErrorResp, HttpStatus.valueOf(baseErrorException.getCodeEnum().getHttpCode()));
    }


//    // Let Spring handle the exception, we just override the status code
//    @ExceptionHandler(BookNotFoundException.class)
//    public void springHandleNotFound(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.NOT_FOUND.value());
//    }
//
//    @ExceptionHandler(BookUnSupportedFieldPatchException.class)
//    public void springUnSupportedFieldPatch(HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleException(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        Map<String, Object> objectBody = new LinkedHashMap<>();
//        objectBody.put("timestamp", new Date());
//        objectBody.put("status", status.value());
//        objectBody.put("message", ex.getMessage());
//        // Get all errors
//        List<String> exceptionalErrors
//                = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(x -> x.getDefaultMessage())
//                .collect(Collectors.toList());
//
//        objectBody.put("trace", exceptionalErrors);
//
//        return new ResponseEntity<>(objectBody, status);
//        //return super.handleMethodArgumentNotValid(ex, headers, status, request);
//    }


}
