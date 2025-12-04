package gal.dario.advent2025

class Day3 {
    companion object {
        const val INPUT_FILE = "/day-3-input.txt"
    }

    private fun readInput(): Sequence<String> {
        return object {}.javaClass
            .getResourceAsStream(INPUT_FILE)!!
            .bufferedReader()
            .lineSequence()
    }

    private fun calculateBankJoltage(line: String): Long {
        val toRemove = line.length - 12
        var removed = 0

        return line.fold(mutableListOf<Char>()) { result, char ->
            while (result.isNotEmpty() && result.last() < char && removed < toRemove) {
                result.removeLast()
                removed++
            }
            result.add(char)
            result
        }.take(12).joinToString("").toLong()
    }

    fun calculateTotalJoltages(): Long {
        var totalJoltages = 0L
        readInput().forEach { line -> totalJoltages += calculateBankJoltage(line) }
        return totalJoltages
    }
}

fun main() {
    val calculateTotalJoltages = Day3().calculateTotalJoltages()
    println("Total joltages: $calculateTotalJoltages")
}
