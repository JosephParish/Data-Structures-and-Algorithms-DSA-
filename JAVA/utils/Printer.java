import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Utility class for printing and exporting benchmark reports.
 */
public final class Printer {

    private Printer() {
        throw new AssertionError("Utility class");
    }

    /*=====================================================
     * Single Benchmark
     *=====================================================*/

    public static void print(Benchmark.BenchmarkResult result) {

        long[] samples = result.samples();

        System.out.println("========================================");
        System.out.println("Benchmark Report");
        System.out.println("========================================");

        System.out.printf("Iterations           : %,d%n", samples.length);

        System.out.printf("Average Time         : %.3f ms%n",
                ComplexityAnalyzer.average(samples) / 1_000_000.0);

        System.out.printf("Median Time          : %.3f ms%n",
                ComplexityAnalyzer.median(samples) / 1_000_000.0);

        System.out.printf("Minimum Time         : %.3f ms%n",
                ComplexityAnalyzer.min(samples) / 1_000_000.0);

        System.out.printf("Maximum Time         : %.3f ms%n",
                ComplexityAnalyzer.max(samples) / 1_000_000.0);

        System.out.printf("Std. Deviation       : %.3f ms%n",
                ComplexityAnalyzer.standardDeviation(samples) / 1_000_000.0);

        System.out.println();

        System.out.printf("Throughput           : %.2f ops/sec%n",
                result.operationsPerSecond());

        System.out.printf("Average Memory       : %,d bytes%n",
                result.averageMemoryBytes());

        System.out.printf("Peak Memory          : %,d bytes%n",
                result.peakMemoryBytes());

        System.out.println("========================================");
    }

    /*=====================================================
     * Multiple Benchmarks
     *=====================================================*/

    public static void print(List<Benchmark.BenchmarkResult> results) {

        System.out.println();
        System.out.println("===============================================================");
        System.out.println("Benchmark Summary");
        System.out.println("===============================================================");

        System.out.printf(
                "%-10s %-15s %-15s %-15s%n",
                "Input",
                "Avg(ms)",
                "Memory(KB)",
                "Ops/sec");

        for (Benchmark.BenchmarkResult result : results) {

            double avg =
                    ComplexityAnalyzer.average(result.samples())
                            / 1_000_000.0;

            double memory =
                    result.averageMemoryBytes() / 1024.0;

            System.out.printf(
                    "%-10d %-15.3f %-15.2f %-15.2f%n",
                    result.inputSize(),
                    avg,
                    memory,
                    result.operationsPerSecond()
            );
        }

        System.out.println("===============================================================");
    }

    /*=====================================================
     * Complexity Report
     *=====================================================*/

    public static void print(
            ComplexityAnalyzer.AnalysisReport report) {

        System.out.println();
        System.out.println("========================================");
        System.out.println("Complexity Analysis");
        System.out.println("========================================");

        System.out.printf(
                "Estimated Complexity : %s%n",
                report.estimatedComplexity());

        System.out.printf(
                "Confidence           : %.2f%%%n",
                report.confidence());

        System.out.println();

        printComplexityTable(report.modelErrors());

        System.out.println("========================================");
    }

    /*=====================================================
     * Timing Table
     *=====================================================*/

    public static void printTimingTable(
            List<Benchmark.BenchmarkResult> results) {

        System.out.println();
        System.out.println("Timing Results");

        System.out.printf(
                "%-10s %-12s %-12s %-12s %-12s%n",
                "Input",
                "Min(ms)",
                "Avg(ms)",
                "Median",
                "Max(ms)");

        for (Benchmark.BenchmarkResult result : results) {

            long[] samples = result.samples();

            System.out.printf(
                    "%-10d %-12.3f %-12.3f %-12.3f %-12.3f%n",
                    result.inputSize(),
                    ComplexityAnalyzer.min(samples) / 1_000_000.0,
                    ComplexityAnalyzer.average(samples) / 1_000_000.0,
                    ComplexityAnalyzer.median(samples) / 1_000_000.0,
                    ComplexityAnalyzer.max(samples) / 1_000_000.0
            );
        }
    }

    /*=====================================================
     * Memory Table
     *=====================================================*/

    public static void printMemoryTable(
            List<Benchmark.BenchmarkResult> results) {

        System.out.println();
        System.out.println("Memory Usage");

        System.out.printf(
                "%-10s %-18s %-18s%n",
                "Input",
                "Average(KB)",
                "Peak(KB)");

        for (Benchmark.BenchmarkResult result : results) {

            System.out.printf(
                    "%-10d %-18.2f %-18.2f%n",
                    result.inputSize(),
                    result.averageMemoryBytes() / 1024.0,
                    result.peakMemoryBytes() / 1024.0
            );
        }
    }

    /*=====================================================
     * Complexity Error Table
     *=====================================================*/

    public static void printComplexityTable(
            Map<String, Double> errors) {

        System.out.println("Model Errors");
        System.out.println("-----------------------------");

        for (var entry : errors.entrySet()) {

            System.out.printf(
                    "%-15s %.6f%n",
                    entry.getKey(),
                    entry.getValue());
        }
    }

    /*=====================================================
     * CSV Export
     *=====================================================*/

    public static void exportCSV(
            List<Benchmark.BenchmarkResult> results,
            Path output)
            throws IOException {

        try (PrintWriter writer =
                     new PrintWriter(
                             Files.newBufferedWriter(output))) {

            writer.println(
                    "InputSize,AverageTimeNs,MedianTimeNs,MinTimeNs,MaxTimeNs,AverageMemoryBytes,PeakMemoryBytes,OperationsPerSecond");

            for (Benchmark.BenchmarkResult result : results) {

                long[] samples = result.samples();

                writer.printf(
                        "%d,%.2f,%d,%d,%d,%d,%d,%.4f%n",

                        result.inputSize(),

                        ComplexityAnalyzer.average(samples),

                        ComplexityAnalyzer.median(samples),

                        ComplexityAnalyzer.min(samples),

                        ComplexityAnalyzer.max(samples),

                        result.averageMemoryBytes(),

                        result.peakMemoryBytes(),

                        result.operationsPerSecond()
                );
            }
        }
    }

    /*=====================================================
     * JSON Export
     *=====================================================*/

    public static void exportJSON(
            List<Benchmark.BenchmarkResult> results,
            Path output)
            throws IOException {

        try (PrintWriter writer =
                     new PrintWriter(
                             Files.newBufferedWriter(output))) {

            writer.println("[");
            for (int i = 0; i < results.size(); i++) {

                Benchmark.BenchmarkResult result = results.get(i);

                long[] samples = result.samples();

                writer.println("  {");
                writer.printf("    \"inputSize\": %d,%n",
                        result.inputSize());

                writer.printf("    \"averageTimeNs\": %.2f,%n",
                        ComplexityAnalyzer.average(samples));

                writer.printf("    \"medianTimeNs\": %d,%n",
                        ComplexityAnalyzer.median(samples));

                writer.printf("    \"minTimeNs\": %d,%n",
                        ComplexityAnalyzer.min(samples));

                writer.printf("    \"maxTimeNs\": %d,%n",
                        ComplexityAnalyzer.max(samples));

                writer.printf("    \"averageMemoryBytes\": %d,%n",
                        result.averageMemoryBytes());

                writer.printf("    \"peakMemoryBytes\": %d,%n",
                        result.peakMemoryBytes());

                writer.printf("    \"operationsPerSecond\": %.4f%n",
                        result.operationsPerSecond());

                writer.print("  }");

                if (i < results.size() - 1)
                    writer.println(",");

                else
                    writer.println();
            }

            writer.println("]");
        }
    }
}
