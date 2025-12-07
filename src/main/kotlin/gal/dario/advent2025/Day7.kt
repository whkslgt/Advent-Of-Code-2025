package gal.dario.advent2025

import java.io.BufferedReader
import java.math.BigInteger

class Day7 {
    companion object {
        const val INPUT_FILE = "/day-7-input.txt"
    }

    val inputLines = readInput().readLines()

    fun readInput(): BufferedReader {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
    }

    fun part1(): Int {
        var splits = 0
        var beams = mutableSetOf<Int>()

        val firstLine = inputLines.first()
        val startX = firstLine.indexOf('S')
        beams.add(startX)

        inputLines.drop(1).forEach { line ->
            val nextBeams = mutableSetOf<Int>()

            for (x in beams) {
                when (line[x]) {
                    '.' -> {
                        nextBeams.add(x)
                    }
                    '^' -> {
                        splits += 1
                        nextBeams.add(x - 1)
                        nextBeams.add(x + 1)
                    }
                }
            }
            beams = nextBeams
        }
        return splits
    }

    fun part2(): BigInteger {
        var timelines = mutableMapOf<Int, BigInteger>()

        val firstLine = inputLines.first()
        val startX = firstLine.indexOf('S')
        timelines[startX] = BigInteger.ONE

        inputLines.drop(1).forEach { line ->
            val nextTimelines = mutableMapOf<Int, BigInteger>()

            for ((x, count) in timelines) {
                when (line[x]) {
                    '.' -> {
                        nextTimelines.merge(x, count, BigInteger::add)
                    }
                    '^' -> {
                        nextTimelines.merge(x - 1, count, BigInteger::add)
                        nextTimelines.merge(x + 1, count, BigInteger::add)
                    }
                }
            }
            timelines = nextTimelines
        }

        // The total number of different timelines is the sum of all active paths at the end
        return timelines.values.fold(BigInteger.ZERO, BigInteger::add)
    }
}

fun main() {
    Day7().part1().also(::println)
    Day7().part2().also(::println)
}
