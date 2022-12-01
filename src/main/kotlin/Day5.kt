import kotlin.math.max
import kotlin.math.min

object Day5 : PuzzleSolver(5) {

    override fun solve1(input: String): Number {
        val lines = input.splitToLines()
            .filter { it.straight }
        val maxX = lines.maxOf { max(it.from.x, it.to.x) }
        val maxY = lines.maxOf { max(it.from.y, it.to.y) }

        val multipleLines = (0..maxX).flatMap { x -> (0..maxY).map { y -> x to y } }
            .filter { (x, y) -> lines.count { it.covers(x, y) } > 1 }

        return multipleLines.size
    }

    override fun solve2(input: String): Number {
        val lines = input.splitToLines()
        val maxX = lines.maxOf { max(it.from.x, it.to.x) }
        val maxY = lines.maxOf { max(it.from.y, it.to.y) }

        val multipleLines = (0..maxX).flatMap { x -> (0..maxY).map { y -> x to y } }
            .filter { (x, y) -> lines.count { it.covers(x, y) } > 1 }

        return multipleLines.size
    }

    private fun String.splitToLines() = lines().map { it.toLine() }
    private fun String.toLine() = split(" -> ").let { Line(it[0].toCoordinate(), it[1].toCoordinate()) }
    private fun String.toCoordinate() = split(",").let { Coordinate(it[0].toInt(), it[1].toInt()) }

    private data class Line(val from: Coordinate, val to: Coordinate) {
        val straight get() = from.x == to.x || from.y == to.y

        private val diagonalCoordinatesCovered = lazy { xRange.zip(yRange) }

        fun covers(x: Int, y: Int) = when {
            straight -> (minX..maxX).contains(x) && (minY..maxY).contains(y)
            else -> diagonalCoordinatesCovered.value.contains(x to y)
        }

        private val minX get() = min(from.x, to.x)
        private val maxX get() = max(from.x, to.x)
        private val minY get() = min(from.y, to.y)
        private val maxY get() = max(from.y, to.y)
        private val xRange get() = if (from.x < to.x) from.x..to.x else from.x downTo to.x
        private val yRange get() = if (from.y < to.y) from.y..to.y else from.y downTo to.y
    }

    private data class Coordinate(val x: Int, val y: Int)
}
