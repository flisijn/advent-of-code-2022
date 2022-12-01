package adventofcode.day01

import adventofcode.common.getResourceAsText

private fun part1(caloriesPerElf: List<Int>): Int? {
    return caloriesPerElf.maxOrNull()
}

private fun part2(caloriesPerElf: List<Int>): Int {
    return caloriesPerElf.sortedDescending().subList(0, 3).sum()
}

fun main() {
    val input = getResourceAsText("/day01/input.txt")
        .split("\n\n")
        .map { calories ->
            calories.split("\n")
                .filter { it.isNotBlank() }
                .sumOf { it.toInt() }
        }

    val part1 = part1(input)
    println("part 1: $part1")

    val part2 = part2(input)
    println("part 2: $part2")
}
