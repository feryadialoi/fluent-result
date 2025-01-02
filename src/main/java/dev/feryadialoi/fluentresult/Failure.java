package dev.feryadialoi.fluentresult;

public record Failure<T, E extends Exception>(E exception) implements Result<T, E> {
}
