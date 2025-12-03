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
        val lineChars = line.toCharArray()
        val resultArray = charArrayOf(lineChars[0], lineChars[1])

        for(index in 1 until lineChars.lastIndex) {
            if(lineChars[index] > resultArray[0]) {
                resultArray[0] = lineChars[index]
                resultArray[1] = lineChars[index + 1]
            }
            else if(lineChars[index] > resultArray[1]) resultArray[1] = lineChars[index]
        }

        if(lineChars.last() > resultArray[1]) resultArray[1] = lineChars.last()

//        println("Line: $line, biggestFirstNumber: $biggestFirstNumber, biggestSecondNumber: $biggestSecondNumber")
        val maxJoltage = String(resultArray)
//        println("Max joltage: $maxJoltage")

        return maxJoltage.toLong()
    }

    fun calculateTotalJoltages(): Long {
        var totalJoltages = 0L
        readInput().forEach { line -> totalJoltages += calculateBankJoltage(line)}
        return totalJoltages
    }
}

fun main() {
    val calculateTotalJoltages = Day3().calculateTotalJoltages()
    println("Total joltages: $calculateTotalJoltages")
}
