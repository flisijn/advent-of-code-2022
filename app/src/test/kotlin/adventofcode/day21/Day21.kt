package adventofcode.day21

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day21 {
    val regexNumber = """(\w+): (\d+)""".toRegex()
    val regexEquation = """(\w+): (\w+) ([+-/*]) (\w+)""".toRegex()

    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .associate { line ->
            listOfNotNull(
                regexNumber.find(line)?.groupValues?.let { it[1] to NumberMonkeyAction(it[2].toLong()) },
                regexEquation.find(line)?.groupValues?.let { it[1] to EquationMonkeyAction(it[2], it[4], it[3]) },
            ).first()
        }

    sealed interface MonkeyAction
    class NumberMonkeyAction(val number: Long) : MonkeyAction
    class EquationMonkeyAction(val lhs: String, val rhs: String, val operation: String) : MonkeyAction

    class Monkeys(monkeyActions: Map<String, MonkeyAction>) {
        private val monkeyActions = monkeyActions.toMutableMap()
        private val dependents = monkeyActions.flatMap { (name, action) ->
            if (action is EquationMonkeyAction) listOf(action.lhs, action.rhs).map { it to name } else emptyList()
        }.groupBy({ it.first }, { it.second })

        fun solve(name: String): Long {
            return when (val action = monkeyActions.getValue(name)) {
                is NumberMonkeyAction -> action.number
                is EquationMonkeyAction -> {
                    solveEquation(action).also { result ->
                        monkeyActions[name] = NumberMonkeyAction(result)
                    }
                }
            }
        }

        fun solveEquation(action: EquationMonkeyAction): Long {
            val lhs = solve(action.lhs)
            val rhs = solve(action.rhs)
            return when (action.operation) {
                "+" -> lhs + rhs
                "-" -> lhs - rhs
                "*" -> lhs * rhs
                "/" -> lhs / rhs
                else -> error("invalid operation")
            }
        }

        fun solve2(name: String): Long {
            val deps = dependents.getValue(name)
            check(deps.size == 1)
            val other = deps[0]
            val action = monkeyActions.getValue(other) as EquationMonkeyAction
            return if (other == "root") {
                val searchedValue = if (action.lhs == name) {
                    action.rhs
                } else if (action.rhs == name) {
                    action.lhs
                } else {
                    error("no lhs and no rhs?")
                }
                solve(searchedValue)
            } else {
                val otherValue = solve2(other)
                monkeyActions[other] = NumberMonkeyAction(otherValue)
                val newEquation = inverseEquation(other, action, name)
                solveEquation(newEquation)
            }
        }

        private fun inverseEquation(other: String, action: EquationMonkeyAction, name: String): EquationMonkeyAction {
            return if (action.lhs == name) {
                when (action.operation) {
                    "+" -> EquationMonkeyAction(other, action.rhs, "-") // other = name + rhs -> name = other - rhs
                    "-" -> EquationMonkeyAction(other, action.rhs, "+") // other = name - rhs -> name = other + rhs
                    "*" -> EquationMonkeyAction(other, action.rhs, "/") // other = name * rhs -> name = other / rhs
                    "/" -> EquationMonkeyAction(other, action.rhs, "*") // other = name / rhs -> name = other * rhs
                    else -> error("invalid operation")
                }
            } else if (action.rhs == name) {
                when (action.operation) {
                    "+" -> EquationMonkeyAction(other, action.lhs, "-") // other = lhs + name -> name = other - lhs
                    "-" -> EquationMonkeyAction(action.lhs, other, "-") // other = lhs - name -> name = lhs - other
                    "*" -> EquationMonkeyAction(other, action.lhs, "/") // other = lhs * name -> name = other / lhs
                    "/" -> EquationMonkeyAction(action.lhs, other, "/") // other = lhs / name -> name = lhs / other
                    else -> error("invalid operation")
                }
            } else {
                error("no lhs and no rhs?")
            }
        }
    }

    private fun part1(input: Map<String, MonkeyAction>) = Monkeys(input).solve("root")
    private fun part2(input: Map<String, MonkeyAction>) = Monkeys(input).solve2("humn")

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(152L, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(301L, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
