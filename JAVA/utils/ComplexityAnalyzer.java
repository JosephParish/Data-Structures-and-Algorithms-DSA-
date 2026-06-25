import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs statistical analysis and empirical complexity estimation.
 *
 * This class never executes algorithms—it only analyzes BenchmarkResult data.
 */
public final class ComplexityAnalyzer {

    private ComplexityAnalyzer() {
        throw new AssertionError("Utility class");
    }

    /*=====================================================
     * Statistics
     *=====================================================*/

    public static long min(long[] samples) {
        if (samples.length == 0)
            throw new IllegalArgumentException("No samples.");

        long min = Long.MAX_VALUE;

        for (long sample : samples)
            min = Math.min(min, sample);

        return min;
    }

    public static long max(long[] samples) {
        if (samples.length == 0)
            throw new IllegalArgumentException("No samples.");

        long max = Long.MIN_VALUE;

        for (long sample : samples)
            max = Math.max(max, sample);

        return max;
    }

    public static double average(long[] samples) {
        if (samples.length == 0)
            return 0;

        double sum = 0;

        for (long sample : samples)
            sum += sample;

        return sum / samples.length;
    }

    public static long median(long[] samples) {
        if (samples.length == 0)
            throw new IllegalArgumentException("No samples.");

        long[] copy = Arrays.copyOf(samples, samples.length);
        Arrays.sort(copy);

        int middle = copy.length / 2;

        if (copy.length % 2 == 0)
            return (copy[middle] + copy[middle - 1]) / 2;

        return copy[middle];
    }

    public static double standardDeviation(long[] samples) {

        if (samples.length == 0)
            return 0;

        double mean = average(samples);

        double variance = 0;

        for (long sample : samples) {
            variance += Math.pow(sample - mean, 2);
        }

        variance /= samples.length;

        return Math.sqrt(variance);
    }

    public static double throughput(double averageTimeNs) {

        if (averageTimeNs <= 0)
            return Double.POSITIVE_INFINITY;

        return 1_000_000_000.0 / averageTimeNs;
    }

    /*=====================================================
     * Complexity Estimation
     *=====================================================*/

    public static AnalysisReport analyze(
            List<Benchmark.BenchmarkResult> results) {

        int n = results.size();

        if (n < 2)
            throw new IllegalArgumentException(
                    "Need at least two benchmark results.");

        int[] sizes = new int[n];
        double[] times = new double[n];

        for (int i = 0; i < n; i++) {

            Benchmark.BenchmarkResult r = results.get(i);

            /*
             * Assumes BenchmarkResult knows its input size.
             * If your current record doesn't have it,
             * add:
             *
             * int inputSize
             */

            sizes[i] = r.inputSize();
            times[i] = average(r.samples());
        }

        Map<String, Double> errors =
                calculateErrors(sizes, times);

        String complexity = null;
        double bestError = Double.MAX_VALUE;

        for (var entry : errors.entrySet()) {

            if (entry.getValue() < bestError) {
                bestError = entry.getValue();
                complexity = entry.getKey();
            }
        }

        double confidence = confidence(errors);

        return new AnalysisReport(
                complexity,
                confidence,
                errors
        );
    }

    public static Map<String, Double> calculateErrors(
            int[] sizes,
            double[] actualTimes) {

        Map<String, Double> errors =
                new LinkedHashMap<>();

        errors.put("O(1)",
                rmse(actualTimes,
                        fitModel(sizes, x -> 1)));

        errors.put("O(log n)",
                rmse(actualTimes,
                        fitModel(sizes, Math::log)));

        errors.put("O(n)",
                rmse(actualTimes,
                        fitModel(sizes, x -> x)));

        errors.put("O(n log n)",
                rmse(actualTimes,
                        fitModel(sizes,
                                x -> x * Math.log(x))));

        errors.put("O(n²)",
                rmse(actualTimes,
                        fitModel(sizes,
                                x -> x * x)));

        return errors;
    }

    /*=====================================================
     * Internal Helpers
     *=====================================================*/

    private static double confidence(
            Map<String, Double> errors) {

        double best = Double.MAX_VALUE;
        double second = Double.MAX_VALUE;

        for (double error : errors.values()) {

            if (error < best) {
                second = best;
                best = error;
            } else if (error < second) {
                second = error;
            }
        }

        if (second == 0)
            return 100;

        double score =
                (1.0 - (best / second)) * 100;

        return Math.max(0, Math.min(100, score));
    }

    private static double[] fitModel(
            int[] sizes,
            GrowthFunction function) {

        int n = sizes.length;

        double[] model = new double[n];

        for (int i = 0; i < n; i++)
            model[i] = function.apply(sizes[i]);

        double numerator = 0;
        double denominator = 0;

        for (int i = 0; i < n; i++) {
            numerator += actual(model, i) * model[i];
            denominator += model[i] * model[i];
        }

        double scale =
                denominator == 0
                        ? 1
                        : numerator / denominator;

        double[] fitted = new double[n];

        for (int i = 0; i < n; i++)
            fitted[i] = scale * model[i];

        return fitted;
    }

    /**
     * Placeholder used during fitting.
     * The actual values are injected by RMSE.
     */
    private static double actual(double[] values, int index) {
        return values[index];
    }

    private static double rmse(
            double[] actual,
            double[] predicted) {

        double error = 0;

        for (int i = 0; i < actual.length; i++) {

            double diff =
                    actual[i] - predicted[i];

            error += diff * diff;
        }

        return Math.sqrt(error / actual.length);
    }

    @FunctionalInterface
    private interface GrowthFunction {
        double apply(int n);
    }

    /*=====================================================
     * Report
     *=====================================================*/

    public record AnalysisReport(
            String estimatedComplexity,
            double confidence,
            Map<String, Double> modelErrors
    ) {}
}
