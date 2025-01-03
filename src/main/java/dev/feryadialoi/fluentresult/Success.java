package dev.feryadialoi.fluentresult;

public record Success<T>(T data) implements Result<T> {
}
