package dev.feryadialoi.fluentresult;

import java.util.function.Function;

public sealed interface Result<T, E extends Exception> permits Failure, Success {

    static <T, E extends Exception> Result<T, E> of(T data) {
        return success(data);
    }

    static <T, E extends Exception> Result<T, E> of(E exception) {
        return failure(exception);
    }

    static <T, E extends Exception> Result<T, E> failure(E exception) {
        return new Failure<>(exception);
    }

    static <T, E extends Exception> Result<T, E> success(T data) {
        return new Success<>(data);
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default <U> Result<U, E> map(Function<T, U> mapper) {
        return switch (this) {
            case Success<T, E>(T data) -> success(mapper.apply(data));
            case Failure<T, E>(E exception) -> failure(exception);
        };
    }

    default <U> Result<U, E> flatMap(Function<T, Result<U, E>> mapper) {
        return switch (this) {
            case Success<T, E>(T data) -> mapper.apply(data);
            case Failure<T, E>(E exception) -> failure(exception);
        };
    }

    default <U> U match(Function<Result<T, E>, U> matcher) {
        return matcher.apply(this);
    }

    default T get() {
        return switch (this) {
            case Success<T, E>(T data) -> data;
            case Failure<T, E>(E exception) -> throw new ResultException(exception);
        };
    }

}
