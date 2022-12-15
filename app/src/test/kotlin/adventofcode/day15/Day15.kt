package adventofcode.day15

import adventofcode.common.getResourceAsText
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

private const val FREQ_MULTIPLIER = 4000000

class Day15 {
    private val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
    private fun load(path: String) = getResourceAsText(path)
        .split("\n")
        .filter { it.isNotBlank() }
        .mapNotNull { line -> regex.find(line) }
        .map { SensorData(Coordinate(it.groupValues[1].toInt(), it.groupValues[2].toInt()), Coordinate(it.groupValues[3].toInt(), it.groupValues[4].toInt())) }


    data class Coordinate(val x: Int, val y: Int) {
        val frequency get() = x.toLong() * FREQ_MULTIPLIER + y
        fun distanceTo(other: Coordinate): Int {
            val distanceX = maxOf(x, other.x) - minOf(x, other.x)
            val distanceY = maxOf(y, other.y) - minOf(y, other.y)
            return distanceX + distanceY
        }
    }

    data class SensorData(val sensor: Coordinate, val beacon: Coordinate)

    private fun part1(input: List<SensorData>, row: Int): Int {
        val set = mutableSetOf<Int>()

        input.map { data -> blockedCoordinatesInRow(data, row) }
            .forEach { set += it }

        input.flatMap { data -> listOf(data.sensor, data.beacon) }
            .filter { it.y == row }
            .forEach { set -= it.x }

        return set.size
    }

    private fun blockedCoordinatesInRow(sensorData: SensorData, row: Int): IntRange {
        val (sensor, beacon) = sensorData
        val distance = sensor.distanceTo(beacon)
        val distanceY = (sensor.y - row).absoluteValue
        val distanceX = distance - distanceY
        return (sensor.x - distanceX..sensor.x + distanceX)
    }

    private fun IntRange.limit(other: IntRange) = maxOf(first, other.first)..minOf(last, other.last)

    private fun part2(input: List<SensorData>, max: Int): Long {
        val globalRange = 0..max
        globalRange.forEach { row ->
            val ranges = input.map { data -> blockedCoordinatesInRow(data, row).limit(globalRange) }
                .filterNot { it.isEmpty() }
                .sortedBy { it.first }

            if (ranges[0].first > 0) {
                return Coordinate(0, row).frequency
            }

            ranges.fold(0) { acc, it ->
                if (acc + 1 < it.first) {
                    return Coordinate(acc + 1, row).frequency
                }
                maxOf(acc, it.last)
            }
        }
        error("invalid")
    }


    @Test
    fun test() {
        val testData1 = load("test.txt")
        assertEquals(26, part1(testData1, 10))
        val testData2 = load("test.txt")
        assertEquals(56000011, part2(testData2, 20))
    }

    @Test
    fun run() {
        val inputData1 = load("input.txt")
        println("part 1: ${part1(inputData1, 2000000)}")
        val inputData2 = load("input.txt")
        println("part 2: ${part2(inputData2, FREQ_MULTIPLIER)}")
    }
}
