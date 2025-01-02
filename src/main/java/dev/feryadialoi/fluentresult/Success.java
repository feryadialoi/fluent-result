package dev.feryadialoi.fluentresult;

public record Success<T, E extends Exception>(T data) implements Result<T, E> {
}
