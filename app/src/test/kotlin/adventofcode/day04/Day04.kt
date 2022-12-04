package adventofcode.day04

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day04 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { line ->
            line.split(",").map { part ->
                val boundaries = part.split("-").map { it.toInt() }
                boundaries[0]..boundaries[1]
            }
        }

    private fun IntRange.containsAll(other: IntRange) = first <= other.first && last >= other.last
    private fun IntRange.containsAny(other: IntRange) = contains(other.first) || contains(other.last)

    private fun part1(lines: List<List<IntRange>>): Int {
        return lines.count { sections ->
            sections[0].containsAll(sections[1]) || sections[1].containsAll(sections[0])
        }
    }

    private fun part2(lines: List<List<IntRange>>): Int {
        return lines.count { sections ->
            sections[0].containsAny(sections[1]) || sections[1].containsAny(sections[0])
        }
    }

    @Test
    fun test() {
        val testData = load("/day04/test.txt")

        val part1 = part1(testData)
        assertEquals(2, part1)

        val part2 = part2(testData)
        assertEquals(4, part2)
    }

    @Test
    fun run() {
        val inputData = load("/day04/input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
