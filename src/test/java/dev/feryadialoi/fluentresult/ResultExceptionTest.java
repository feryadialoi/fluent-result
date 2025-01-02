package dev.feryadialoi.fluentresult;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultExceptionTest {
    @Test
    void testResultException() {
        var exception = new ResultException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResultExceptionWithMessage() {
        var exception = new ResultException("message");
        assertNotNull(exception);
        assertEquals("message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResultExceptionWithCause() {
        var exception = new ResultException(new RuntimeException());
        assertNotNull(exception);
        assertEquals("java.lang.RuntimeException", exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void testResultExceptionWithMessageAndCause() {
        var exception = new ResultException("message", new RuntimeException());
        assertNotNull(exception);
        assertEquals("message", exception.getMessage());
        assertNotNull(exception.getCause());
    }
}