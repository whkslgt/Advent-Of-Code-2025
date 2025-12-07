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

    fun calculateTotalCephalopodSolutionsSum(): Long {
        val lines = readInput().readLines()
        val maxLen = lines.maxOfOrNull { it.length } ?: 0
        val paddedLines = lines.map { it.padEnd(maxLen, ' ') }

        val operationsLine = paddedLines.last()
        val digitLines = paddedLines.dropLast(1)

        val problems = mutableListOf<Pair<Char, List<Long>>>()
        val currentNumbers = mutableListOf<Long>()
        var currentOperator: Char? = null

        for (i in maxLen - 1 downTo 0) {
            val isColEmpty = paddedLines.all { it[i] == ' ' }

            if (isColEmpty) {
                val op = currentOperator!!
                problems.add(Pair(op, currentNumbers.toList()))
                currentNumbers.clear()
                currentOperator = null
            } else {
                val numberStr = digitLines.map { it[i] }
                    .filter { it != ' ' }
                    .joinToString("")

                currentNumbers.add(numberStr.toLong())

                val ch = operationsLine[i]
                if (ch != ' ') {
                    currentOperator = ch
                }
            }
        }

        if (currentNumbers.isNotEmpty()) {
            val op = currentOperator!!
            problems.add(Pair(op, currentNumbers.toList()))
        }

        return problems.fold(0L) { acc, pair ->
            val nums = pair.second
            if (nums.isEmpty()) return@fold acc
            acc + nums.reduce { operationAcc, num ->
                when (pair.first) {
                    '+' -> operationAcc + num
                    '-' -> operationAcc - num
                    '*' -> operationAcc * num
                    '/' -> operationAcc / num
                    else -> operationAcc
                }
            }
        }
    }

    fun calculateTotalProblemSolutionsSum(): Long {
        val (calculationTerms, extractedNumbers) = extractProblems()

        return calculationTerms.foldIndexed(0L) { index, sum, element ->
            sum + extractedNumbers.map { it[index].toInt() }.reduce { acc, num ->
                when (element) {
                    "+" -> acc + num
                    "-" -> acc - num
                    "*" -> acc * num
                    "/" -> acc / num
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
    println("Total part 1: ${Day6().calculateTotalProblemSolutionsSum()}")
    println("Total part 2: ${Day6().calculateTotalCephalopodSolutionsSum()}")
}
