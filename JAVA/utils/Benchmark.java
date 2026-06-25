import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Executes algorithm benchmarks and collects raw timing/memory data.
 *
 * This class does NOT perform statistical analysis or complexity estimation.
 * Those responsibilities belong to ComplexityAnalyzer.
 */
public final class Benchmark {

    private Benchmark() {
        throw new AssertionError("Utility class");
    }

    /**
     * Executes a benchmark for a single input size.
     *
     * @param algorithm     Algorithm to benchmark
     * @param inputSupplier Supplies a NEW input for every iteration
     * @param warmupRuns    JVM warmup iterations
     * @param iterations    Timed iterations
     * @return BenchmarkResult containing raw samples
     */
    public static <T> BenchmarkResult run(
            Consumer<T> algorithm,
            Supplier<T> inputSupplier,
            int warmupRuns,
            int iterations) {

        if (warmupRuns < 0)
            throw new IllegalArgumentException("Warmup runs must be >= 0");

        if (iterations <= 0)
            throw new IllegalArgumentException("Iterations must be > 0");

        warmup(algorithm, inputSupplier, warmupRuns);

        Runtime runtime = Runtime.getRuntime();

        long[] samples = new long[iterations];

        long totalMemory = 0;
        long peakMemory = 0;

        for (int i = 0; i < iterations; i++) {

            T input = inputSupplier.get();

            runtime.gc();

            long beforeMemory = memoryUsage(runtime);

            Sample sample = measure(algorithm, input);

            long afterMemory = memoryUsage(runtime);

            long usedMemory = Math.max(0, afterMemory - beforeMemory);

            samples[i] = sample.timeNs();

            totalMemory += usedMemory;
            peakMemory = Math.max(peakMemory, usedMemory);
        }

        double averageTime = 0;

        for (long sample : samples)
            averageTime += sample;

        averageTime /= iterations;

        double throughput =
                averageTime == 0
                        ? Double.POSITIVE_INFINITY
                        : 1_000_000_000.0 / averageTime;

        return new BenchmarkResult(
                samples,
                totalMemory / iterations,
                peakMemory,
                throughput
        );
    }

    /**
     * Benchmarks several input sizes.
     */
    public static <T> List<BenchmarkResult> runSeries(
            Consumer<T> algorithm,
            Function<Integer, T> generator,
            int[] inputSizes,
            int warmupRuns,
            int iterations) {

        List<BenchmarkResult> results = new ArrayList<>();

        for (int size : inputSizes) {

            BenchmarkResult result = run(
                    algorithm,
                    () -> generator.apply(size),
                    warmupRuns,
                    iterations
            );

            results.add(result);
        }

        return results;
    }

    /**
     * Performs JVM warmup.
     */
    private static <T> void warmup(
            Consumer<T> algorithm,
            Supplier<T> supplier,
            int runs) {

        for (int i = 0; i < runs; i++) {
            algorithm.accept(supplier.get());
        }
    }

    /**
     * Measures one execution.
     */
    private static <T> Sample measure(
            Consumer<T> algorithm,
            T input) {

        long start = System.nanoTime();

        algorithm.accept(input);

        long elapsed = System.nanoTime() - start;

        return new Sample(elapsed);
    }

    /**
     * Returns current heap usage.
     */
    private static long memoryUsage(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Represents one benchmark sample.
     */
    public record Sample(long timeNs) { }

    /**
     * Raw benchmark data.
     *
     * Statistics are intentionally NOT calculated here.
     * ComplexityAnalyzer is responsible for that.
     */
    public record BenchmarkResult(
            long[] samples,
            long averageMemoryBytes,
            long peakMemoryBytes,
            double operationsPerSecond
    ) { }
}
