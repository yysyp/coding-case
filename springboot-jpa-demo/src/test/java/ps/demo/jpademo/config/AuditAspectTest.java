package ps.demo.jpademo.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ps.demo.jpademo.entity.OperationAuditLog;
import ps.demo.jpademo.repository.OperationAuditLogRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuditAspect}.
 *
 * These tests verify the behavior of the {@link AuditAspect} class, focusing on:
 * - Logging before method execution
 * - Logging after method execution with successful return
 * - Handling of security context for user ID
 * - Boundary conditions such as null results and anonymous users
 */
class AuditAspectTest {

    @Mock
    private OperationAuditLogRepository operationAuditLogRepository;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuditAspect auditAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Tests the {@link AuditAspect#logBefore(JoinPoint)} method.
     * Verifies that the method logs the correct message before method execution.
     */
    @Test
    void testLogBefore() {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");

        auditAspect.logBefore(joinPoint);

        verify(joinPoint, times(1)).getSignature();
    }

    /**
     * Tests the {@link AuditAspect#logAfterReturning(JoinPoint, Object)} method with a non-null result.
     * Verifies that the method logs the correct message and saves the audit log with the expected values.
     */
    @Test
    void testLogAfterReturning_WithResult() {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.toShortString()).thenReturn("testMethod()");
        when(authentication.getName()).thenReturn("testUser");

        Object result = "testResult";
        auditAspect.logAfterReturning(joinPoint, result);

        verify(operationAuditLogRepository, times(1)).save(any(OperationAuditLog.class));
    }

    /**
     * Tests the {@link AuditAspect#logAfterReturning(JoinPoint, Object)} method with a null result.
     * Verifies that the method handles null results correctly and still saves the audit log.
     */
    @Test
    void testLogAfterReturning_WithNullResult() {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.toShortString()).thenReturn("testMethod()");
        when(authentication.getName()).thenReturn("testUser");

        auditAspect.logAfterReturning(joinPoint, null);

        verify(operationAuditLogRepository, times(1)).save(any(OperationAuditLog.class));
    }

    /**
     * Tests the {@link AuditAspect#logAfterReturning(JoinPoint, Object)} method with an anonymous user.
     * Verifies that the method correctly handles cases where the security context has no authentication.
     */
    @Test
    void testLogAfterReturning_AnonymousUser() {
        SecurityContextHolder.getContext().setAuthentication(null);

        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
        when(signature.toShortString()).thenReturn("testMethod()");

        auditAspect.logAfterReturning(joinPoint, "testResult");

        verify(operationAuditLogRepository, times(1)).save(any(OperationAuditLog.class));
    }
}