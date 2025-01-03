package dev.feryadialoi.fluentresult;

public record Failure<T>(Exception exception) implements Result<T> {
}
