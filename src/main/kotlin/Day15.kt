import java.lang.System.lineSeparator
import kotlin.math.abs

typealias Coords = Pair<Int, Int>

object Day15 : PuzzleSolver(15) {

    override fun solve1(input: String): Number {
        val lineRegex = "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex()
        val sensors = input.lines()
            .map { line ->
                lineRegex.find(line)!!.groupValues
                    .let { Sensor(Coords(it[1].toInt(), it[2].toInt()), Beacon(Coords(it[3].toInt(), it[4].toInt()))) }
            }

        return Grid(sensors).coverCoordsCountAt(10) // 10 for test, 2000000 for actual input
    }

    override fun solve2(input: String): Number {
        return 0
    }

    private interface CoordsHolder {
        val coords: Coords
    }

    private fun Coords.manhattanDistance(other: Coords) = abs(other.first - first) + abs(other.second - second)

    private data class Sensor(override val coords: Coords, val closestBeacon: Beacon) : CoordsHolder
    private data class Beacon(override val coords: Coords) : CoordsHolder

    private data class Grid(private val sensors: List<Sensor>) {
        fun coverCoordsCountAt(yToCheck: Int) = sensors
            .map { it to it.coords.manhattanDistance(it.closestBeacon.coords) }
            .filter { (sensor, distance) -> sensor.isInRangeOf(yToCheck, distance) }
            .flatMap { (sensor, distance) ->
                sensor.coords.second.rangeWithDistance(distance)
                    .mapIndexed { i, y -> y to (distance - abs(i - distance)) }
                    .filter { (y,_) -> y == yToCheck}
                    .flatMap { (y, lineDistance) ->
                        sensor.coords.first
                            .rangeWithDistance(lineDistance)
                            .map { x -> Coords(x, y) }
                    }
            }
            .toSet()
            .filterNot { coords -> coords in sensors.map { it.closestBeacon }.map { it.coords } }
            .count { it.second == yToCheck }

        private fun Int.rangeWithDistance(distance: Int) = (this - distance..this + distance)

        private fun Sensor.isInRangeOf(y: Int, distance: Int) = when {
            coords.second < y -> coords.second + distance >= y
            coords.second > y -> coords.second - distance <= y
            else -> true
        }
    }
}
