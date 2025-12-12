package gal.dario.advent2025

class Day12 {
    companion object {
        const val INPUT_FILE = "/day-12-input.txt"
        const val MAX_SEARCH_STEPS = 1_000_000
        private val SHAPE_HEADER_PATTERN = Regex("\\d+:")
    }

    data class Shape(val cells: List<Pair<Int, Int>>, val width: Int, val height: Int)
    data class Region(val width: Int, val height: Int, val presentCounts: List<Int>)
    data class Placement(val rowMasks: Map<Int, Long>)

    private var searchSteps = 0

    private fun readInput(): List<String> {
        return object {}.javaClass.getResourceAsStream(INPUT_FILE)!!.bufferedReader().readLines()
    }

    private fun parseShapeFromLines(lines: List<String>): List<Pair<Int, Int>> {
        val cells = mutableListOf<Pair<Int, Int>>()
        for (y in lines.indices) {
            for (x in lines[y].indices) {
                if (lines[y][x] == '#') cells.add(x to y)
            }
        }
        return cells
    }

    private fun normalize(cells: List<Pair<Int, Int>>): Shape {
        if (cells.isEmpty()) return Shape(emptyList(), 0, 0)
        val minX = cells.minOf { it.first }
        val minY = cells.minOf { it.second }
        val normalized = cells.map { (x, y) -> (x - minX) to (y - minY) }
        return Shape(normalized, normalized.maxOf { it.first } + 1, normalized.maxOf { it.second } + 1)
    }

    private fun rotate90(cells: List<Pair<Int, Int>>) = cells.map { (x, y) -> y to -x }
    private fun flip(cells: List<Pair<Int, Int>>) = cells.map { (x, y) -> -x to y }

    private fun generateOrientations(baseCells: List<Pair<Int, Int>>): List<Shape> {
        val seen = mutableSetOf<String>()
        val shapes = mutableListOf<Shape>()

        for (version in listOf(baseCells, flip(baseCells))) {
            val rotations = buildList {
                add(version)
                repeat(3) { add(rotate90(last())) }
            }

            for (rotated in rotations) {
                val shape = normalize(rotated)
                val key = shape.cells.sortedWith(compareBy({ it.first }, { it.second })).toString()
                if (key !in seen) {
                    seen.add(key)
                    shapes.add(shape)
                }
            }
        }
        return shapes
    }

    private fun isHeaderLine(s: String) = SHAPE_HEADER_PATTERN.matches(s.trim())

    private fun collectShapeBlock(lines: List<String>, start: Int): Pair<List<String>, Int> {
        var i = start
        val block = mutableListOf<String>()
        while (i < lines.size) {
            val raw = lines[i]
            if (raw.isEmpty() || isHeaderLine(raw)) break
            block.add(raw)
            i++
        }
        return block to i
    }

    private fun parseShapes(lines: List<String>): Pair<List<List<Shape>>, Int> {
        val shapes = mutableListOf<List<Shape>>()
        var idx = 0

        while (idx < lines.size && lines[idx].trim().isEmpty()) idx++

        while (idx < lines.size && isHeaderLine(lines[idx])) {
            val afterHeader = idx + 1
            val (block, nextIdx) = collectShapeBlock(lines, afterHeader)
            shapes.add(generateOrientations(parseShapeFromLines(block)))
            idx = nextIdx
            while (idx < lines.size && lines[idx].trim().isEmpty()) idx++
        }
        return shapes to idx
    }

    private fun parseRegionLine(line: String): Region {
        val (dims, counts) = line.split(": ")
        val (width, height) = dims.split('x').map { it.toInt() }
        val presentCounts = counts.split(' ').map { it.toInt() }
        return Region(width, height, presentCounts)
    }

    private fun parseRegions(lines: List<String>, startIdx: Int): List<Region> {
        val regions = mutableListOf<Region>()
        var idx = startIdx
        while (idx < lines.size) {
            val line = lines[idx].trim()
            if (line.isEmpty()) {
                idx++; continue
            }
            regions.add(parseRegionLine(line))
            idx++
        }
        return regions
    }

    private fun parseInput(lines: List<String>): Pair<List<List<Shape>>, List<Region>> {
        val (shapes, nextIdx) = parseShapes(lines)
        val regions = parseRegions(lines, nextIdx)
        return shapes to regions
    }

    private fun generatePlacementsForShape(orientations: List<Shape>, region: Region): List<Placement> = buildList {
        for (orientation in orientations) {
            for (row in 0..region.height - orientation.height) {
                for (col in 0..region.width - orientation.width) {
                    val cells = orientation.cells.map { (cellX, cellY) -> (col + cellX) to (row + cellY) }
                    val masks = cells.groupBy({ it.second }, { it.first })
                        .mapValues { (_, xPositions) -> xPositions.fold(0L) { mask, xPos -> mask or (1L shl xPos) } }
                    add(Placement(masks))
                }
            }
        }
    }

    private fun canFit(region: Region, shapes: List<List<Shape>>, counts: List<Int>): Boolean {
        val boardArea = region.width * region.height
        val neededArea = counts.withIndex().sumOf { (index, count) -> shapes[index].first().cells.size * count }
        if (neededArea > boardArea) return false

        val placements = shapes.map { generatePlacementsForShape(it, region) }

        searchSteps = 0
        return solve(LongArray(region.height), counts.toMutableList(), placements)
    }

    private fun solve(board: LongArray, remaining: MutableList<Int>, placements: List<List<Placement>>): Boolean {
        if (++searchSteps > MAX_SEARCH_STEPS) return false
        if (remaining.all { it == 0 }) return true

        val shapeIdx = remaining.indexOfFirst { it > 0 }
        if (shapeIdx == -1) return true

        for (placement in placements[shapeIdx]) {
            if (placement.rowMasks.all { (row, mask) -> (board[row] and mask) == 0L }) {
                placement.rowMasks.forEach { (row, mask) -> board[row] = board[row] or mask }
                remaining[shapeIdx]--

                if (solve(board, remaining, placements)) return true

                placement.rowMasks.forEach { (row, mask) -> board[row] = board[row] xor mask }
                remaining[shapeIdx]++
            }
        }
        return false
    }

    fun countRegionsThatFitShapes(): Long {
        val (shapes, regions) = parseInput(readInput())
        return regions.count { canFit(it, shapes, it.presentCounts) }.toLong()
    }
}

fun main() {
    println(Day12().countRegionsThatFitShapes())
}
