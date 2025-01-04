package dev.feryadialoi.fluentresult.function;

/**
 * This functional interface is for {@link dev.feryadialoi.fluentresult.Result#ofTry(TryRunnable)}
 * with no return value or void
 */
@FunctionalInterface
public interface TryRunnable {
    void run() throws Exception;
}
