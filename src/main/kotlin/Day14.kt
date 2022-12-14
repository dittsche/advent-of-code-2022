import java.lang.System.lineSeparator
import kotlin.math.max
import kotlin.math.min

object Day14 : PuzzleSolver(14) {

    override fun solve1(input: String) = with(input.toCave()) {
        generateSequence { pourGrain().takeIf { isHigherThanMaxY(it) } }.count()
    }

    override fun solve2(input: String) = with(input.toCave().withFloor()) {
        generateSequence { pourGrain().takeUnless { isSource(it) } }.count() + 1
    }

    private fun String.toCave() =
        lines()
            .flatMap { it.toRockCoords() }
            .distinct()
            .let { Cave(it) }

    private fun String.toRockCoords() = split(" -> ")
        .map { it.toCoords() }
        .windowed(2)
        .flatMap { (first, second) ->
            if (first.x == second.x) {
                (min(first.y, second.y)..max(first.y, second.y))
                    .map { Coords(first.x, it) }
            } else {
                (min(first.x, second.x)..max(first.x, second.x))
                    .map { Coords(it, first.y) }
            }
        }

    private fun String.toCoords() = split(',').map { it.toInt() }.let { (x, y) -> Coords(x, y) }

    private data class Coords(val x: Int, val y: Int) {
        operator fun plus(other: Coords) = Coords(x + other.x, y + other.y)
    }

    private class Cave(private val rocks: List<Coords>) {
        private val grains = mutableListOf<Coords>()
        private val minX = (rocks + grains).minOf { it.x }
        private val maxX = (rocks + grains).maxOf { it.x }
        private val minY = 0
        private val maxY = (rocks + grains).maxOf { it.y }
        private val source = Coords(500, 0)

        fun pourGrain() = generateSequence(source) { previous ->
            listOf(
                previous.oneStepDown(),
                previous.oneStepDownAndLeft(),
                previous.oneStepDownAndRight(),
            )
                .find { it !in rocks + grains }
                ?.takeIf { it.y <= maxY }
        }.last()
            .also { if (isHigherThanMaxY(it)) grains.add(it) }

        fun withFloor(): Cave = (maxY + 2).let { source.x - it..source.x + it }
            .map { Coords(it, maxY + 2) }
            .let { Cave(rocks + it) }

        fun isHigherThanMaxY(coords: Coords) = coords.y < maxY
        fun isSource(coords: Coords) = coords == source

        private fun Coords.oneStepDown() = this + Coords(0, 1)
        private fun Coords.oneStepDownAndLeft() = this + Coords(-1, 1)
        private fun Coords.oneStepDownAndRight() = this + Coords(1, 1)
        override fun toString() = (minY..maxY).joinToString(lineSeparator()) { y ->
            (minX..maxX).joinToString("") { x ->
                when (Coords(x, y)) {
                    source -> "+"
                    in rocks -> "#"
                    in grains -> "o"
                    else -> "."
                }
            }
        }
    }
}
