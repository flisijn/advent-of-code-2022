package adventofcode.day02

import adventofcode.common.getResourceAsText
import adventofcode.day02.Day02.Choice.PAPER
import adventofcode.day02.Day02.Choice.ROCK
import adventofcode.day02.Day02.Choice.SCISSORS
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02 {
    enum class Choice(val value: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
    }

    private val winnerToLoserMap = mapOf(
        SCISSORS to PAPER,
        PAPER to ROCK,
        ROCK to SCISSORS
    )

    private val loserToWinnerMap = winnerToLoserMap.entries
        .associate { (k, v) -> v to k }

    inner class Game(private val inputChoice: Choice, private val ownChoice: Choice) {
        fun getScore(): Int {
            val shapePoints = ownChoice.value
            val outcomePoints = when {
                ownChoice == inputChoice -> 3
                winnerToLoserMap[ownChoice] == inputChoice -> 6
                else -> 0
            }
            return shapePoints + outcomePoints
        }
    }

    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { line ->
            val values = line.split(" ")
            Pair(values[0], values[1])
        }

    private fun simpleInput(input: String) = when (input) {
        "A", "X" -> ROCK
        "B", "Y" -> PAPER
        "C", "Z" -> SCISSORS
        else -> error("invalid input")
    }

    private fun xyzToWinTieLose(choice: Choice, xyz: String) = when (xyz) {
        "X" -> winnerToLoserMap.getValue(choice)
        "Y" -> choice
        "Z" -> loserToWinnerMap.getValue(choice)
        else -> error("invalid input")
    }

    private fun part1(games: List<Pair<String, String>>): Int {
        return games.map {
            val inputChoice = simpleInput(it.first)
            val ownChoice = simpleInput(it.second)
            Game(inputChoice, ownChoice)
        }.sumOf { it.getScore() }
    }

    private fun part2(games: List<Pair<String, String>>): Int {
        return games.map {
            val inputChoice = simpleInput(it.first)
            val ownChoice = xyzToWinTieLose(inputChoice, it.second)
            Game(inputChoice, ownChoice)
        }.sumOf { it.getScore() }
    }

    @Test
    fun test() {
        val testData = load("test.txt")

        val part1 = part1(testData)
        assertEquals(15, part1)

        val part2 = part2(testData)
        assertEquals(12, part2)
    }

    @Test
    fun run() {
        val inputData = load("input.txt")

        val part1 = part1(inputData)
        println("part 1: $part1")

        val part2 = part2(inputData)
        println("part 2: $part2")
    }
}
