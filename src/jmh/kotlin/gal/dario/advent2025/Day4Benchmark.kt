package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day4Benchmark {

    private lateinit var day4: Day4

    @Setup
    fun setup() {
        day4 = Day4()
    }

//    @Benchmark
    fun benchmarkCalculateAccessibleRolls(): Int {
        return day4.calculateAccessibleRolls()
    }
}
