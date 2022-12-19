package adventofcode.day17

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .first { it.isNotBlank() }
        .toCharArray()

    data class Coordinate(val x: Long, val y: Long)

    data class Rock(val coordinates: List<Coordinate>) {
        constructor(vararg coordinates: Coordinate) : this(coordinates.toList())

        fun start(startX: Long, startY: Long) = Rock(coordinates.map { it.copy(x = it.x + startX, y = it.y + startY) })
        fun down() = Rock(coordinates.map { it.copy(y = it.y - 1) })
        fun left() = Rock(coordinates.map { it.copy(x = it.x - 1) })
        fun right() = Rock(coordinates.map { it.copy(x = it.x + 1) })
    }


    class Cave(private val input: CharArray) {
        private val shapes = listOf(
            Rock(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0), Coordinate(3, 0)),
            Rock(Coordinate(1, 0), Coordinate(0, 1), Coordinate(1, 1), Coordinate(2, 1), Coordinate(1, 2)),
            Rock(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0), Coordinate(2, 1), Coordinate(2, 2)),
            Rock(Coordinate(0, 0), Coordinate(0, 1), Coordinate(0, 2), Coordinate(0, 3)),
            Rock(Coordinate(0, 0), Coordinate(1, 0), Coordinate(0, 1), Coordinate(1, 1)),
        )
        private val shapeSequence = generateSequence(0) { (it + 1) % shapes.size }.map { idx -> shapes[idx] }
        private val jetIterator = generateSequence(0) { (it + 1) % input.size }.map { idx -> input[idx] }.iterator()

        private val occupied = mutableSetOf<Coordinate>()
        private val startX = 2L
        private var maxHeight = 0L

        fun fallRocks(rocks: Long): Long {
            var fallen = 0L
            shapeSequence.takeWhile { fallen < rocks }.forEach { shape ->
                fallen++

                val maxY = maxHeight
                val startY = maxY + 3

                val position = endingPositionAfterFall(shape.start(startX, startY))
                maxHeight = maxOf(maxHeight, position.coordinates.maxOf { it.y + 1 })

                occupied += position.coordinates

                val fullRow = position.coordinates.map { it.y }.distinct().filter { y ->
                    (0L until 7L).all { x -> Coordinate(x, y) in occupied }
                }.minOrNull()

                if (fullRow != null) {
                    occupied.removeIf { it.y < fullRow }
                }

                if (fallen % 1_000_000L == 0L) println("Fallen ${fallen / 1_000_000L} M")
            }
            return maxHeight
        }

        private fun endingPositionAfterFall(rock: Rock): Rock {
            var current = rock
            while (true) {
                val tryMove = when (jetIterator.next()) {
                    '<' -> current.left()
                    '>' -> current.right()
                    else -> error("invalid")
                }
                if (tryMove.coordinates.all { it.x in 0 until 7 && it !in occupied }) {
                    current = tryMove
                }

                val tryFall = current.down()
                if (tryFall.coordinates.any { it.y < 0 || it in occupied }) {
                    return current
                } else {
                    current = tryFall
                }
            }
        }

    }

    private fun solve(input: CharArray, rocks: Long) = Cave(input).fallRocks(rocks)

    private fun part1(input: CharArray) = solve(input, 2022L)
    private fun part2(input: CharArray) = solve(input, 1000000000000L)

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(3068, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(0, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
