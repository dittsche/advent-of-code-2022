import kotlin.math.abs

class Day15(private val yToCheck: Int, private val searchRange: IntRange) : PuzzleSolver(15) {
    private val lineRegex =
        "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex()

    override fun solve1(input: String) = input.lines()
        .map { it.toSensor() }
        .let { Grid(it) }
        .coveredCoordsCountAt(yToCheck)

    override fun solve2(input: String): Number {
        val grid = input.lines()
            .map { it.toSensor() }
            .let { Grid(it) }

        return grid.findBeacon(searchRange)
            .let { it.x * 4000000L + it.y }
    }

    private fun String.toSensor() = lineRegex.find(this)!!.groupValues
        .drop(1)
        .map { it.toInt() }
        .let { (sx, sy, bx, by) -> Sensor(Coords(sx, sy), Beacon(Coords(bx, by))) }


    private data class Grid(private val sensors: List<Sensor>) {
        fun coveredCoordsCountAt(yToCheck: Int) = sensors
            .filter { it.isInYRangeOf(yToCheck) }
            .flatMap { sensor ->
                sensor.coords.y.rangeWithDistance(sensor.distance)
                    .mapIndexed { i, y -> y to (sensor.distance - abs(i - sensor.distance)) }
                    .filter { (y, _) -> y == yToCheck }
                    .flatMap { (y, lineDistance) ->
                        sensor.coords.x
                            .rangeWithDistance(lineDistance)
                            .map { x -> Coords(x, y) }
                    }
            }
            .toSet()
            .filterNot { coords -> coords in sensors.map { it.closestBeacon }.map { it.coords } }
            .count { it.y == yToCheck }

        fun findBeacon(searchRange: IntRange) = sensors
            .flatMap { sensor -> sensor.boundary.filter { it.x in searchRange && it.y in searchRange } }
            .first { sensors.none { sensor -> sensor.covers(it.x, it.y) } }

        private val Sensor.boundary
            get() = (0..distance).map { coords + Coords(distance - it + 1, -it) } +
                    (0..distance).map { coords + Coords(-it, -distance + it - 1) } +
                    (0..distance).map { coords + Coords(-distance + it - 1, it) } +
                    (0..distance).map { coords + Coords(it, distance - it + 1) }
        private fun Sensor.covers(x: Int, y: Int) = abs(x - this.coords.x) + abs(y - this.coords.y) <= distance

        private fun Int.rangeWithDistance(distance: Int) = (this - distance..this + distance)

        private fun Sensor.isInYRangeOf(y: Int) = when {
            coords.y < y -> coords.y + distance >= y
            coords.y > y -> coords.y - distance <= y
            else -> true
        }
    }

    private data class Coords(val x: Int, val y: Int) {
        operator fun plus(other: Coords) = Coords(x + other.x, y + other.y)
    }

    private data class Sensor(val coords: Coords, val closestBeacon: Beacon) {
        private val _distance = lazy { abs(closestBeacon.coords.x - coords.x) + abs(closestBeacon.coords.y - coords.y) }
        val distance: Int
            get() = _distance.value
    }

    private data class Beacon(val coords: Coords)
}
