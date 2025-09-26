package ps.demo.jpademo.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ps.demo.jpademo.entity.OperationAuditLog;
import ps.demo.jpademo.repository.OperationAuditLogRepository;

import java.time.Instant;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    @Autowired
    private OperationAuditLogRepository operationAuditLogRepository;

    @Pointcut("execution(* ps.demo.jpademo.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Audit: Before method execution - {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        OperationAuditLog operationAuditLog = new OperationAuditLog();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication != null ? authentication.getName() : "anonymous";
        operationAuditLog.setUserId(userId);

        //operationAuditLog.setUserId("anonymous"); // Replace with actual user ID from security context
        operationAuditLog.setAction(joinPoint.getSignature().getName());
        operationAuditLog.setMethodName(joinPoint.getSignature().toShortString());
        //endpointAuditLog.setParameters(joinPoint.getArgs().toString());
        //endpointAuditLog.setResult(result != null ? result.toString() : "null");
        operationAuditLog.setIpAddress("127.0.0.1"); // Replace with actual IP address
        //endpointAuditLog.setCreatedAt(Instant.now());

        operationAuditLogRepository.save(operationAuditLog);
        log.info("Audit: After method execution - {}", joinPoint.getSignature().getName());
    }
}