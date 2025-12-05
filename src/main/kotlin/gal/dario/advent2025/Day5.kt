package gal.dario.advent2025

import java.util.BitSet

class Day5 {
    companion object {
        const val INPUT_FILE = "/day-5-input.txt"
    }

    private fun readInput(): Sequence<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .lineSequence()
    }

    fun calculateFreshIngredients(): Int {
        val input = readInput().iterator()
        val mergedRanges = ranges(input)

        var totalCount = 0
        while(input.hasNext()) {
            val ingredient = input.next().toLong()
            if (mergedRanges.any { ingredient in it }) {
                totalCount++
            }
        }

        return totalCount
    }

    private fun ranges(input: Iterator<String>): MutableList<LongRange> {
        val ranges = mutableListOf<LongRange>()

        while (input.hasNext()) {
            val line = input.next()
            if (line.isBlank()) break
            val (from, to) = line.split('-').map { it.toLong() }
            ranges.add(LongRange(from, to))
        }

        ranges.sortBy { it.first }

        val mergedRanges = mutableListOf<LongRange>()
        var currentRange = ranges[0]

        for (i in 1 until ranges.size) {
            val nextRange = ranges[i]
            if (nextRange.first <= currentRange.last) {
                currentRange = LongRange(currentRange.first, maxOf(currentRange.last, nextRange.last))
            } else {
                mergedRanges.add(currentRange)
                currentRange = nextRange
            }
        }
        mergedRanges.add(currentRange)
        return mergedRanges
    }

    fun calculatePossibleFreshIngredients(): Long {
        val input = readInput().iterator()
        val mergedRanges = ranges(input)

        return mergedRanges.fold(0L) { acc, range -> acc + (range.last - range.first + 1) }
    }
}

fun main() {
    println("Fresh ingredients: ${Day5().calculateFreshIngredients()}")
    println("Possible fresh ingredients: ${Day5().calculatePossibleFreshIngredients()}")
}
