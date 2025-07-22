package ps.demo.jpademo.error;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler {//extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error("Handle exception, ex={}", ex.getMessage(), ex);
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("timestamp", LocalDateTime.now());
        linkedHashMap.put("message", ex.getMessage());
        linkedHashMap.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(linkedHashMap, HttpStatus.BAD_REQUEST);
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
