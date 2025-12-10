package gal.dario.advent2025

import com.google.ortools.Loader
import com.google.ortools.sat.CpModel
import com.google.ortools.sat.CpSolver
import com.google.ortools.sat.CpSolverStatus
import com.google.ortools.sat.LinearExpr
import java.util.BitSet

class Day10 {
    companion object {
        const val INPUT_FILE = "/day-10-input.txt"
    }

    data class Machine(val lights: BitSet, val buttons: Set<ButtonWiring>, val joltages: List<Int>)
    data class ButtonWiring(val actionablePositions: List<Int>)

    private val inputLines: List<String> = object {}.javaClass
        .getResourceAsStream(INPUT_FILE)!!
        .bufferedReader()
        .readLines()

    fun part1(): Int = inputLines.sumOf { line ->
        val machine = parseMachine(line)
        findMinimumButtonPressesForLights(machine)
    }

    fun part2(): Int = inputLines.sumOf { line ->
        val machine = parseMachine(line)
        findMinimumButtonPressesForJoltages(machine)
    }

    private fun findMinimumButtonPressesForLights(machine: Machine): Int {
        val totalLights = calculateTotalLights(machine)
        if (totalLights == 0) return 0

        val targetState = buildTargetBitmask(machine.lights, totalLights)
        if (targetState == 0) return 0

        val buttonMasks = machine.buttons.map { button ->
            button.actionablePositions.fold(0) { mask, index -> mask or (1 shl index) }
        }

        return searchMinimumPresses(targetState, buttonMasks, totalLights)
    }

    private fun calculateTotalLights(machine: Machine): Int {
        val maxFromLights = machine.lights.length()
        val maxFromButtons = machine.buttons.maxOfOrNull { button ->
            button.actionablePositions.maxOrNull() ?: -1
        }?.plus(1) ?: 0
        return maxOf(maxFromLights, maxFromButtons)
    }

    private fun buildTargetBitmask(lights: BitSet, totalLights: Int): Int {
        var bitmask = 0
        for (lightIndex in 0 until totalLights) {
            if (lights.get(lightIndex)) {
                bitmask = bitmask or (1 shl lightIndex)
            }
        }
        return bitmask
    }

    private fun searchMinimumPresses(targetState: Int, buttonMasks: List<Int>, totalLights: Int): Int {
        val maxStates = 1 shl totalLights
        val distances = IntArray(maxStates) { -1 }
        val queue = ArrayDeque<Int>()

        distances[0] = 0
        queue.add(0)

        while (queue.isNotEmpty()) {
            val currentState = queue.removeFirst()
            val currentDistance = distances[currentState]

            for (buttonMask in buttonMasks) {
                val nextState = currentState xor buttonMask
                if (distances[nextState] == -1) {
                    distances[nextState] = currentDistance + 1
                    if (nextState == targetState) {
                        return currentDistance + 1
                    }
                    queue.add(nextState)
                }
            }
        }
        return -1
    }

    private fun findMinimumButtonPressesForJoltages(machine: Machine): Int {
        val targetJoltages = machine.joltages.map { it.coerceAtLeast(0) }
        if (targetJoltages.isEmpty()) return 0

        val validButtons = cleanAndDeduplicateButtons(machine.buttons, targetJoltages.size)
        if (validButtons.isEmpty()) {
            return if (targetJoltages.all { it == 0 }) 0 else -1
        }

        if (!allTargetsCovered(targetJoltages, validButtons)) return -1

        Loader.loadNativeLibraries()
        return solveWithConstraintProgramming(targetJoltages, validButtons)
    }

    private fun cleanAndDeduplicateButtons(buttons: Set<ButtonWiring>, joltageCount: Int): List<IntArray> {
        val cleanedButtons = buttons.mapNotNull { button ->
            val validIndices = button.actionablePositions
                .filter { it in 0 until joltageCount }
                .distinct()
                .sorted()
            if (validIndices.isNotEmpty()) validIndices.toIntArray() else null
        }

        return cleanedButtons
            .distinctBy { it.joinToString(",") }
            .map { it.copyOf() }
    }

    private fun allTargetsCovered(targets: List<Int>, buttons: List<IntArray>): Boolean {
        return targets.indices.all { index ->
            targets[index] == 0 || buttons.any { button -> button.contains(index) }
        }
    }

    private fun solveWithConstraintProgramming(targets: List<Int>, buttons: List<IntArray>): Int {
        val model = CpModel()

        val buttonVariables = buttons.map { button ->
            val maxPresses = button.minOf { index -> targets[index] }
            model.newIntVar(0, maxPresses.toLong(), "button")
        }

        for (joltageIndex in targets.indices) {
            val affectingButtons = buttons.indices.mapNotNull { buttonIndex ->
                if (buttons[buttonIndex].contains(joltageIndex)) {
                    buttonVariables[buttonIndex]
                } else null
            }

            if (affectingButtons.isEmpty()) {
                if (targets[joltageIndex] > 0) return -1
            } else {
                model.addEquality(
                    LinearExpr.sum(affectingButtons.toTypedArray()),
                    targets[joltageIndex].toLong()
                )
            }
        }

        model.minimize(LinearExpr.sum(buttonVariables.toTypedArray()))

        val solver = CpSolver()
        solver.parameters.numSearchWorkers = Runtime.getRuntime().availableProcessors()
        val status = solver.solve(model)

        return when (status) {
            CpSolverStatus.OPTIMAL, CpSolverStatus.FEASIBLE -> solver.objectiveValue().toInt()
            else -> -1
        }
    }

    private fun parseMachine(line: String): Machine {
        val parts = line.split(' ')

        val lightsString = parts.first().removeSurrounding("[", "]")
        val lights = BitSet(lightsString.length).apply {
            lightsString.forEachIndexed { index, char ->
                if (char == '#') set(index)
            }
        }

        val joltagesString = parts.last().removeSurrounding("{", "}")
        val joltages = if (joltagesString.isEmpty()) {
            emptyList()
        } else {
            joltagesString.split(',').map { it.toInt() }
        }

        val buttons = parts.drop(1).dropLast(1).map { buttonString ->
            val indicesString = buttonString.removeSurrounding("(", ")")
            val indices = if (indicesString.isEmpty()) {
                emptyList()
            } else {
                indicesString.split(',').map { it.toInt() }
            }
            ButtonWiring(indices)
        }.toSet()

        return Machine(lights, buttons, joltages)
    }
}

fun main() {
    val day10 = Day10()
    println(day10.part1())
    println(day10.part2())
}
