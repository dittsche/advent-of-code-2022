import kotlin.math.max

object Day17 : PuzzleSolver(17) {

    override fun solve1(input: String) = with(input.toArea()) {
        (1..xRange.last).maxOf { x ->
            (yRange.first..xRange.last).maxOf { y ->
                Trajectory(0 to 0, Velocity(x, y))
                    .takeIf { it.hitsTargetArea(this) }
                    ?.maxY ?: Int.MIN_VALUE
            }
        }
    }

    override fun solve2(input: String) = with(input.toArea()) {
        (1..xRange.last).sumOf { x ->
            (yRange.first..xRange.last)
                .count { y -> Trajectory(0 to 0, Velocity(x, y)).hitsTargetArea(this) }
        }
    }

    private fun String.toArea() = removePrefix("target area: ")
        .split(", ")
        .let { Area(it.parseRange("x"), it.parseRange("y")) }

    private fun List<String>.parseRange(axis: String) = single { it.startsWith("$axis=") }
        .removePrefix("$axis=")
        .split("..")
        .let { IntRange(it[0].toInt(), it[1].toInt()) }

    private data class Point(val x: Int, val y: Int)
    private data class Velocity(val x: Int, val y: Int)

    private data class Trajectory(val start: Point, val initialVelocity: Velocity) {
        private val points = lazy {
            generateSequence(0 to start) { (index, point) ->
                index + 1 to point.move(Velocity(max(initialVelocity.x - index, 0), initialVelocity.y - index))
            }.map { it.second }
        }

        fun hitsTargetArea(targetArea: Area) = points.value
            .takeWhile { it.x <= targetArea.xRange.last && it.y >= targetArea.yRange.first }
            .any { it in targetArea }

        val maxY = points.value.map { it.y }.takeWhile { it >= start.y }
            .maxOf { it }
    }

    private infix fun Int.to(that: Int) = Point(this, that)
    private infix fun Point.move(velocity: Velocity) = x + velocity.x to y + velocity.y

    private data class Area(val xRange: IntRange, val yRange: IntRange) {
        operator fun contains(point: Point) = point.x in xRange && point.y in yRange
    }
}
