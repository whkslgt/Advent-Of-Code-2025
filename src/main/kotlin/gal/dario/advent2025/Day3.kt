package gal.dario.advent2025

class Day3 {
    companion object {
        const val INPUT_FILE = "/day-3-input.txt"
    }

    private fun readInput(): ByteArray {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .readBytes()
    }

    private fun calculateBankJoltage(input: ByteArray, start: Int, end: Int, stack: ByteArray): Long {
        val length = end - start
        val toRemove = length - 12
        var removed = 0
        var stackSize = 0

        var i = start
        while (i < end) {
            val byte = input[i]
            while (stackSize > 0 && stack[stackSize - 1] < byte && removed < toRemove) {
                stackSize--
                removed++
            }
            stack[stackSize++] = byte
            i++
        }

        var result = 0L
        i = 0
        while (i < 12) {
            result = result * 10 + (stack[i] - 48)
            i++
        }
        return result
    }

    fun calculateTotalJoltages(): Long {
        val input = readInput()
        val stack = ByteArray(128)
        var totalJoltages = 0L
        var lineStart = 0

        var i = 0
        val newline = '\n'.code.toByte()
        while (i < input.size) {
            if (input[i] == newline) {
                if (i > lineStart) {
                    totalJoltages += calculateBankJoltage(input, lineStart, i, stack)
                }
                lineStart = i + 1
            }
            i++
        }

        if (lineStart < input.size) {
            totalJoltages += calculateBankJoltage(input, lineStart, input.size, stack)
        }

        return totalJoltages
    }
}

fun main() {
    val calculateTotalJoltages = Day3().calculateTotalJoltages()
    println("Total joltages: $calculateTotalJoltages")
}
