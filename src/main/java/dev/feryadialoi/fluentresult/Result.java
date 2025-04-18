package dev.feryadialoi.fluentresult;

import dev.feryadialoi.fluentresult.function.TryRunnable;
import dev.feryadialoi.fluentresult.function.TrySupplier;

import java.util.function.Function;

public sealed interface Result<T> permits Failure, Success {

    static <T> Result<T> of(T data) {
        return success(data);
    }

    static <T> Result<T> of(Exception exception) {
        return failure(exception);
    }

    static <T> Result<T> failure(Exception exception) {
        return new Failure<>(exception);
    }

    static <T> Result<T> success(T data) {
        return new Success<>(data);
    }

    static Result<Void> ofTry(TryRunnable runnable) {
        try {
            runnable.run();
            return success(null);
        } catch (Exception e) {
            return failure(e);
        }
    }

    static <T> Result<T> ofTry(TrySupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default <U> Result<U> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T>(T data) -> success(mapper.apply(data));
            case Failure<T>(Exception exception) -> failure(exception);
        };
    }

    default <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return switch (this) {
            case Success<T>(T data) -> mapper.apply(data);
            case Failure<T>(Exception exception) -> failure(exception);
        };
    }

    default <U, E extends Exception> Result<U> fold(Function<T, U> successMapper, Function<Exception, E> failureMapper) {
        return switch (this) {
            case Success<T>(T data) -> success(successMapper.apply(data));
            case Failure<T>(Exception exception) -> failure(failureMapper.apply(exception));
        };
    }

    default T get() {
        return switch (this) {
            case Success<T>(T data) -> data;
            case Failure<T>(Exception exception) -> throw new ResultException(exception);
        };
    }

    default T getValue() {
        return switch (this) {
            case Success<T>(T data) -> data;
            case Failure<T>(Exception exception) -> null;
        };
    }

    default Exception getException() {
        return switch (this) {
            case Success<T>(T data) -> null;
            case Failure<T>(Exception exception) -> exception;
        };
    }
}
