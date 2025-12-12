package gal.dario.advent2025

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(2)
open class Day12Benchmark {

    private lateinit var day12: Day12

    @Setup
    fun setup() {
        day12 = Day12()
    }

    @Benchmark
    fun benchmark(): Long {
        return day12.countRegionsThatFitShapes()
    }
}
