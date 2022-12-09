package adventofcode.day09

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.sign
import kotlin.test.assertEquals

class Day09 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { it.first() to it.last().toInt() }

    data class Position(val x: Int, val y: Int) {
        private val right get() = this.copy(x = x + 1)
        private val left get() = this.copy(x = x - 1)
        private val up get() = this.copy(y = y + 1)
        private val down get() = this.copy(y = y - 1)

        fun move(direction: String): Position {
            return when (direction) {
                "U" -> up
                "D" -> down
                "L" -> left
                "R" -> right
                else -> error("unknown direction")
            }
        }

        fun moveTowards(other: Position): Position {
            val dx = (other.x - x).sign
            val dy = (other.y - y).sign
            return this.copy(x = x + dx, y = y + dy)
        }

        fun isAdjacent(other: Position): Boolean {
            return abs(other.x - x) <= 1 && abs(other.y - y) <= 1
        }
    }

    class State(size: Int) {
        private val positions = MutableList(size) { Position(0, 0) }

        private var head: Position
            get() = positions.first()
            set(value) {
                positions[0] = value
            }
        val tail: Position
            get() = positions.last()

        fun move(direction: String) {
            head = head.move(direction)

            for (idx in 1 until positions.size) {
                if (positions[idx].isAdjacent(positions[idx - 1])) {
                    return
                }
                positions[idx] = positions[idx].moveTowards(positions[idx - 1])
            }
        }

    }

    private fun calculateVisited(input: List<Pair<String, Int>>, size: Int): Int {
        val state = State(size)
        val visited = mutableSetOf<Position>()
        input.forEach { (direction, count) ->
            repeat(count) {
                state.move(direction)
                visited += state.tail
            }
        }
        return visited.size
    }

    private fun part1(input: List<Pair<String, Int>>): Int {
        return calculateVisited(input, 2)
    }

    private fun part2(input: List<Pair<String, Int>>): Int {
        return calculateVisited(input, 10)
    }

    @Test
    fun test() {
        val testData = load("/day09/test.txt")

        val part1 = part1(testData)
        assertEquals(13, part1)

        val part2 = part2(testData)
        assertEquals(1, part2)
    }

    @Test
    fun run() {
        val inputData = load("/day09/input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
