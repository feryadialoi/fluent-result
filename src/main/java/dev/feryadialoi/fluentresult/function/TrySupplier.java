package dev.feryadialoi.fluentresult.function;

/**
 * This functional interface is for {@link dev.feryadialoi.fluentresult.Result#ofTry(TrySupplier)}
 * with return value
 */
@FunctionalInterface
public interface TrySupplier<T> {
    T get() throws Exception;
}
