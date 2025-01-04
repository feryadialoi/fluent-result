package dev.feryadialoi.fluentresult;

import dev.feryadialoi.fluentresult.function.TryRunnable;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testOfData() {
        var result = Result.of("success");
        assertNotNull(result);
        assertInstanceOf(Success.class, result);
    }

    @Test
    void testOfException() {
        var result = Result.of(new NoSuchElementException("failure"));
        assertNotNull(result);
        assertInstanceOf(Failure.class, result);
    }

    @Test
    void testSuccess() {
        var result = Result.success("success");
        assertNotNull(result);
        assertInstanceOf(Success.class, result);
    }

    @Test
    void testFailure() {
        var result = Result.failure(new NoSuchElementException("failure"));
        assertNotNull(result);
        assertInstanceOf(Failure.class, result);
    }

    @Test
    void testOfTryRunSuccess() {
        var result = Result.ofTry(() -> {});
        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    void testOfTryRunFailure() {
        var result = Result.ofTry((TryRunnable) () -> {
            throw new Exception("failure");
        });
        assertNotNull(result);
        assertInstanceOf(Failure.class, result);
    }

    @Test
    void testOfTrySupplySuccess() {
        var result = Result.ofTry(() -> "success");
        assertNotNull(result);
        assertInstanceOf(Success.class, result);
        assertEquals("success", result.get());
    }

    @Test
    void testOfTrySupplyFailure() {
        var result = Result.ofTry(() -> {
            throw new Exception("failure");
        });
        assertNotNull(result);
        assertInstanceOf(Failure.class, result);
    }

    @Test
    void testIsSuccess() {
        var result = Result.success("success");
        assertTrue(result.isSuccess());
    }

    @Test
    void testIsFailure() {
        var result = Result.failure(new NoSuchElementException("failure"));
        assertTrue(result.isFailure());
    }

    @Test
    void testMap() {
        var result = Result.success("success");
        var newResult = result.map(String::toUpperCase);
        assertNotNull(newResult);
        assertInstanceOf(Success.class, newResult);
        assertEquals("SUCCESS", newResult.get());
    }

    @Test
    void testMapFailure() {
        var result = Result.<String>failure(new NoSuchElementException("failure"));
        var newResult = result.map(String::toUpperCase);
        assertNotNull(newResult);
        assertInstanceOf(Failure.class, newResult);
        var resultException = assertThrows(ResultException.class, result::get);
        assertInstanceOf(NoSuchElementException.class, resultException.getCause());
    }

    @Test
    void testFlatMap() {
        var result = Result.success("success");
        var newResult = result.flatMap(data -> Result.success(data.toUpperCase()));
        assertNotNull(newResult);
        assertInstanceOf(Success.class, newResult);
        assertEquals("SUCCESS", newResult.get());
    }

    @Test
    void testFlatMapFailure() {
        var result = Result.<String>failure(new NoSuchElementException("failure"));
        var newResult = result.flatMap(data -> Result.success(data.toUpperCase()));
        assertNotNull(newResult);
        assertInstanceOf(Failure.class, newResult);
        var resultException = assertThrows(ResultException.class, result::get);
        assertInstanceOf(NoSuchElementException.class, resultException.getCause());
    }

    Result<Integer> add(Integer a, Integer b) {
        return Result.success(a + b);
    }

    Result<Integer> subtract(Integer a, Integer b) {
        return Result.success(a - b);
    }

    Result<Integer> multiply(Integer a, Integer b) {
        return Result.success(a * b);
    }

    Result<Integer> divide(Integer a, Integer b) {
        if (b == 0) {
            return Result.failure(new ArithmeticException("divide by zero"));
        }
        return Result.success(a / b);
    }

    @Test
    void testMatch() {
        var finalResult = Result.success("success").match(result -> switch (result) {
            case Success<String>(var data) -> Result.success(data.toUpperCase());
            case Failure<String> failure -> failure;
        });

        assertEquals("SUCCESS", finalResult.get());
    }

    @Test
    void testGet() {
        var result = Result.success("success");
        assertNotNull(result);
        assertInstanceOf(Success.class, result);
        assertEquals("success", result.get());
    }

    @Test
    void testGetThrow() {
        var result = Result.failure(new NoSuchElementException("failure"));
        assertNotNull(result);
        assertInstanceOf(Failure.class, result);
        var resultException = assertThrows(ResultException.class, result::get);
        assertInstanceOf(NoSuchElementException.class, resultException.getCause());
    }

    @Test
    void testFunctionalChaining() {
        var result = add(1, 2)
                .flatMap(data -> multiply(data, 10))
                .flatMap(data -> divide(data, 2))
                .flatMap(data -> subtract(data, 10));

        assertNotNull(result);
        assertEquals(5, result.get());
    }

    @Test
    void testPatternMatching() {
        var result = add(1, 2);

        result = switch (result) {
            case Success<Integer>(var data) -> multiply(data, 10);
            case Failure<Integer> failure -> failure;
        };

        result = switch (result) {
            case Success<Integer>(var data) -> divide(data, 2);
            case Failure<Integer> failure -> failure;
        };

        result = switch (result) {
            case Success<Integer>(var data) -> subtract(data, 10);
            case Failure<Integer> failure -> failure;
        };

        assertEquals(5, result.get());
    }

}
