package adventofcode.day01

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01 {
    private fun load(path: String) = getResourceAsText(path)
        .split("\n\n")
        .map { calories ->
            calories.split("\n")
                .filter { it.isNotBlank() }
                .sumOf { it.toInt() }
        }

    private fun part1(caloriesPerElf: List<Int>): Int? {
        return caloriesPerElf.maxOrNull()
    }

    private fun part2(caloriesPerElf: List<Int>): Int {
        return caloriesPerElf.sortedDescending().subList(0, 3).sum()
    }

    @Test
    fun test() {
        val testData = load("/day01/test.txt")

        val part1 = part1(testData)
        assertEquals(24000, part1)

        val part2 = part2(testData)
        assertEquals(45000, part2)
    }

    @Test
    fun run() {
        val inputData = load("/day01/input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
