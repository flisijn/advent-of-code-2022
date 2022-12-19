package adventofcode.day18

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day18 {
    private val regex = """(\d+),(\d+),(\d+)""".toRegex()

    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .map { regex.find(it)!!.groupValues }
        .map { Cube(it[1].toInt(), it[2].toInt(), it[3].toInt()) }

    data class Cube(val x: Int, val y: Int, val z: Int) {
        val connectedCubes: List<Cube>
            get() = listOf(
                copy(x = x + 1), copy(x = x - 1),
                copy(y = y + 1), copy(y = y - 1),
                copy(z = z + 1), copy(z = z - 1)
            )

    }

    private fun part1(input: List<Cube>): Int {
        val lava = Lava(input)
        return lava.surfaceCount()
    }

    class Lava(val input: List<Cube>) {
        private val cubeSet = input.toSet()

        private val xs = input.minOf { it.x }..input.maxOf { it.x }
        private val ys = input.minOf { it.y }..input.maxOf { it.y }
        private val zs = input.minOf { it.z }..input.maxOf { it.z }

        private val steamXs = xs.first - 1..xs.last + 1
        private val steamYs = ys.first - 1..ys.last + 1
        private val steamZs = zs.first - 1..zs.last + 1

        val edgeCube = input.first { it.x == xs.first }

        private val propagatedSteam = mutableSetOf<Cube>()

        fun propagateSteam(steamCube: Cube) {
            val connectedSteam = steamCube.connectedCubes
                .filter { it.x in steamXs && it.y in steamYs && it.z in steamZs }
                .filter { it !in cubeSet }
                .filter { it !in propagatedSteam }

            propagatedSteam.addAll(connectedSteam)
            connectedSteam.forEach { propagateSteam(it) }
        }

        fun surfaceCount() = input.sumOf { cube -> cube.connectedCubes.count { it !in cubeSet } }
        fun surfaceCountReachableBySteam() = input.sumOf { cube -> cube.connectedCubes.count { it in propagatedSteam } }
    }

    private fun part2(input: List<Cube>): Int {
        val lava = Lava(input)
        val startCube = lava.edgeCube
        val startSteam = startCube.copy(x = startCube.x - 1)
        lava.propagateSteam(startSteam)
        return lava.surfaceCountReachableBySteam()
    }

    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(64, part1(testData1))
        val testData2 = load("test.txt")
        assertEquals(58, part2(testData2))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2)}")
    }
}
