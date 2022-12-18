package adventofcode.day16

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16 {
    private val regex = """Valve (\w\w) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .mapNotNull {
            val r = regex.find(it)
            r
        }
        .associate { result -> result.groupValues[1] to Valve(result.groupValues[2].toInt(), result.groupValues[3].split(", ")) }

    data class Valve(val flowRate: Int, val others: List<String>)

    data class Route(val v1: String, val v2: String) {
        override fun equals(other: Any?) = (other is Route) && (v1 == other.v1 && v2 == other.v2 || v1 == other.v2 && v2 == other.v1)
    }

    data class Path(val valves: List<String>) {
        val current: String get() = valves.last()
        val distance: Int get() = valves.size
        fun go(other: String) = Path(valves + other)
    }

    data class ParticipantState(val valve: String, val currentMinutes: Int)

    class Tunnel(private val valves: Map<String, Valve>, private val distances: MutableMap<Route, Int> = mutableMapOf()) {
        private val valvesToOpen = valves.filterValues { it.flowRate > 0 }.keys
        private val maxMinutes = 30
        private val start = "AA"

        fun findPressure() = findSolution(participantStates = arrayOf(ParticipantState(start, 0), ParticipantState(start, maxMinutes)))

        fun findPressureWithElephant() = findSolution(participantStates = arrayOf(ParticipantState(start, 4), ParticipantState(start, 4)))

        private fun findSolution(
            participantStates: Array<ParticipantState>,
            todoValves: Set<String> = valvesToOpen,
            currentPressure: Int = 0,
        ): Int {
            return participantStates.indices.flatMap { idx ->
                val participant = participantStates[idx]
                val currentMinutes = participant.currentMinutes

                todoValves.mapNotNull { other ->
                    val route = Route(participant.valve, other)
                    val distance = distances.computeIfAbsent(route) { calculateDistance(route) }
                    val openingMinute = currentMinutes + distance
                    if (openingMinute < 30) {
                        val newParticipantStates = arrayOf(
                            if (idx == 0) ParticipantState(other, openingMinute) else participantStates[0],
                            if (idx == 1) ParticipantState(other, openingMinute) else participantStates[1]
                        )
                        val newTodoValves = todoValves - other
                        val newPressure = currentPressure + calculatePressure(other, openingMinute)

                        findSolution(newParticipantStates, newTodoValves, newPressure)
                    } else {
                        null
                    }
                }
            }.maxOrNull()
                ?: currentPressure
        }


        private fun calculatePressure(valve: String, openingMinute: Int): Int {
            val minutesLeft = maxMinutes - openingMinute
            return valves.getValue(valve).flowRate * minutesLeft
        }

        private fun calculateDistance(route: Route) = paths(route).minOf { it.distance }

        private fun paths(route: Route) = paths(Path(listOf(route.v1)), route.v2)

        private fun paths(path: Path, destination: String): List<Path> {
            val current = path.current
            if (current == destination) {
                return listOf(path)
            }
            return valves.getValue(current).others
                .filter { it !in path.valves }
                .flatMap { other ->
                    paths(path.go(other), destination)
                }
        }
    }

    private fun part1(input: Map<String, Valve>) = Tunnel(input).findPressure()
    private fun part2(input: Map<String, Valve>) = Tunnel(input).findPressureWithElephant()

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(1651, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(1707, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
