package gal.dario.advent2025

import java.io.BufferedReader
import java.util.regex.Pattern

class Day6 {
    companion object {
        const val INPUT_FILE = "/day-6-input.txt"
    }

    fun readInput(): BufferedReader {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
    }

    fun calculateTotalProblemSolutionsSum(): Long {
        val (calculationTerms, extractedNumbers) = extractProblems()

        return calculationTerms.foldIndexed(0L) { index, sum, element ->
            sum + extractedNumbers.map { it[index].toLong() }.reduce { acc, lng ->
                when (element) {
                    "+" -> acc + lng
                    "-" -> acc - lng
                    "*" -> acc * lng
                    "/" -> acc / lng
                    else -> acc
                }
            }
        }
    }

    private fun extractProblems(): Pair<List<String>, List<List<String>>> {
        val lines = readInput().readLines()
        val calculationTerms = lines.drop(lines.size - 1).flatMap {
            it.split(Pattern.compile("\\s+"))
        }

        val extractedNumbers = lines.dropLast(1).map {
            it.trimStart().split(Pattern.compile("\\s+"))
        }
        return Pair(calculationTerms, extractedNumbers)
    }
}

fun main() {
    println("Total: ${Day6().calculateTotalProblemSolutionsSum()}")
}
