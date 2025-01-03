package dev.feryadialoi.fluentresult;

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

    default <U> U match(Function<Result<T>, U> matcher) {
        return matcher.apply(this);
    }

    default T get() {
        return switch (this) {
            case Success<T>(T data) -> data;
            case Failure<T>(Exception exception) -> throw new ResultException(exception);
        };
    }

}
