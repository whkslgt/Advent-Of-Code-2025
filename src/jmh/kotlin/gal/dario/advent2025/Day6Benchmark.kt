package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day6Benchmark {

    private lateinit var day6: Day6

    @Setup
    fun setup() {
        day6 = Day6()
    }

//    @Benchmark
    fun benchmarkCalculateTotalProblemSolutionsSum(): Long {
        return day6.calculateTotalProblemSolutionsSum()
    }

//    @Benchmark
    fun benchmarkCalculateTotalCephalopodSolutionsSum(): Long {
        return day6.calculateTotalCephalopodSolutionsSum()
    }
}
