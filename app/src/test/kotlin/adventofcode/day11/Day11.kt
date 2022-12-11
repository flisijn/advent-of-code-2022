package adventofcode.day11

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day11 {
    private val regex = """Monkey (\d+):
  Starting items: (.*)
  Operation: new = old (.) (.+)
  Test: divisible by (\d+)
    If true: throw to monkey (\d+)
    If false: throw to monkey (\d+)""".toRegex()

    class Monkey(val items: MutableList<Long>, val op: (old: Long) -> Long, val testValue: Long, val trueMonkey: Int, val falseMonkey: Int)

    private fun load(path: String) = getResourceAsText(path)
        .split("\n\n")
        .map { regex.matchAt(it, 0) }
        .map { checkNotNull(it).groupValues }
        .map { matches ->
            Monkey(
                items = matches[2].split(", ").map { it.toLong() }.toMutableList(),
                op = determineOperation(matches[3], matches[4]),
                testValue = matches[5].toLong(),
                trueMonkey = matches[6].toInt(),
                falseMonkey = matches[7].toInt()
            )
        }

    private fun determineOperation(operation: String, operationValue: String): (Long) -> Long {
        if (operationValue == "old" && operation == "*") {
            return { it * it }
        }
        val value = operationValue.toLong()
        return when (operation) {
            "*" -> {
                { it * value }
            }

            "+" -> {
                { it + value }
            }

            else -> error("invalid operation $operation")
        }
    }

    private fun solve(monkeys: List<Monkey>, divider: Long, rounds: Int): Long {
        val modulo = monkeys.map { it.testValue }.reduce(Long::times)
        val inspections = LongArray(monkeys.size) { 0L }
        for (round in 1..rounds) {
            monkeys.forEachIndexed { index, monkey ->
                inspections[index] = inspections[index] + monkey.items.size
                monkey.items.forEach { item ->
                    val newItem = monkey.op(item) / divider % modulo
                    val other = if (newItem % monkey.testValue == 0L) monkey.trueMonkey else monkey.falseMonkey
                    monkeys[other].items += newItem
                }
                monkey.items.clear()
            }
            if (round <= 20 || round % 1000 == 0) {
                println("Round $round: ${inspections.map { it.toString() }}")
            }
        }
        return inspections
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }


    private fun part1(testData: List<Monkey>) = solve(testData, 3L, 20)

    private fun part2(testData: List<Monkey>) = solve(testData, 1L, 10000)

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(10605, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(2713310158L, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
