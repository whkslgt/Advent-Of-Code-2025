package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day3Benchmark {

    private lateinit var day3: Day3

    @Setup
    fun setup() {
        day3 = Day3()
    }

    @Benchmark
    fun benchmarkCalculateTotalJoltages(): Long {
        return day3.calculateTotalJoltages()
    }
}
