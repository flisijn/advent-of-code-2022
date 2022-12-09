package adventofcode.day03

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day03 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }


    private fun priorityOfItem(item: Char): Int {
        return when (item) {
            in 'a'..'z' -> item - 'a' + 1
            in 'A'..'Z' -> item - 'A' + 27
            else -> error("invalid char $item")
        }
    }

    private fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val compartiments = line.chunked(line.length / 2).map { it.toSet() }
            val item = compartiments[0].intersect(compartiments[1]).single()
            priorityOfItem(item)
        }
    }

    private fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { bags ->
                val b = bags.map { it.toSet() }
                val item = b[0].intersect(b[1]).intersect(b[2]).single()
                priorityOfItem(item)
            }
    }

    @Test
    fun test() {
        val testData = load("test.txt")

        val part1 = part1(testData)
        assertEquals(157, part1)

        val part2 = part2(testData)
        assertEquals(70, part2)
    }

    @Test
    fun puzzle() {
        val inputData = load("input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
