package gal.dario.advent2025

import kotlin.math.pow

class Day2() {
    private companion object {
        const val INPUT_FILE = "/day-2-input.txt"
    }

    private fun readInput(): List<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .readLine()
            .split(',')
    }

    private fun isInvalidId(s: String): Boolean {
        for (patternLength in 1..s.length / 2) {
            if (s.length % patternLength == 0) {
                val pattern = s.take(patternLength)
                val repetitions = s.length / patternLength

                if (repetitions >= 2 && pattern.repeat(repetitions) == s) {
                    return true
                }
            }
        }
        return false
    }

    private fun generateInvalidIds(start: Long, end: Long): List<Long> {
        val invalidIds = mutableListOf<Long>()

        // Determine the digit length range we need to check
        val minDigits = start.toString().length
        val maxDigits = end.toString().length

        for (numDigits in minDigits..maxDigits) {
            // For each possible pattern length that divides numDigits evenly
            for (patternLength in 1..numDigits / 2) {
                if (numDigits % patternLength == 0) {
                    val repetitions = numDigits / patternLength
                    if (repetitions >= 2) {
                        // Generate all possible patterns of this length
                        val minPattern = 10.0.pow(patternLength - 1).toLong()
                        val maxPattern = 10.0.pow(patternLength).toLong() - 1

                        for (pattern in minPattern..maxPattern) {
                            val patternStr = pattern.toString()
                            if (patternStr.length == patternLength) {
                                val invalidId = patternStr.repeat(repetitions).toLong()
                                if (invalidId in start..end) {
                                    invalidIds.add(invalidId)
                                }
                            }
                        }
                    }
                }
            }
        }

        return invalidIds.distinct()
    }

    private fun sumInvalidIdsInRange(start: Long, end: Long): Long {
        return generateInvalidIds(start, end).sum()
    }

    fun calculateBadIDsSum(): Long {
        return readInput().sumOf { range ->
            val (start, end) = range.split('-').map { it.toLong() }
            sumInvalidIdsInRange(start, end)
        }
    }
}

fun main() {
    val sum = Day2().calculateBadIDsSum()
    println("Sum: $sum")
    println("Sum should be: 43872163557")
}
