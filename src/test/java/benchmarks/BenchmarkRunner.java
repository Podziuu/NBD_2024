package benchmarks;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(CacheBenchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }
}
